package src.AST.expression;

import src.AST.ASTVisitor;

public class PrimaryExp extends Expression {
    public Expression exp;

    public PrimaryExp() {
        isAssign = exp.isAssign;
    }
}
