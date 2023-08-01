package src.AST.definition.variableDef;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.type.Type;
import src.Util.Position;

import java.util.ArrayList;
import java.util.List;

public class VariableDef extends ASTNode {
    public Position position;
    public Type type;
    public List<InitVariable> initVariablelist;

    public VariableDef() {
        initVariablelist = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {

    }
}
