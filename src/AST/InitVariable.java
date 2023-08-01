package src.AST;

public class InitVariable extends ASTNode {

    Type type;
    String variableName;
    Expression exp;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
