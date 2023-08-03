package src.Util.type;


import org.antlr.v4.runtime.tree.TerminalNode;
import src.AST.ASTVisitor;
import src.Util.position.Position;

import java.util.Objects;

import static src.Util.type.Type.TypeEnum.*;

public class Type {
    public enum TypeEnum {
        VOID, BOOL, INT, STRING, CLASS, NULL, UNKNOWN;
    }

    public Position position;
    public TypeEnum typeEnum;
    public String typeName;
    public int dim;

    public Type() { //默认
        typeEnum = UNKNOWN;
        typeName = "";
        dim = 0;
    }

    public Type(Type obj) { //拷贝
        typeEnum = obj.typeEnum;
        typeName = obj.typeName;
        dim = obj.dim;
    }

    public Type setVoid() {
        typeEnum = VOID;
        return this;
    }

    public Type setInt() {
        typeEnum = INT;
        return this;
    }

    public Type setBool() {
        typeEnum = BOOL;
        return this;
    }

    public Type setString() {
        typeEnum = STRING;
        return this;
    }

    public Type setClass(String name) {
        typeEnum = CLASS;
        typeName = name;
        return this;
    }

    public Type setNull() {
        typeEnum = NULL;
        return this;
    }

    public Type setArray(Type typeBase, int dim_) {
        typeEnum = typeBase.typeEnum;
        typeName = typeBase.typeName;
        dim = dim_;
        return this;
    }

    public String GetType() {
        if (!Objects.equals(typeName, "")) {
            return typeName;
        } else {
            switch (typeEnum) {
                case VOID:
                    return "void";
                case INT:
                    return "int";
                case BOOL:
                    return "bool";
                case STRING:
                    return "string";
                case CLASS:
                    return typeName;
                case NULL:
                    return "null";
                default:
                    return "";
            }
        }
    }

    public boolean isArray() {
        return dim == 0;
    }

    public boolean isAssign() {
        return dim > 0 || typeEnum == CLASS || typeEnum == STRING;
    }

    public void accept(ASTVisitor visitor) {

    }
}
