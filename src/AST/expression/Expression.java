package src.AST.expression;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.type.Type;

public class Expression extends ASTNode {
    public Type type;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
