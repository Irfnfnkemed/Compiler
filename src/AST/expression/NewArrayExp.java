package src.AST.expression;

import src.AST.ASTVisitor;
import src.Util.type.Type;

import java.util.ArrayList;
import java.util.List;

public class NewArrayExp extends Expression {
    public Type type;
    public List<Expression> expressionList;
    public int dim;

    public NewArrayExp() {
        expressionList = new ArrayList<>();
    }
}
