package src.optimize;

import src.IR.instruction.Br;
import src.IR.instruction.Label;
import src.IR.statement.FuncDef;

import java.util.*;

public class CFGBase {
    public HashMap<String, Block> funcBlocks;//块名->Block节点

    public CFGBase(FuncDef funcDef) {
        funcBlocks = new HashMap<>();
        Block nowBlockDom = null;
        for (var instr : funcDef.irList) {
            if (instr instanceof Label) {
                nowBlockDom = funcBlocks.get(((Label) instr).labelName);
                if (nowBlockDom == null) {
                    nowBlockDom = new Block(((Label) instr).labelName);
                    funcBlocks.put(nowBlockDom.label, nowBlockDom);
                }
            } else {
                assert nowBlockDom != null;
                nowBlockDom.pushIR(instr);
                if (instr instanceof Br) {
                    var nextBlock = funcBlocks.get(((Br) instr).trueLabel.substring(1));
                    if (nextBlock == null) {
                        nextBlock = new Block(((Br) instr).trueLabel.substring(1));
                        funcBlocks.put(nextBlock.label, nextBlock);
                    }
                    nextBlock.setPre(nowBlockDom);
                    nowBlockDom.setSuc(nextBlock);
                    if (((Br) instr).condition != null) {
                        nextBlock = funcBlocks.get(((Br) instr).falseLabel.substring(1));
                        if (nextBlock == null) {
                            nextBlock = new Block(((Br) instr).falseLabel.substring(1));
                            funcBlocks.put(nextBlock.label, nextBlock);
                        }
                        nowBlockDom.setSuc(nextBlock);
                        nextBlock.setPre(nowBlockDom);
                    }
                }
            }
        }
    }
}