package src.IR.instruction;

import src.Util.type.IRType;

import java.util.ArrayList;
import java.util.List;

public class Phi extends Instruction {
    public static class assignBlock {
        public String var;
        public long value;
        public String label;

        public assignBlock(String var_, long value_, String label_) {
            var = var_;
            value = value_;
            label = label_;
        }
    }

    public IRType irType;
    public String result;
    public List<assignBlock> assignBlockList;

    public Phi(IRType irType_, String result_) {
        irType = irType_;
        result = result_;
        assignBlockList = new ArrayList<>();
    }

    public void push(String var_, String label_) {
        assignBlockList.add(new assignBlock(var_, 0, label_));
    }

    public void push(long value_, String label_) {
        assignBlockList.add(new assignBlock(null, value_, label_));
    }

}
