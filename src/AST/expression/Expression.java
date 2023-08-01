package src.AST.expression;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.type.Type;
import src.Util.Position;

public class Expression extends ASTNode {
    public Position position;
    public Type type;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
