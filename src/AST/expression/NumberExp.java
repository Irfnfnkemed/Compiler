package src.AST.expression;

import src.AST.ASTVisitor;

public class NumberExp extends Expression {
    public int value;

    public NumberExp(String number) {
        value = Integer.parseInt(number);
    }

    public void accept(ASTVisitor visitor) {

    }
}
