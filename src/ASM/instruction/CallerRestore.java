package src.ASM.instruction;

public class CallerRestore extends ASMInstr {
    public CallerSave callerSave;

    public CallerRestore(CallerSave callerSave_) {
        callerSave = callerSave_;
    }
}
