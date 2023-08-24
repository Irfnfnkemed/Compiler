package src.optimize.Mem2Reg;

import src.IR.IRProgram;
import src.IR.instruction.*;
import src.IR.statement.FuncDef;
import src.optimize.Block;
import src.optimize.RegAllocation.CFGReg;

import java.util.HashMap;

public class Mem2Reg {
    public HashMap<String, String> replaceLabel;//被合并的块名->目标的块名

    public Mem2Reg(IRProgram irProgram) {
        for (var stmt : irProgram.stmtList) {
            if (stmt instanceof FuncDef) {
                while (true) {
                    CFGDom cfg = new CFGDom((FuncDef) stmt);
                    Dom dom = new Dom(cfg);
                    PutPhi putPhi = new PutPhi(dom, (FuncDef) stmt);
                    if (putPhi.replace.size() == 0 && !cfg.change) {
                        merge((FuncDef) stmt);//合并可以合并的块
                        break;
                    }
                }
                CFGReg cfgReg = new CFGReg((FuncDef) stmt);
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

    private void merge(FuncDef stmt) {
        CFGDom cfg = new CFGDom(stmt);
        replaceLabel = new HashMap<>();
        for (var block : cfg.funcBlocks.values()) {//合并能合并的块
            if (block.pre == 1 && block.prev.get(0).suc == 1) {
                replaceLabel.put(block.label, block.prev.get(0).label);
            }
        }
        for (String label : replaceLabel.keySet()) {
            find(label);
        }
        stmt.irList.clear();
        Label nowLabel;
        String findLabel;
        Block nowBlock;
        Instruction instr;
        for (int i = 0; i < stmt.labelList.size(); ++i) {
            nowLabel = stmt.labelList.get(i);
            nowBlock = cfg.funcBlocks.get(nowLabel.labelName);
            if (replaceLabel.containsKey(nowLabel.labelName)) {
                stmt.labelList.remove(i--);
                continue;
            }
            stmt.irList.add(nowLabel);
            for (int j = 0; j < nowBlock.instructionList.size(); ++j) {
                instr = nowBlock.instructionList.get(j);
                if (instr instanceof Br && ((Br) instr).condition == null) {
                    findLabel = replaceLabel.get(((Br) instr).trueLabel.substring(1));
                    if (findLabel != null) {
                        nowBlock.instructionList.addAll(cfg.funcBlocks.get(((Br) instr).trueLabel.substring(1)).instructionList);
                        continue;
                    }
                }
                stmt.irList.add(instr);
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
    }
}