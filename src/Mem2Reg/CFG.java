package src.Mem2Reg;

import src.IR.instruction.Alloca;
import src.IR.instruction.Br;
import src.IR.instruction.Label;
import src.IR.instruction.Store;
import src.IR.statement.FuncDef;
import src.Util.type.IRType;

import java.util.*;

public class CFG {
    public HashMap<String, Block> funcBlocks;//块名->Block节点
    public HashMap<String, List<String>> allocaVar;//alloca的变量，变量名->def的块名列表
    public HashMap<String, IRType> allocaVarType;//alloca的变量名->类型

    public CFG(FuncDef funcDef) {
        funcBlocks = new HashMap<>();
        allocaVar = new HashMap<>();
        allocaVarType = new HashMap<>();
        Block nowBlock = null;
        for (var instr : funcDef.irList) {
            if (instr instanceof Label) {
                nowBlock = funcBlocks.get(((Label) instr).labelName);
                if (nowBlock == null) {
                    nowBlock = new Block(((Label) instr).labelName);
                    funcBlocks.put(nowBlock.label, nowBlock);
                }
            } else {
                assert nowBlock != null;
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
                } else if (instr instanceof Alloca) {
                    List<String> defList = new ArrayList<>();
                    defList.add(nowBlock.label);
                    allocaVar.put(((Alloca) instr).varName, defList);
                    allocaVarType.put(((Alloca) instr).varName, ((Alloca) instr).irType);
                } else if (instr instanceof Store) {
                    if (allocaVar.containsKey(((Store) instr).toPointer)) {
                        var defList = allocaVar.get(((Store) instr).toPointer);
                        if (!Objects.equals(defList.get(defList.size() - 1), nowBlock.label)) {
                            allocaVar.get(((Store) instr).toPointer).add(nowBlock.label);
                        }
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