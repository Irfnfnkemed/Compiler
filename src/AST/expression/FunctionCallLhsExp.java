package src.AST.expression;

import src.AST.ASTVisitor;

public class FunctionCallLhsExp extends Expression {
    public String functionName;
    public ParallelExp callExpList;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
