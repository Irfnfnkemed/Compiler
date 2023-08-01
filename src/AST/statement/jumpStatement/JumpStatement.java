package src.AST.statement.jumpStatement;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.Util.Position;

public class JumpStatement extends ASTNode {
    public Position position;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
