package src.AST;


import org.antlr.v4.runtime.atn.SemanticContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import static src.AST.Type.TypeEnum.*;

public class Type extends ASTNode {
    enum TypeEnum {
        VOID, BOOL, INT, STRING, CLASS, NULL, UNKNOWN;
    }

    TypeEnum typeEnum;
    String typeName;
    int dim;

    public Type(TerminalNode Void, TerminalNode Bool, TerminalNode Int, TerminalNode String) { //基础类型
        if (Void != null) {
            typeEnum = VOID;
        } else if (Bool != null) {
            typeEnum = BOOL;
        } else if (Int != null) {
            typeEnum = INT;
        } else if (String != null) {
            typeEnum = STRING;
        }
        typeName = "";
        dim = 0;
    }

    public Type(String typeName_) { //类类型
        typeEnum = CLASS;
        typeName = typeName_;
        dim = 0;
    }

    public Type(Type typeBase, int dim_) { //数组类型
        typeEnum = typeBase.typeEnum;
        typeName = typeBase.typeName;
        dim = dim_;
    }

    public Type(boolean empty) { //默认与空指针
        if (empty) {
            typeEnum = NULL;
        } else {
            typeEnum = UNKNOWN;
        }
        typeName = "";
        dim = 0;
    }

    public void accept(ASTVisitor visitor) {

    }
}
