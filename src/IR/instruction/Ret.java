package src.IR.instruction;

import src.IR.IRNode;
import src.Util.type.Type;

public class Ret extends Instruction {
    public Type type;
    public String retVar;
    public int retValue;

    public Ret() {}

    public Ret(int value) {
        type = new Type();
        type.setInt();
        retValue = value;
    }

    public Ret(Type type_) {
        type = type_;
    }

}
