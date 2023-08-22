package src.mem2Reg;

import src.IR.IRProgram;
import src.IR.instruction.*;
import src.IR.statement.FuncDef;

import java.util.HashMap;

public class Mem2Reg {
    public HashMap<String, String> replaceLabel;//被合并的块名->目标的块名

    public Mem2Reg(IRProgram irProgram) {
        for (var stmt : irProgram.stmtList) {
            if (stmt instanceof FuncDef) {
                while (true) {
                    var cfg = new CFG((FuncDef) stmt);
                    var dom = new Dom(cfg);
                    var putPhi = new PutPhi(dom, (FuncDef) stmt);
                    if (putPhi.replace.size() == 0 && !cfg.change) {
                        cfg = new CFG((FuncDef) stmt);
                        replaceLabel = new HashMap<>();
                        for (var block : cfg.funcBlocks.values()) {//合并能合并的块
                            if (block.pre == 1 && block.prev.get(0).suc == 1) {
                                replaceLabel.put(block.label, block.prev.get(0).label);
                            }
                        }
                        for (String label : replaceLabel.keySet()) {
                            find(label);
                        }
                        ((FuncDef) stmt).irList.clear();
                        Label nowLabel;
                        String findLabel;
                        Block nowBlock;
                        Instruction instr;
                        for (int i = 0; i < ((FuncDef) stmt).labelList.size(); ++i) {
                            nowLabel = ((FuncDef) stmt).labelList.get(i);
                            nowBlock = cfg.funcBlocks.get(nowLabel.labelName);
                            if (replaceLabel.containsKey(nowLabel.labelName)) {
                                ((FuncDef) stmt).labelList.remove(i--);
                                continue;
                            }
                            ((FuncDef) stmt).irList.add(nowLabel);
                            for (int j = 0; j < nowBlock.instructionList.size(); ++j) {
                                instr = nowBlock.instructionList.get(j);
                                if (instr instanceof Br && ((Br) instr).condition == null) {
                                    findLabel = replaceLabel.get(((Br) instr).trueLabel.substring(1));
                                    if (findLabel != null) {
                                        nowBlock.instructionList.addAll(cfg.funcBlocks.get(((Br) instr).trueLabel.substring(1)).instructionList);
                                        continue;
                                    }
                                }
                                ((FuncDef) stmt).irList.add(instr);
                                if (instr instanceof Phi) {
                                    for (var assign : ((Phi) instr).assignBlockList) {
                                        findLabel = replaceLabel.get(assign.label.substring(1));
                                        if (findLabel != null) {
                                            assign.label = "%" + findLabel;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private String find(String label) {
        String fatherLabel = replaceLabel.get(label);
        if (fatherLabel != null) {
            String findLabel = find(fatherLabel);
            replaceLabel.put(label, findLabel);
            return findLabel;
        }
        return label;
    }
}
