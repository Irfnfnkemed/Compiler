package src.AST;

public class SelectStatement extends ASTNode {

    Expression judgeExp;

    Statement trueStmt;
    Statement falseStmt = null;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
