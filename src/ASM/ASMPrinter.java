package src.ASM;

import src.ASM.instruction.*;
import src.ASM.instruction.binary.*;
import src.ASM.instruction.binaryImme.*;

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
        } else if (asmInstr instanceof ADD) {
            print((ADD) asmInstr);
        } else if (asmInstr instanceof SUB) {
            print((SUB) asmInstr);
        } else if (asmInstr instanceof MUL) {
            print((MUL) asmInstr);
        } else if (asmInstr instanceof DIV) {
            print((DIV) asmInstr);
        } else if (asmInstr instanceof REM) {
            print((REM) asmInstr);
        } else if (asmInstr instanceof SLL) {
            print((SLL) asmInstr);
        } else if (asmInstr instanceof SLLI) {
            print((SLLI) asmInstr);
        } else if (asmInstr instanceof SRA) {
            print((SRA) asmInstr);
        } else if (asmInstr instanceof SRAI) {
            print((SRAI) asmInstr);
        } else if (asmInstr instanceof AND) {
            print((AND) asmInstr);
        } else if (asmInstr instanceof ANDI) {
            print((ANDI) asmInstr);
        } else if (asmInstr instanceof OR) {
            print((OR) asmInstr);
        } else if (asmInstr instanceof ORI) {
            print((ORI) asmInstr);
        } else if (asmInstr instanceof XOR) {
            print((XOR) asmInstr);
        } else if (asmInstr instanceof XORI) {
            print((XORI) asmInstr);
        } else if (asmInstr instanceof SLT) {
            print((SLT) asmInstr);
        } else if (asmInstr instanceof SLTI) {
            print((SLTI) asmInstr);
        } else if (asmInstr instanceof SGT) {
            print((SGT) asmInstr);
        } else if (asmInstr instanceof SGTI) {
            print((SGTI) asmInstr);
        } else if (asmInstr instanceof SEQZ) {
            print((SEQZ) asmInstr);
        } else if (asmInstr instanceof SNEZ) {
            print((SNEZ) asmInstr);
        } else if (asmInstr instanceof J) {
            print((J) asmInstr);
        }
    }

    public void print(LABEL label) {
        printOut(label.label, ":\n");
    }

    public void print(ADDI addi) {
        printOut("  addi ", addi.to, " ", addi.from, " ", Integer.toString(addi.imme), "\n");
    }

    public void print(J j) {
        printOut("  j ", j.toLabel, "\n");
    }

    public void print(ANDI andi) {
        printOut("  andi ", andi.to, " ", andi.from, " ", Integer.toString(andi.imme), "\n");
    }

    public void print(SLLI slli) {
        printOut("  slli ", slli.to, " ", slli.from, " ", Integer.toString(slli.imme), "\n");
    }

    public void print(SRAI srai) {
        printOut("  srai ", srai.to, " ", srai.from, " ", Integer.toString(srai.imme), "\n");
    }

    public void print(ORI ori) {
        printOut("  ori ", ori.to, " ", ori.from, " ", Integer.toString(ori.imme), "\n");
    }

    public void print(XORI xori) {
        printOut("  xori ", xori.to, " ", xori.from, " ", Integer.toString(xori.imme), "\n");
    }

    public void print(SLTI slti) {
        printOut("  slti ", slti.to, " ", slti.from, " ", Integer.toString(slti.imme), "\n");
    }

    public void print(SGTI sgti) {
        printOut("  sgti ", sgti.to, " ", sgti.from, " ", Integer.toString(sgti.imme), "\n");
    }


    public void print(SW sw) {
        printOut("  sw ", sw.from, " ", Integer.toString(sw.offset), "(", sw.to, ")\n");
    }

    public void print(LW lw) {
        printOut("  lw ", lw.to, " ", Integer.toString(lw.offset), "(", lw.from, ")\n");
    }

    public void print(LI li) {
        printOut("  li ", li.to, " ", Integer.toString(li.imme), "\n");
    }

    public void print(MV mv) {
        printOut("  mv ", mv.to, " ", mv.from, "\n");
    }

    public void print(RET ret) {
        printOut("  ret\n");
    }

    public void print(CALL call) {
        printOut("  call ", call.func, "\n");
    }

    public void print(ADD add) {
        printOut("  add ", add.to, " ", add.lhs, " ", add.rhs, "\n");
    }

    public void print(AND and) {
        printOut("  and ", and.to, " ", and.lhs, " ", and.rhs, "\n");
    }

    public void print(SUB sub) {
        printOut("  sub ", sub.to, " ", sub.lhs, " ", sub.rhs, "\n");
    }

    public void print(MUL mul) {
        printOut("  mul ", mul.to, " ", mul.lhs, " ", mul.rhs, "\n");
    }

    public void print(DIV div) {
        printOut("  div ", div.to, " ", div.lhs, " ", div.rhs, "\n");
    }

    public void print(SLL sll) {
        printOut("  sll ", sll.to, " ", sll.lhs, " ", sll.rhs, "\n");
    }

    public void print(SRA sra) {
        printOut("  sra ", sra.to, " ", sra.lhs, " ", sra.rhs, "\n");
    }

    public void print(SLT slt) {
        printOut("  slt ", slt.to, " ", slt.lhs, " ", slt.rhs, "\n");
    }

    public void print(SGT sgt) {
        printOut("  sgt ", sgt.to, " ", sgt.lhs, " ", sgt.rhs, "\n");
    }

    public void print(OR or) {
        printOut("  or ", or.to, " ", or.lhs, " ", or.rhs, "\n");
    }

    public void print(XOR xor) {
        printOut("  xor ", xor.to, " ", xor.lhs, " ", xor.rhs, "\n");
    }

    public void print(REM rem) {
        printOut("  rem ", rem.to, " ", rem.lhs, " ", rem.rhs, "\n");
    }

    public void print(SEQZ seqz) {
        printOut("  seqz ", seqz.to, " ", seqz.from, "\n");
    }

    public void print(SNEZ snez) {
        printOut("  snez ", snez.to, " ", snez.from, "\n");
    }


    public void printOut(String... elements) {
        for (String ele : elements) {
            System.out.print(ele);
        }
    }
}
