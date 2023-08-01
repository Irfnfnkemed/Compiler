package src.AST.statement.jumpStatement;

import src.AST.ASTVisitor;
import src.AST.expression.Expression;

public class ReturnStmt extends JumpStatement {

    public Expression returnExp;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
