package src.AST.definition.variableDef;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.type.Type;
import src.AST.expression.Expression;
import src.Util.Position;

public class InitVariable extends ASTNode {
    public Position position;
    public Type type;
    public String variableName;
    public Expression exp;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
