package src.AST.definition;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.statement.Suite;
import src.Util.Position;

public class MainDef extends ASTNode {
    public Position position;
    public Suite suite;

    public void accept(ASTVisitor visitor) {

    }
}
