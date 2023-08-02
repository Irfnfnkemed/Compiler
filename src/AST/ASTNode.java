package src.AST;

import src.Util.position.Position;

abstract public class ASTNode {
     public Position position;
     abstract public void accept(ASTVisitor visitor);
}
