package src.ASM.instruction;

public class LB extends ASMInstr {
    public String from = "sp", to;
    public int offset = 0;

    public LB(String to_, int offset_) {
        to = to_;
        offset = offset_;
    }

    public LB(String from_, String to_, int offset_) {
        from = from_;
        to = to_;
        offset = offset_;
    }
}
