package src.optimize.Mem2Reg;

import src.IR.instruction.Alloca;
import src.IR.instruction.Br;
import src.IR.instruction.Label;
import src.IR.instruction.Store;
import src.IR.statement.FuncDef;
import src.Util.type.IRType;

import java.util.*;

public class CFGDom {

    public HashMap<String, BlockDom> funcBlocks;//块名->Block节点
    public HashMap<String, List<String>> allocaVar;//alloca的变量，变量名->def的块名列表
    public HashMap<String, IRType> allocaVarType;//alloca的变量名->类型
    public boolean change = false;//控制流发生改变
    public boolean noReturn = false;//是否一定死循环
    public FuncDef funcDef;

    public CFGDom(FuncDef funcDef_) {
        funcDef = funcDef_;
        funcBlocks = new HashMap<>();
        allocaVar = new HashMap<>();
        allocaVarType = new HashMap<>();
        buildCFG();
        collectAlloca();
        eliminateBlock();
    }

    public void buildCFG() {
        BlockDom nowBlockDomDom = null;
        for (var instr : funcDef.irList) {//建图
            if (instr instanceof Label) {
                nowBlockDomDom = funcBlocks.get(((Label) instr).labelName);
                if (nowBlockDomDom == null) {
                    nowBlockDomDom = new BlockDom(((Label) instr).labelName);
                    funcBlocks.put(nowBlockDomDom.label, nowBlockDomDom);
                }
            } else {
                assert nowBlockDomDom != null;
                nowBlockDomDom.pushIR(instr);
                if (instr instanceof Br) {
                    var nextBlock = funcBlocks.get(((Br) instr).trueLabel.substring(1));
                    if (nextBlock == null) {
                        nextBlock = new BlockDom(((Br) instr).trueLabel.substring(1));
                        funcBlocks.put(nextBlock.label, nextBlock);
                    }
                    nextBlock.setPre(nowBlockDomDom);
                    nowBlockDomDom.setSuc(nextBlock);
                    if (((Br) instr).condition != null) {
                        nextBlock = funcBlocks.get(((Br) instr).falseLabel.substring(1));
                        if (nextBlock == null) {
                            nextBlock = new BlockDom(((Br) instr).falseLabel.substring(1));
                            funcBlocks.put(nextBlock.label, nextBlock);
                        }
                        nowBlockDomDom.setSuc(nextBlock);
                        nextBlock.setPre(nowBlockDomDom);
                    }
                }
            }
        }
    }

    public void collectAlloca() {
        for (var label : funcDef.labelList) {//收集alloca信息
            BlockDom blockDom = funcBlocks.get(label.labelName);
            for (var instr : blockDom.instructionList) {
                if (instr instanceof Alloca) {
                    allocaVar.put(((Alloca) instr).varName, new ArrayList<>());
                    allocaVarType.put(((Alloca) instr).varName, ((Alloca) instr).irType);
                } else if (instr instanceof Store) {
                    if (allocaVar.containsKey(((Store) instr).toPointer)) {
                        var defList = allocaVar.get(((Store) instr).toPointer);
                        if (defList.isEmpty() || !Objects.equals(defList.get(defList.size() - 1), blockDom.label)) {
                            allocaVar.get(((Store) instr).toPointer).add(blockDom.label);
                        }
                    }
                }
            }
        }
    }

    public void eliminateBlock() {
        Queue<BlockDom> queue = new ArrayDeque<>();
        queue.add(funcBlocks.get("entry"));
        BlockDom blockDom;
        while (!queue.isEmpty()) {//BFS
            blockDom = queue.poll();
            blockDom.visited = true;
            for (int i = 0; i < blockDom.suc; ++i) {
                if (!(blockDom.next.get(i)).visited) {
                    queue.add(blockDom.next.get(i));
                }
            }
        }
        var iterator = funcBlocks.values().iterator();
        while (iterator.hasNext()) {//消除死块
            var entry = iterator.next();
            if (!(entry).visited) {
                for (int i = 0; i < entry.suc; ++i) {
                    blockDom = entry.next.get(i);
                    for (int j = 0; j < blockDom.pre; ++j) {
                        if (blockDom.prev.get(j) == entry) {
                            blockDom.prev.remove(j);
                            --blockDom.pre;
                            break;
                        }
                    }
                }
                if (Objects.equals(entry.label, funcDef.returnLabel)) {
                    noReturn = true;
                }
                iterator.remove();
                change = true;
            }
        }
    }

    public void inverse() {//建立反图

    }

}
