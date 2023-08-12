package src.IR;

import src.IR.statement.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class IRProgram extends IRNode {
    public List<IRStatement> stmtList;
    public int classDefIndex = 1;
    public int varDefIndex = 1;
    public int funcDefIndex = 1;

    public IRProgram() {
        stmtList = new LinkedList<>();
        stmtList.add(new ConstString());
    }

    public void push(IRStatement stmt) {
        if (stmt instanceof ClassTypeDef) {
            stmtList.add(classDefIndex++, stmt);
            ++varDefIndex;
            ++funcDefIndex;
        } else if (stmt instanceof GlobalVarDef) {
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

    public String pushConstString(String content) {
        return ((ConstString) stmtList.get(0)).push(content);
    }
}
