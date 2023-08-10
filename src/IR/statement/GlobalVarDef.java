package src.IR.statement;

import src.Util.type.IRType;
import src.Util.type.Type;

public class GlobalVarDef extends IRStatement {
    public Type type;
    public String varName;
    public FuncDef funcDef;

    public long value = 0;

    public void setFuncDef() {
        funcDef = new FuncDef();
        funcDef.irType = new IRType().setVoid();
        funcDef.functionName = "@init-" + varName.substring(1);
    }
}
