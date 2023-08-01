package src.AST;

import src.AST.definition.ClassDef;
import src.AST.definition.FunctionDef;
import src.AST.definition.MainDef;
import src.AST.definition.variableDef.VariableDef;
import src.Util.Position;

import java.util.ArrayList;
import java.util.List;

public class Program extends ASTNode {
    public Position position;
    public MainDef mainDef;
    public List<ClassDef> classDefList;
    public List<FunctionDef> functionDefList;
    public List<VariableDef> variableDefList;

    Program() {
        classDefList = new ArrayList<>();
        functionDefList = new ArrayList<>();
        variableDefList = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {

    }
}
