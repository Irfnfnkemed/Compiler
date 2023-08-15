package src.ASM;

import src.IR.IRProgram;
import src.IR.instruction.*;
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;
import src.Util.reg.Reg;
import src.Util.reg.TmpVarScope;

public class ASMBuilder {
    public ASMProgram asmProgram;

    public ASMBuilder(IRProgram irProgram) {
        asmProgram = new ASMProgram();
        for (var stmt : irProgram.stmtList) {
            if (stmt instanceof GlobalVarDef) {
                asmProgram.sectionData.pushGlobal(((GlobalVarDef) stmt).varName.substring(1));
            } else if (stmt instanceof FuncDef) {
                asmProgram.sectionData.pushGlobal(((FuncDef) stmt).functionName.substring(1));
                Reg reg = new Reg();
                reg.collect((FuncDef) stmt);
                for (int i = 0; i < ((FuncDef) stmt).irList.size(); ++i) {
                    reg.changeReg();
                    ++reg.nowId;
                }
            }


        }
    }

}
