package src.optimize.Mem2Reg;

import src.IR.instruction.Alloca;
import src.IR.instruction.Store;
import src.IR.statement.FuncDef;
import src.Util.type.IRType;
import src.optimize.Block;
import src.optimize.CFGBase;

import java.util.*;

public class CFGDom extends CFGBase {
    public HashMap<String, List<String>> allocaVar;//alloca的变量，变量名->def的块名列表
    public HashMap<String, IRType> allocaVarType;//alloca的变量名->类型
    public boolean change = false;//控制流发生改变

    public CFGDom(FuncDef funcDef) {
        super(funcDef);
        allocaVar = new HashMap<>();
        allocaVarType = new HashMap<>();
        for (var label : funcDef.labelList) {
            Block block = funcBlocks.get(label.labelName);
            for (var instr : block.instructionList) {
                if (instr instanceof Alloca) {
                    allocaVar.put(((Alloca) instr).varName, new ArrayList<>());
                    allocaVarType.put(((Alloca) instr).varName, ((Alloca) instr).irType);
                } else if (instr instanceof Store) {
                    if (allocaVar.containsKey(((Store) instr).toPointer)) {
                        var defList = allocaVar.get(((Store) instr).toPointer);
                        if (defList.isEmpty() || !Objects.equals(defList.get(defList.size() - 1), block.label)) {
                            allocaVar.get(((Store) instr).toPointer).add(block.label);
                        }
                    }
                }
            }
        }
        Queue<Block> queue = new ArrayDeque<>();
        queue.add(funcBlocks.get("entry"));
        Block block;
        while (!queue.isEmpty()) {//BFS
            block = queue.poll();
            block.visited = true;
            for (int i = 0; i < block.suc; ++i) {
                if (!(block.next[i]).visited) {
                    queue.add(block.next[i]);
                }
            }
        }
        var iterator = funcBlocks.values().iterator();
        while (iterator.hasNext()) {//消除死块
            var entry = iterator.next();
            if (!(entry).visited) {
                for (int i = 0; i < entry.suc; ++i) {
                    block = entry.next[i];
                    for (int j = 0; j < block.pre; ++j) {
                        if (block.prev.get(j) == entry) {
                            block.prev.remove(j);
                            --block.pre;
                            break;
                        }
                    }
                }
                iterator.remove();
                change = true;
            }
        }
    }
}
