package src.IR.instruction;

import src.IR.IRNode;

import java.util.ArrayList;
import java.util.List;

abstract public class Instruction extends IRNode {
    public List<String> tmpVarScopeEnd;

    public void setTmpVarScopeEnd(String varName) {
        if (tmpVarScopeEnd == null) {
            tmpVarScopeEnd = new ArrayList<>();
        }
        tmpVarScopeEnd.add(varName);
    }
}
