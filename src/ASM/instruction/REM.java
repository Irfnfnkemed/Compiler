package src.ASM.instruction;

public class REM extends ASMInstr {
    public String lhs, rhs, to;

    public REM(String lhs_, String rhs_, String to_) {
        lhs = lhs_;
        rhs = rhs_;
        to = to_;
    }
}
