package src.AST.expression;

import src.AST.ASTVisitor;

public class TernaryExp extends Expression {
    public Expression condition;
    public Expression trueExp, falseExp;
}
