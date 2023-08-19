package src.Mem2Reg;

import src.IR.IRProgram;
import src.IR.instruction.Br;
import src.IR.instruction.Label;
import src.IR.statement.FuncDef;

import java.util.HashMap;
import java.util.Objects;

public class CFG {
    public HashMap<String, Block> funcBlocks;//函数名->(块名->Block节点)

    public CFG(FuncDef funcDef) {
        funcBlocks = new HashMap<String, Block>();
        Block nowBlock = null;
        for (var instr : funcDef.irList) {
            if (instr instanceof Label) {
                nowBlock = funcBlocks.get(((Label) instr).labelName);
                if (nowBlock == null) {
                    nowBlock = new Block(((Label) instr).labelName);
                    funcBlocks.put(nowBlock.label, nowBlock);
                }
            } else {
                nowBlock.pushIR(instr);
                if (instr instanceof Br) {
                    var nextBlock = funcBlocks.get(((Br) instr).trueLabel.substring(1));
                    if (nextBlock == null) {
                        nextBlock = new Block(((Br) instr).trueLabel.substring(1));
                        funcBlocks.put(nextBlock.label, nextBlock);
                    }
                    nextBlock.setPre(nowBlock);
                    nowBlock.setSuc(nextBlock);
                    if (((Br) instr).falseLabel != null) {
                        nextBlock = funcBlocks.get(((Br) instr).falseLabel.substring(1));
                        if (nextBlock == null) {
                            nextBlock = new Block(((Br) instr).falseLabel.substring(1));
                            funcBlocks.put(nextBlock.label, nextBlock);
                        }
                        nowBlock.setSuc(nextBlock);
                        nextBlock.setPre(nowBlock);
                    }
                }

            }
        }
        var iterator = funcBlocks.entrySet().iterator();
        while (iterator.hasNext()) {//消除死块
            var entry = iterator.next();
            if (entry.getValue().prev.size() == 0 && !Objects.equals(entry.getValue().label, "entry")) {
                funcBlocks.forEach((label, block) -> block.deletePre(entry.getKey()));
                iterator.remove();
            } else if (entry.getValue().suc == 0 && !Objects.equals(entry.getValue().label, "returnLabel") &&
                    !Objects.equals(entry.getValue().label, "entry")) {
                funcBlocks.forEach((label, block) -> block.deleteSuc(entry.getKey()));
                iterator.remove();
            }
        }
    }
}