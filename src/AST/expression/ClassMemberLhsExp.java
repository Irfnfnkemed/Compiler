package src.AST.expression;

import src.AST.ASTVisitor;

public class ClassMemberLhsExp extends Expression {
    public Expression classVariable;
    public String memberName;
}
