package src.ASM.instruction;

public class SW extends ASMInstr {
    public String from, to;
    public int offset;

    public SW(String from_, String to_, int offset_) {
        from = from_;
        to = to_;
        offset = offset_;
    }
}
