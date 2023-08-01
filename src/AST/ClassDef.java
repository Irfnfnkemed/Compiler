package src.AST;

import java.util.ArrayList;
import java.util.List;

public class ClassDef extends ASTNode {
    String className;
    List<VariableDef> variableDefList;
    List<FunctionDef> functionDefList;

    public ClassDef() {
        variableDefList = new ArrayList<>();
        functionDefList = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {

    }
}
