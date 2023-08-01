package src.AST;

import src.AST.definition.ClassDef;
import src.AST.definition.FunctionDef;
import src.AST.definition.MainDef;
import src.AST.definition.variableDef.VariableDef;

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
