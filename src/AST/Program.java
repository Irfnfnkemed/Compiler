package src.AST;

import src.AST.definition.ClassDef;
import src.AST.definition.FunctionDef;
import src.AST.definition.MainDef;
import src.AST.definition.variableDef.VariableDef;

import java.util.ArrayList;
import java.util.List;

public class Program extends ASTNode {
    public MainDef mainDef;
    public List<ClassDef> classDefList;
    public List<FunctionDef> functionDefList;
    public List<VariableDef> variableDefList;

    Program() {
        classDefList = new ArrayList<>();
        functionDefList = new ArrayList<>();
        variableDefList = new ArrayList<>();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
