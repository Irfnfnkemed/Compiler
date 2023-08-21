package src.IR.instruction;

import src.IR.IRNode;
import src.Util.type.IRType;
import src.Util.type.Type;

public class Ret extends Instruction {
    public IRType irType;
    public String var;
    public int value;

    public Ret() {
    }

    public Ret(Type type_, String var_) {
        var = var_;
        irType = new IRType(type_);
    }

    public Ret(IRType irType_, String var_) {
        var = var_;
        irType = irType_;
    }
}
