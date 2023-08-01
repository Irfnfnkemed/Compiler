package src.AST.expression;

import src.AST.ASTVisitor;

public class ClassMemFunctionLhsExp extends Expression {
    public VariableLhsExp classVariable;
    public String memberFuncName;
    public ParallelExp callList;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
