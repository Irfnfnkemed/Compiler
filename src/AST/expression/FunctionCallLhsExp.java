package src.AST.expression;

import src.AST.Expression;
import src.AST.ParallelExp;

public class FunctionCallLhsExp extends Expression {
    public String functionName;
    public ParallelExp callExpList;
}
