package src.AST.statement.loopStatement;

import src.AST.ASTVisitor;
import src.AST.expression.Expression;
import src.AST.statement.Statement;

public class WhileLoop extends LoopStatement {
    public Expression judgeExp;
    public Statement stmt;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
