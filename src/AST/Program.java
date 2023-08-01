package src.AST;

import java.util.ArrayList;
import java.util.List;

public class Program extends ASTNode {
    MainDef mainDef;
    List<ClassDef> classDefList;
    List<FunctionDef> functionDefList;
    List<VariableDef> variableDefList;

    Program() {
        classDefList = new ArrayList<>();
        functionDefList = new ArrayList<>();
        variableDefList = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {

    }
}
