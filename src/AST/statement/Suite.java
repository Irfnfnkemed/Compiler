package src.AST.statement;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.statement.Statement;
import src.Util.Position;

import java.util.ArrayList;
import java.util.List;

public class Suite extends ASTNode {
    public Position position;
    public List<Statement> statementList;

    public Suite() {
        statementList = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {

    }
}
