package src.AST.expression;

import src.AST.ASTVisitor;

public class TernaryExp extends Expression {
    public Expression lhs, mhs, rhs;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
