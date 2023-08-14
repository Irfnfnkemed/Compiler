package src.ASM;

import src.IR.IRProgram;
import src.IR.statement.GlobalVarDef;

public class ASMBuilder {
    public ASMProgram asmProgram;

    public ASMBuilder(IRProgram irProgram) {
        asmProgram = new ASMProgram();
        for (var stmt : irProgram.stmtList) {
            if (stmt instanceof GlobalVarDef) {
                asmProgram.sectionData.pushGlobal(((GlobalVarDef) stmt).varName.substring(1));
            }
        }
    }

}
