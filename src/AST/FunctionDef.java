package src.AST;

import java.util.ArrayList;
import java.util.List;

public class FunctionDef extends ASTNode {
    Type type;
    String functionName;
    List<Type> parameterTypeList;
    List<String> parameterNameList;
    Suite body;

    public FunctionDef() {
        parameterTypeList = new ArrayList<>();
        parameterNameList = new ArrayList<>();
    }

    public void accept(ASTVisitor visitor) {

    }
}
