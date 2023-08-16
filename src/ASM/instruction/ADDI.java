package src.ASM.instruction;

public class ADDI extends ASMInstr {
    public String to, from;
    public int imme;

    public ADDI(String to_, String from_, int imme_) {
        to = to_;
        from = from_;
        imme = imme_;
    }
}
