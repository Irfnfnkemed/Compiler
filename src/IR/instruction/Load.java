package src.IR.instruction;

import src.IR.IRNode;
import src.Util.type.Type;

public class Load extends Instruction {
    public Type type;
    public String toVarName;//存到匿名变量里
    public String fromPointer;

    public Load(Type type_, String toVarName_, String fromPointer_) {
        type = type_;
        toVarName = toVarName_;
        fromPointer = fromPointer_;
    }
}
