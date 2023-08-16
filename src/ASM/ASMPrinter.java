package src.ASM;

import src.ASM.instruction.*;

public class ASMPrinter {

    public ASMProgram asmProgram;

    public ASMPrinter(ASMProgram asmProgram_) {
        asmProgram = asmProgram_;
    }

    public void print() {
        print(asmProgram.sectionText);
    }

    public void print(Section section) {
        printOut(" .section ", section.sectionName, "\n");
        for (String global : section.globalList) {
            printOut(" .globl ", global, "\n");
        }
        for (var instr : section.ASMInstrList) {
            print(instr);
        }
    }

    public void print(ASMInstr asmInstr) {
        if (asmInstr instanceof LABEL) {
            print((LABEL) asmInstr);
        } else if (asmInstr instanceof ADDI) {
            print((ADDI) asmInstr);
        } else if (asmInstr instanceof SW) {
            print((SW) asmInstr);
        } else if (asmInstr instanceof LW) {
            print((LW) asmInstr);
        } else if (asmInstr instanceof RET) {
            print((RET) asmInstr);
        } else if (asmInstr instanceof LI) {
            print((LI) asmInstr);
        } else if (asmInstr instanceof MV) {
            print((MV) asmInstr);
        } else if (asmInstr instanceof CALL) {
            print((CALL) asmInstr);
        }
    }

    public void print(LABEL label) {
        printOut(label.label, ":\n");
    }

    public void print(ADDI addi) {
        printOut(" addi ", addi.to, " ", addi.from, " ", Integer.toString(addi.imme), "\n");
    }

    public void print(SW sw) {
        printOut(" sw ", sw.from, " ", Integer.toString(sw.offset), "(", sw.to, ")\n");
    }

    public void print(LW lw) {
        printOut(" lw ", lw.to, " ", Integer.toString(lw.offset), "(", lw.from, ")\n");
    }

    public void print(LI li) {
        printOut(" li ", li.to, " ", Integer.toString(li.imme), "\n");
    }

    public void print(MV mv) {
        printOut(" mv ", mv.to, " ", mv.from, "\n");
    }

    public void print(RET ret) {
        printOut(" ret\n");
    }

    public void print(CALL call) {
        printOut(" call ", call.func, "\n");
    }


    public void printOut(String... elements) {
        for (String ele : elements) {
            System.out.print(ele);
        }
    }
}
