package src.optimize.ADCE;


import src.IR.IRProgram;
import src.IR.statement.FuncDef;
import src.optimize.Mem2Reg.CFGDom;
import src.optimize.Mem2Reg.Dom;
import src.optimize.Mem2Reg.PutPhi;

import java.util.HashMap;

public class ADCE {

    public IRProgram irProgram;

    public ADCE(IRProgram irProgram_) {
        irProgram = irProgram_;
        for (var stmt : irProgram.stmtList) {
            if (stmt instanceof FuncDef) {
                while (true) {
                    CFGDom cfg = new CFGDom((FuncDef) stmt);
                    cfg.inverse();



//                    Dom dom = new Dom(cfg);
//                    PutPhi putPhi = new PutPhi(dom, (FuncDef) stmt);
//                    if (dom.dfnList.size() > 4000) {
//                        break;
//                    }
//                    if (putPhi.replace.size() == 0 && !cfg.change) {
//                        merge((FuncDef) stmt);//合并可以合并的块
//                        break;
//                    }
                }
            }
        }
    }

}
