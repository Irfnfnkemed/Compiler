package src.AST.definition;

import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.AST.definition.variableDef.VariableDef;
import src.Util.Position;

import java.util.ArrayList;
import java.util.List;

public class ClassDef extends ASTNode {
    public Position position;
    public String className;
    public List<VariableDef> variableDefList;
    public List<FunctionDef> functionDefList;

    public ClassDef() {
        variableDefList = new ArrayList<>();
        functionDefList = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {

    }
}
