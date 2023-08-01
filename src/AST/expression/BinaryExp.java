package src.AST.expression;

import src.AST.Expression;

public class BinaryExp extends Expression {
    public Expression lhs, rhs;
    public String op;
}
