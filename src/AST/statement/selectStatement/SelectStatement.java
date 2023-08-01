package src.AST.statement.selectStatement;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.statement.Statement;
import src.AST.expression.Expression;
import src.Util.Position;

public class SelectStatement extends ASTNode {
    public Position position;
    public Expression judgeExp;
    public Statement trueStmt;
    public Statement falseStmt = null;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
