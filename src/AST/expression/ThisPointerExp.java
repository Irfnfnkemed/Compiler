package src.AST.expression;

import src.AST.ASTVisitor;
import src.AST.type.Type;

public class ThisPointerExp extends Expression {
    public Type type;

    public ThisPointerExp() {
        type = new Type(false);
    }

    public void accept(ASTVisitor visitor) {

    }

}
