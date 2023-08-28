package src.ASM.instruction;

public class LI extends ASMInstr {
    public String to;
    public int imme;
    public boolean ignoreDef = false;

    public LI(String to_, int imme_) {
        to = to_;
        imme = imme_;
    }
}
