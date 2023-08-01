package src.AST;

import org.antlr.v4.runtime.tree.TerminalNode;
import src.AST.expression.TernaryExp;

public class BoolExp extends Expression {
    public boolean value;

    BoolExp(TerminalNode True, TerminalNode False) {
        if (True == null) {
            value = false;
        } else {
            value = true;
        }
    }

    public void accept(ASTVisitor visitor) {

    }

}
