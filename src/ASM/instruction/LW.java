package src.ASM.instruction;

public class LW extends ASMInstr {
    public String from = "sp", to;
    public int offset = 0;

    public LW(String to_, int offset_) {
        to = to_;
        offset = offset_;
    }

    public LW(String from_, String to_, int offset_) {
        from = from_;
        to = to_;
        offset = offset_;
    }
}
