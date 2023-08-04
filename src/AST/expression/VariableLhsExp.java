package src.AST.expression;

public class VariableLhsExp extends Expression {
    public String variableName;

    public VariableLhsExp(String variableName_) {
        variableName = variableName_;
    }
}
