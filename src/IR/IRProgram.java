package src.IR;

import src.AST.expression.FunctionCallLhsExp;
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;
import src.IR.statement.IRStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IRProgram extends IRNode {
    public List<IRStatement> stmtList;
    public int varDefIndex = 0;
    public int funcDefIndex = 0;

    public IRProgram() {
        stmtList = new ArrayList<>();
    }

    public void push(IRStatement stmt) {
        if (stmt instanceof GlobalVarDef) {
            stmtList.add(varDefIndex++, stmt);
            ++funcDefIndex;
        } else if (stmt instanceof FuncDef) {
            if (Objects.equals(((FuncDef) stmt).functionName, "@main")) {
                stmtList.add(stmt);
            } else {
                stmtList.add(funcDefIndex++, stmt);
            }
        } else {

        }
    }
}
