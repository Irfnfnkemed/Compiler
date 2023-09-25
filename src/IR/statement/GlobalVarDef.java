package src.IR.statement;

import src.Util.type.IRType;
import src.optimize.LoopInvariant.LoopInvariant;

public class GlobalVarDef extends IRStatement {
    public IRType irType;
    public String varName;
    public FuncDef funcDef;

    public long value = 0;

    public void setFuncDef(LoopInvariant loopInvariant) {
        funcDef = new FuncDef(loopInvariant);
        funcDef.irType = new IRType().setVoid();
        funcDef.functionName = "@.init-" + varName.substring(1);
    }
}
