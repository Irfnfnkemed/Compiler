package src.Mem2Reg;

import src.IR.IRNode;
import src.IR.instruction.Instruction;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Block extends IRNode {
    public List<Instruction> instructionList;
    public String label;
    public Block[] next;
    public List<Block> prev;
    public HashSet<String> defList;//在块内def的变量
    public int pre = 0, suc = 0;//前驱、后继个数

    public Block(String label_) {
        label = label_;
        instructionList = new LinkedList<>();
        next = new Block[2];
        prev = new LinkedList<>();
        defList = new HashSet<>();
    }

    public void pushIR(Instruction instruction) {
        instructionList.add(instruction);
    }

    public void setPre(Block preBlock) {
        prev.add(preBlock);
        ++pre;
    }

    public void setSuc(Block sucBlock) {
        next[suc++] = sucBlock;
    }

    public void deletePre(String preLabel) {
        for (int i = 0; i < prev.size(); ++i) {
            if (Objects.equals(prev.get(i).label, preLabel)) {
                prev.remove(i);
                --pre;
                break;
            }
        }
    }

    public void deleteSuc(String sucLabel) {
        if (suc == 0) {
            return;
        }
        if (suc > 0 && Objects.equals(next[0].label, sucLabel)) {
            --suc;
            if (suc == 1) {
                next[0] = next[1];
                next[1] = null;
            }
        } else if (suc > 1 && Objects.equals(next[1].label, sucLabel)) {
            --suc;
            next[1] = null;
        }
    }
}
