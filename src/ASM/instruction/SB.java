package src.ASM.instruction;
public class SB extends ASMInstr {
    public String from, to = "sp";
    public int offset = 0;

    public SB(String from_, int offset_) {
        from = from_;
        offset = offset_;
    }

    public SB(String from_, String to_, int offset_) {
        from = from_;
        to = to_;
        offset = offset_;
    }
}

