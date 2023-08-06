package src.IR.instruction;

import src.IR.IRNode;
import src.Util.type.Type;

public class Ret extends Instruction {
    public Type type;

    public Ret() {
        type = new Type();
        type.setVoid();
    }
}
