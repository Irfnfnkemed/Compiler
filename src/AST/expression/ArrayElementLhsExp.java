package src.AST.expression;

import src.AST.ASTVisitor;

public class ArrayElementLhsExp extends Expression {
    public Expression variable;
    public Expression index;
}
