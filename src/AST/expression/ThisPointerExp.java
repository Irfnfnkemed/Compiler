package src.AST.expression;

import src.AST.ASTVisitor;
import src.Util.type.Type;

public class ThisPointerExp extends Expression {
    public Type type;

    public ThisPointerExp() {
        type = new Type();
    }
}
