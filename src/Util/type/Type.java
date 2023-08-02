package src.AST.type;


import org.antlr.v4.runtime.tree.TerminalNode;
import src.AST.ASTNode;
import src.AST.ASTVisitor;
import src.Util.Position;

import static src.AST.type.Type.TypeEnum.*;

public class Type {
    enum TypeEnum {
        VOID, BOOL, INT, STRING, CLASS, NULL, UNKNOWN;
    }

    public Position position;
    public TypeEnum typeEnum;
    public String typeName;
    public int dim;

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
