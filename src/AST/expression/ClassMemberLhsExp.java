package src.AST.expression;

import src.AST.Expression;
import src.AST.ParallelExp;

public class ClassMemberLhsExp extends Expression {
    public Expression classVariable;
    public String memberName;
}
