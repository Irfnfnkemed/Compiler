package src.IR.instruction;

import src.IR.IRNode;
import src.Util.type.Type;

public class Alloca extends Instruction {
    public Type type;
    public String varName;

    public Alloca(Type type_, String varName_) {
        type = type_;
        varName = varName_;
    }
}
