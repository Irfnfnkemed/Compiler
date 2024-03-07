package src.optimize.ADCE;

import src.IR.instruction.*;
import src.IR.statement.FuncDef;
import src.optimize.Mem2Reg.Dom;

import java.util.*;

public class FunctionADCE {
    public HashMap<String, Instruction> defSet;//变量名->定义
    public HashMap<String, List<Br>> domBr;
    public HashSet<String> activeBlock;
    public HashMap<String, Br> blockBr;//块名->结尾Br

    public Queue<Instruction> activeInstr;
    public Dom dom;
    public FuncDef funcDef;

    public FunctionADCE(Dom dom_) {
        activeInstr = new ArrayDeque<>();
        defSet = new HashMap<>();
        domBr = new HashMap<>();
        activeBlock = new HashSet<>();
        activeBlock.add("entry");
        blockBr = new HashMap<>();
        dom = dom_;
        funcDef = dom_.cfgDom.funcDef;
        setDefAndActive();
        setBrDom();
        markActive();
        rebuildBr();
        recollect();
    }

    private void setDefAndActive() {
        String nowBlockLabel = null;
        for (var instr : funcDef.irList) {
            instr.activeADCE = instr.visitADCE = false;
            if (instr instanceof Label) {
                nowBlockLabel = ((Label) instr).labelName;
            } else if (instr instanceof Br) {
                blockBr.put(((Br) instr).nowLabel.substring(1), ((Br) instr));
            } else if (instr instanceof Binary) {
                defSet.put(((Binary) instr).output, instr);
            } else if (instr instanceof Icmp) {
                defSet.put(((Icmp) instr).output, instr);
            } else if (instr instanceof Load) {
                defSet.put(((Load) instr).toVarName, instr);
            } else if (instr instanceof Store) {
                activeInstr.add(instr);
                instr.activeADCE = true;
                activeBlock.add(nowBlockLabel);
            } else if (instr instanceof Call) {
                if (((Call) instr).resultVar != null) {
                    defSet.put(((Call) instr).resultVar, instr);
                }
                activeInstr.add(instr);
                instr.activeADCE = true;
                activeBlock.add(nowBlockLabel);
            } else if (instr instanceof Phi) {
                defSet.put(((Phi) instr).result, instr);
            } else if (instr instanceof Getelementptr) {
                defSet.put(((Getelementptr) instr).result, instr);
            } else if (instr instanceof Ret) {
                activeInstr.add(instr);
                instr.activeADCE = true;
                activeBlock.add(nowBlockLabel);
            }
            instr.nowBlockADCE = nowBlockLabel;
        }
    }

    private void setBrDom() {
        for (var block : dom.domMap.values()) {
            List<Br> tmp = new ArrayList<>();
            for (String domFrontier : block.domFrontier) {
                tmp.add(blockBr.get(domFrontier));
            }
            domBr.put(block.blockName, tmp);
        }
    }

    private void markActive() {
        while (!activeInstr.isEmpty()) {
            Instruction instr = activeInstr.poll();
            if (!instr.visitADCE) {
                instr.visitADCE = true;
                setDomInstr(instr.nowBlockADCE);
                if (instr instanceof Br) {
                    setActiveInstr(((Br) instr).condition);
                    activeBlock.add(((Br) instr).trueLabel.substring(1));
                    if (((Br) instr).falseLabel != null) {
                        activeBlock.add(((Br) instr).falseLabel.substring(1));
                    }
                } else if (instr instanceof Binary) {
                    setActiveInstr(((Binary) instr).operandLeft);
                    setActiveInstr(((Binary) instr).operandRight);
                } else if (instr instanceof Icmp) {
                    setActiveInstr(((Icmp) instr).operandLeft);
                    setActiveInstr(((Icmp) instr).operandRight);
                } else if (instr instanceof Load) {
                    setActiveInstr(((Load) instr).fromPointer);
                } else if (instr instanceof Store) {
                    setActiveInstr(((Store) instr).valueVar);
                    setActiveInstr(((Store) instr).toPointer);
                } else if (instr instanceof Call) {
                    for (var para : ((Call) instr).callList) {
                        setActiveInstr(para.varName);
                    }
                } else if (instr instanceof Phi) {
                    for (var assign : ((Phi) instr).assignBlockList) {
                        setActiveInstr(assign.var);
                        Br pre_br = blockBr.get(assign.label.substring(1));
                        activeInstr.add(pre_br);
                        pre_br.activeADCE = true;
                    }
                } else if (instr instanceof Getelementptr) {
                    setActiveInstr(((Getelementptr) instr).from);
                    setActiveInstr(((Getelementptr) instr).indexVar);
                } else if (instr instanceof Ret) {
                    setActiveInstr(((Ret) instr).var);
                }
            }
        }
    }

    private void addDomBr(String label, Br br) {
        var tmp = domBr.computeIfAbsent(label, k -> new ArrayList<>());
        tmp.add(br);
    }

    private void setActiveInstr(String varName) {
        if (varName == null || varName.charAt(0) == '@') {
            return;
        }
        Instruction def = defSet.get(varName);
        if (def != null && !def.visitADCE) {
            activeInstr.add(defSet.get(varName));//加入定义
            def.activeADCE = true;
        }
    }

    private void setDomInstr(String nowLabel) {
        activeBlock.add(nowLabel);
        var brList = domBr.get(nowLabel);
        if (brList != null) {
            for (Br br : brList) {//添加控制依赖
                if (!br.visitADCE) {
                    activeInstr.add(br);
                    br.activeADCE = true;
                }
            }
        }
    }

    private void rebuildBr() {
        for (Br br : blockBr.values()) {
            if ((activeBlock.contains(br.nowBlockADCE)) && !br.activeADCE) {
                String label = dom.domMap.get(br.nowBlockADCE).immeDom.blockName;
                while (!activeBlock.contains(label)) {
                    label = dom.domMap.get(label).immeDom.blockName;
                }
                br.condition = null;
                br.trueLabel = "%" + label;
                br.activeADCE = true;
            }
        }
    }

    private void recollect() {
        dom.cfgDom.funcDef.labelList.clear();
        List<Instruction> newList = new ArrayList<>();
        for (Instruction instr : funcDef.irList) {
            if (instr.activeADCE) {
                newList.add(instr);
            } else if (instr instanceof Label && activeBlock.contains(((Label) instr).labelName)) {
                dom.cfgDom.funcDef.labelList.add((Label) instr);
                newList.add(instr);
            }
        }
        funcDef.irList = newList;
    }
}
