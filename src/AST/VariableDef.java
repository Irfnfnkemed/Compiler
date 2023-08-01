package src.AST;

import java.util.ArrayList;
import java.util.List;

public class VariableDef extends ASTNode {
    Type type;

    List<InitVariable> initVariablelist;

    public VariableDef() {
        initVariablelist = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {

    }
}
