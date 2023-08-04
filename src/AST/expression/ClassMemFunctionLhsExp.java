package src.AST.expression;

import src.AST.ASTVisitor;

public class ClassMemFunctionLhsExp extends Expression {
    public Expression classVariable;
    public String memberFuncName;
    public ParallelExp callList;
}
