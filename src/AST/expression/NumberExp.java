package src.AST.expression;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.Expression;

public class NumberExp extends Expression {

    public int value;

    public NumberExp(String number) {
        value = Integer.parseInt(number);
    }

    public void accept(ASTVisitor visitor) {

    }
}
