package src.AST.expression;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.Expression;
import src.AST.Type;

public class NewClassExp extends Expression {
    public Type type;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
