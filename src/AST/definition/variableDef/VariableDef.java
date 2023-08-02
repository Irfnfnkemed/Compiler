package src.AST.definition.variableDef;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.Util.type.Type;

import java.util.ArrayList;
import java.util.List;

public class VariableDef extends ASTNode {
    public Type type;
    public List<InitVariable> initVariablelist;

    public VariableDef() {
        initVariablelist = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
