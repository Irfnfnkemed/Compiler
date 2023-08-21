package src.Mem2Reg;

import src.IR.IRProgram;
import src.IR.instruction.*;
import src.IR.statement.FuncDef;

import java.util.*;

public class PutPhi {

    public static class variable {
        public String varName;
        public long varValue;

        public variable(String varName_) {
            varName = varName_;
        }

        public variable(long varValue_) {
            varValue = varValue_;
        }

        public variable(String varName_, long varValue_) {
            varName = varName_;
            varValue = varValue_;
        }

        public variable(variable variable_) {
            varName = variable_.varName;
            varValue = variable_.varValue;
        }


    }

    public CFG cfg;
    public Dom dom;
    public HashMap<String, Stack<variable>> varRename;//重命名栈
    public HashMap<String, variable> replace;//替换变量
    public FuncDef funcDef;

    public PutPhi(Dom dom_, FuncDef funcDef_) {
        cfg = dom_.cfg;
        dom = dom_;
        funcDef = funcDef_;
        varRename = new HashMap<>();
        cfg.allocaVar.keySet().forEach(varName -> varRename.put(varName, new Stack<>()));
        replace = new HashMap<>();
        setPhiPos();
        rename();
        recollect();
    }

    public void setPhiPos() {
        HashSet<String> defLabelSet = new HashSet<>();//有def的块
        for (var entry : cfg.allocaVar.entrySet()) {
            int index = 0;
            defLabelSet.clear();
            String varName = entry.getKey();
            var defList = entry.getValue();
            defLabelSet.addAll(defList);
            while (index < defList.size()) {
                for (String putBlockLabel : dom.domMap.get(defList.get(index)).domFrontier) {
                    if (!cfg.funcBlocks.get(putBlockLabel).insertPhi.containsKey(varName)) {
                        cfg.funcBlocks.get(putBlockLabel).insertPhi.put(
                                varName, new Phi(cfg.allocaVarType.get(varName), varName + "-" + putBlockLabel));
                    }
                    if (!defLabelSet.contains(putBlockLabel)) {
                        defList.add(putBlockLabel);
                        defLabelSet.add(putBlockLabel);
                    }
                }
                ++index;
            }
        }
    }

    public void rename() {
        Block root = cfg.funcBlocks.get("entry");
        renameBlock(root, null);
    }

