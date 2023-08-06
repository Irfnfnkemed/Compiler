package src.IR;

import src.IR.statement.IRStatement;

import java.util.ArrayList;
import java.util.List;

public class IRProgram extends IRNode {
    public List<IRStatement> stmtList;

    public IRProgram() {
        stmtList = new ArrayList<>();
    }

    public void push(IRStatement stmt) {
        stmtList.add(stmt);
    }
}
