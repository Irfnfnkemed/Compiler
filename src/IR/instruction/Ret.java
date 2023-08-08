package src.IR.instruction;

import src.IR.IRNode;
import src.Util.type.Type;

public class Ret extends Instruction {
    public Type type;
    public String var;

    public Ret() {
    }

    public Ret(Type type_, String var_) {
        var = var_;
        type = type_;
    }

}
