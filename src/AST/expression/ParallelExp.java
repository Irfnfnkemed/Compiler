package src.AST.expression;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class ParallelExp extends Expression {
    public List<Expression> expList;

    public ParallelExp() {
        expList = new ArrayList<>();
    }
}
