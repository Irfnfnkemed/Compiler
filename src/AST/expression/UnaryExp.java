package src.AST.expression;

import src.AST.ASTVisitor;

public class UnaryExp extends Expression {
    public Expression exp;
    public String op;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
