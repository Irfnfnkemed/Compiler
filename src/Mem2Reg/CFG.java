package src.mem2Reg;

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
    public boolean change = false;//控制流发生改变

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
                    if (((Br) instr).condition != null) {
                        nextBlock = funcBlocks.get(((Br) instr).falseLabel.substring(1));
                        if (nextBlock == null) {
                            nextBlock = new Block(((Br) instr).falseLabel.substring(1));
                            funcBlocks.put(nextBlock.label, nextBlock);
                        }
                        nowBlock.setSuc(nextBlock);
                        nextBlock.setPre(nowBlock);
                    }
                } else if (instr instanceof Alloca) {
                    allocaVar.put(((Alloca) instr).varName, new ArrayList<>());
                    allocaVarType.put(((Alloca) instr).varName, ((Alloca) instr).irType);
                } else if (instr instanceof Store) {
                    if (allocaVar.containsKey(((Store) instr).toPointer)) {
                        var defList = allocaVar.get(((Store) instr).toPointer);
                        if (defList.isEmpty() || !Objects.equals(defList.get(defList.size() - 1), nowBlock.label)) {
                            allocaVar.get(((Store) instr).toPointer).add(nowBlock.label);
                        }
                    }
                }
            }
        }
//        Queue<Block> queue = new ArrayDeque<>();
//        queue.add(funcBlocks.get("entry"));
//        Block block;
//        while (!queue.isEmpty()) {//BFS
//            block = queue.poll();
//            block.visited = true;
//            for (int i = 0; i < block.suc; ++i) {
//                if (!block.next[i].visited) {
//                    queue.add(block.next[i]);
//                }
//            }
//        }
//        var iterator = funcBlocks.values().iterator();
//        while (iterator.hasNext()) {//消除死块
//            var entry = iterator.next();
//            if (!entry.visited) {
//                for (int i = 0; i < entry.suc; ++i) {
//                    block = entry.next[i];
//                    for (int j = 0; j < block.pre; ++j) {
//                        if (block.prev.get(j) == entry) {
//                            block.prev.remove(j);
//                            --block.pre;
//                            break;
//                        }
//                    }
//                }
//                iterator.remove();
//                change = true;
//            }
//        }
        boolean flag = true;
        while (flag) {
            flag = false;
            var iterator = funcBlocks.entrySet().iterator();
            while (iterator.hasNext()) {//消除死块
                var entry = iterator.next();
                if (entry.getValue().prev.size() == 0 && !Objects.equals(entry.getValue().label, "entry")) {
                    funcBlocks.forEach((label, block) -> block.deletePre(entry.getKey()));
                    iterator.remove();
                    change = true;
                    flag = true;//迭代，直至没有死块
                }
            }
        }
    }
}