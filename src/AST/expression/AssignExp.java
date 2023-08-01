package src.AST.expression;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.Expression;

public class AssignExp extends Expression {
    public Expression lhs, rhs;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
