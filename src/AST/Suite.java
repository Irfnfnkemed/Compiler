package src.AST;

import java.util.ArrayList;
import java.util.List;

public class Suite extends ASTNode {
    List<Statement> statementList;

    Suite() {
        statementList = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {

    }
}
