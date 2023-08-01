package src.AST.expression;

import src.AST.ASTVisitor;

public class AssignExp extends Expression {
    public Expression lhs, rhs;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