    private void renameBlock(Block block, String fromLabel) {
        for (var entry : block.insertPhi.entrySet()) {//phi变量放置
            if (!varRename.get(entry.getKey()).isEmpty()) {
                boolean newLabel = true;
                var varDef = varRename.get(entry.getKey()).peek();
                for (var assign : entry.getValue().assignBlockList) {
                    if (Objects.equals(assign.label, "%" + fromLabel)) {
                        newLabel = false;
                        break;
                    }
                }
                if (!newLabel) {
                    continue;
                }
                if (varDef.varName == null) {
                    entry.getValue().push(varDef.varValue, "%" + fromLabel);
                } else {
                    entry.getValue().push(varDef.varName, "%" + fromLabel);
                }
                varRename.get(entry.getKey()).push(new variable(entry.getValue().result));
            }
        }
        if (block.renamed) {
            return;
        }
        variable tmpVar;
        for (var inst : block.instructionList) {
            if (inst instanceof Binary) {
                if (((Binary) inst).operandLeft != null) {
                    tmpVar = replace.get(((Binary) inst).operandLeft);
                    if (tmpVar != null) {
                        ((Binary) inst).operandLeft = tmpVar.varName;
                        ((Binary) inst).valueLeft = tmpVar.varValue;
                    }
                }
                if (((Binary) inst).operandRight != null) {
                    tmpVar = replace.get(((Binary) inst).operandRight);
                    if (tmpVar != null) {
                        ((Binary) inst).operandRight = tmpVar.varName;
                        ((Binary) inst).valueRight = tmpVar.varValue;
                    }
                }
            } else if (inst instanceof Br) {
                if (((Br) inst).condition != null) {
                    tmpVar = replace.get(((Br) inst).condition);
                    if (tmpVar != null) {
                        if (tmpVar.varName != null) {
                            ((Br) inst).condition = tmpVar.varName;
                        } else {
                            ((Br) inst).condition = null;
                            if (tmpVar.varValue == 0) {
                                ((Br) inst).falseLabel = ((Br) inst).trueLabel;
                            }
                        }

                    }
                }
            } else if (inst instanceof Call) {
                for (var variable : ((Call) inst).callList) {
                    if (variable.varName != null) {
                        tmpVar = replace.get(variable.varName);
                        if (tmpVar != null) {
                            variable.varName = tmpVar.varName;
                            variable.varValue = tmpVar.varValue;
                        }
                    }
                }
            } else if (inst instanceof Getelementptr) {
                if (((Getelementptr) inst).indexVar != null) {
                    if (((Getelementptr) inst).indexVar != null) {
                        tmpVar = replace.get(((Getelementptr) inst).indexVar);
                        if (tmpVar != null) {
                            ((Getelementptr) inst).indexVar = tmpVar.varName;
                        }
                    }
                }
                tmpVar = replace.get(((Getelementptr) inst).from);
                if (tmpVar != null) {
                    ((Getelementptr) inst).from = tmpVar.varName;
                }
            } else if (inst instanceof Icmp) {
                if (((Icmp) inst).operandLeft != null) {
                    tmpVar = replace.get(((Icmp) inst).operandLeft);
                    if (tmpVar != null) {
                        ((Icmp) inst).operandLeft = tmpVar.varName;
                        ((Icmp) inst).valueLeft = tmpVar.varValue;
                    }
                }
                if (((Icmp) inst).operandRight != null) {
                    tmpVar = replace.get(((Icmp) inst).operandRight);
                    if (tmpVar != null) {
                        ((Icmp) inst).operandRight = tmpVar.varName;
                        ((Icmp) inst).valueRight = tmpVar.varValue;
                    }
                }
            } else if (inst instanceof Load) {
                if (cfg.allocaVar.containsKey(((Load) inst).fromPointer)) {
                    replace.put(((Load) inst).toVarName, new variable(varRename.get(((Load) inst).fromPointer).peek()));
                    ((Load) inst).toVarName = null;//表示改指令会在后续删去
                }
            } else if (inst instanceof Phi) {
                for (var assignBlock : ((Phi) inst).assignBlockList) {
                    if (assignBlock.var != null) {
                        tmpVar = replace.get(assignBlock.var);
                        if (tmpVar != null) {
                            assignBlock.var = tmpVar.varName;
                        }
                    }
                }
            } else if (inst instanceof Ret) {
                if (((Ret) inst).var != null) {
                    tmpVar = replace.get(((Ret) inst).var);
                    if (tmpVar != null) {
                        ((Ret) inst).var = tmpVar.varName;
                        ((Ret) inst).value = (int) tmpVar.varValue;
                    }
                }
            } else if (inst instanceof Store) {
                if (((Store) inst).valueVar != null) {
                    tmpVar = replace.get(((Store) inst).valueVar);
                    if (tmpVar != null) {
                        ((Store) inst).value = tmpVar.varValue;
                        ((Store) inst).valueVar = tmpVar.varName;
                    }
                }
                if (cfg.allocaVar.containsKey(((Store) inst).toPointer)) {
                    if (((Store) inst).valueVar == null) {
                        varRename.get(((Store) inst).toPointer).push(new variable(((Store) inst).value));
                    } else {
                        varRename.get(((Store) inst).toPointer).push(new variable(((Store) inst).valueVar));
                    }
                    ((Store) inst).toPointer = null;//表示改指令会在后续删去
                }
            }
        }
        block.renamed = true;
        HashMap<String, variable> restoreStack = new HashMap<>();
        for (var entry : varRename.entrySet()) {
            if (!entry.getValue().empty()) {
                restoreStack.put(entry.getKey(), entry.getValue().peek());
            }
        }
        for (int i = 0; i < block.suc; ++i) {
            renameBlock(block.next[i], block.label);
            for (var entry : varRename.entrySet()) {//恢复栈
                var restore = restoreStack.get(entry.getKey());
                var stack = entry.getValue();
                if (restore != null) {
                    while (restore != stack.peek()) {
                        stack.pop();
                    }
                } else {
                    stack.clear();
                }
            }
        }
    }

    private void recollect() {
        var irList = funcDef.irList;
        Block nowBlock = null;
        Instruction instruction;
        for (int i = 0; i < irList.size(); ++i) {
            instruction = irList.get(i);
            if (instruction instanceof Label) {
                nowBlock = cfg.funcBlocks.get(((Label) instruction).labelName);
                if (nowBlock == null) {
                    irList.remove(i--);
                    continue;
                }
                for (var phi : nowBlock.insertPhi.values()) {//放置phi
                    if (phi.assignBlockList.size() == nowBlock.pre) {//分支数不等，表明实际上该变量不会在该块中作用
                        irList.add(++i, phi);
                    }
                }
            } else {
                if (nowBlock == null) {
                    irList.remove(i--);
                    continue;
                }
                if (instruction instanceof Load) {
                    if (((Load) instruction).toVarName == null) {
                        irList.remove(i--);
                    }
                } else if (instruction instanceof Store) {
                    if (((Store) instruction).toPointer == null) {
                        irList.remove(i--);
                    }
                } else if (instruction instanceof Alloca) {
                    irList.remove(i--);
                }
            }
        }
    }
}
