package src.AST.expression;

import src.AST.ASTVisitor;

public class BinaryExp extends Expression {
    public Expression lhs, rhs;
    public String op;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
