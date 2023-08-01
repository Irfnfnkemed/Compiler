package src.AST.expression;

import org.antlr.v4.runtime.tree.TerminalNode;
import src.AST.ASTVisitor;

public class BoolExp extends Expression {
    public boolean value;

    public BoolExp(TerminalNode True, TerminalNode False) {
        if (True == null) {
            value = false;
        } else {
            value = true;
        }
    }

    public void accept(ASTVisitor visitor) {

    }

}
