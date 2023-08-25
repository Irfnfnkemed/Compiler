package src.ASM.instruction;

public class MV extends ASMInstr {
    public String from, to;
    public boolean ignoreDef = false, ignoreUse = false;

    public MV(String from_, String to_) {
        from = from_;
        to = to_;
    }
}
