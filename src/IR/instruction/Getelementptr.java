package src.IR.instruction;

import src.Util.type.IRType;

public class Getelementptr extends Instruction {
    public String result;
    public IRType irType;
    public String from;
    public String index;
    public int offset = -1;

    public Getelementptr(String result_, IRType irType_, String from_, int offset_, String index_) {
        result = result_;
        irType = irType_;
        from = from_;
        offset = offset_;
        index = index_;
    }
}
