package src.AST.expression;

import src.AST.ASTVisitor;
import src.AST.type.Type;

public class NewClassExp extends Expression {
    public Type type;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
