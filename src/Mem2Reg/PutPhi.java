package src.Mem2Reg;

import src.IR.instruction.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class PutPhi {
    public static class varDef {
        public String varName;
        public long varValue;
        public String defLabel;//定义所在的块

        public varDef(String varName_, String defLabel_) {
            varName = varName_;
            defLabel = defLabel_;
        }

        public varDef(String varName_, long varValue_, String defLabel_) {
            varName = varName_;
            varValue = varValue_;
            defLabel = defLabel_;
        }
    }

    public static class variable {
        public String varName;
        public long varValue;

        public variable(String varName_) {
            varName = varName_;
        }

        public variable(long varValue_) {
            varValue = varValue_;
        }

        public variable(varDef varDef_) {
            varName = varDef_.varName;
            varValue = varDef_.varValue;

        }
    }

    public CFG cfg;
    public Dom dom;
    public HashMap<String, Stack<varDef>> varRename;//重命名栈
    public HashMap<String, variable> replace;//替换变量

    public PutPhi(Dom dom_) {
        cfg = dom_.cfg;
        dom = dom_;
        varRename = new HashMap<>();
        cfg.allocaVar.keySet().forEach(varName -> varRename.put(varName, new Stack<>()));
        replace = new HashMap<>();
        setPhiPos();
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
    }

    private void renameBlock(Block block) {
        for (var entry : block.insertPhi.entrySet()) {//phi变量放置
            var varDef = varRename.get(entry.getKey()).pop();
            if (varDef.varName == null) {
                entry.getValue().push(varDef.varValue, "%" + varDef.defLabel);
            } else {
                entry.getValue().push(varDef.varName, "%" + varDef.defLabel);
            }
            varRename.get(entry.getKey()).push(new varDef(entry.getValue().result, block.label));
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
                        ((Br) inst).condition = tmpVar.varName;
                    }
                }
            } else if (inst instanceof Call) {
                for (int i = 0; i < ((Call) inst).varNameList.size(); ++i) {
                    tmpVar = replace.get(((Call) inst).varNameList.get(i));
                    if (tmpVar != null) {
                        ((Call) inst).varNameList.set(i, tmpVar.varName);
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
                replace.put(((Load) inst).toVarName, new variable(varRename.get(((Load) inst).fromPointer).peek()));
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
                    }
                }
            } else if (inst instanceof Store) {
                if (((Store) inst).valueVar != null) {
                    varRename.get(((Store) inst).toPointer).push(
                            new varDef(((Store) inst).valueVar, ((Store) inst).value, block.label));
                }
            }
            block.renamed = true;

        }
    }
}
