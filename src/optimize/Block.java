package src.optimize;

import src.IR.IRNode;
import src.IR.instruction.Instruction;
import src.IR.instruction.Phi;

import java.util.*;

public class Block{
    public List<Instruction> instructionList;
    public String label;
    public Block[] next;
    public List<Block> prev;
    public int pre = 0, suc = 0;//前驱、后继个数
    public HashMap<String, Phi> insertPhi;//要插入的phi，局部变量名->phi指令
    public boolean renamed = false;//是否已经重命名过各操作
    public boolean visited = false;//用于建图时消除死块

    public Block(String label_) {
        label = label_;
        instructionList = new LinkedList<>();
        next = new Block[2];
        prev = new LinkedList<>();
        insertPhi = new HashMap<>();
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

}
