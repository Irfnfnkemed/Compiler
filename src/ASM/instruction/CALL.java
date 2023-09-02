package src.ASM.instruction;

import java.util.ArrayList;
import java.util.List;

public class CALL extends ASMInstr {
    public String func;
    public List<String> useList;//用到的虚拟寄存器(预染色为a0-a7)

    public CALL(String func_) {
        func = func_;
        useList = new ArrayList<>();
    }
}
