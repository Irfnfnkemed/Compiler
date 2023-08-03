package src.AST.expression;

import src.Util.type.Type;

public class VariableLhsExp extends Expression {
    public String variableName;

    public VariableLhsExp(String variableName_) {
        variableName = variableName_;
    }
}
