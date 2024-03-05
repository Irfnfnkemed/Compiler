package src.optimize.ADCE;


import src.IR.IRProgram;
import src.IR.statement.FuncDef;
import src.optimize.Mem2Reg.CFGDom;
import src.optimize.Mem2Reg.Dom;
import src.optimize.Mem2Reg.PutPhi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class ADCE {

    public IRProgram irProgram;



    public ADCE(IRProgram irProgram_) {
        irProgram = irProgram_;
        for (var stmt : irProgram.stmtList) {
            if (stmt instanceof FuncDef && !Objects.equals(((FuncDef) stmt).functionName, "@.newArray")) {
                CFGDom cfg = new CFGDom((FuncDef) stmt);
                if (cfg.noReturn) {
                    return;//死循环
                }
                cfg.inverse();
                Dom dom = new Dom(cfg, ((FuncDef) stmt).returnLabel);
                FunctionADCE functionADCE = new FunctionADCE(dom);

            }
        }
    }

}
