package src.Util.scope;

import src.Util.error.SemanticErrors;
import src.Util.position.Position;
import src.Util.type.Type;

import java.util.HashMap;

public class Scope {
    public static class Variable {
        Type type;
        boolean cover;//最多覆盖一次

        Variable(Type type_, boolean cover_) {
            type = type_;
            cover = cover_;
        }
    }

    public HashMap<String, Variable> variable;
    public boolean isFunction = false;
    public boolean isLoop = false;
    public Type returnType;//用于isFunction为true

    public Scope() {
        variable = new HashMap<>();
    }

    public Scope(Scope fatherScope) { //从父作用域构造出子作用域
        variable = new HashMap<>();
        fatherScope.variable.forEach((key, value) -> {
            variable.put(key, new Variable(new Type(value.type), false));
        });
        isFunction = fatherScope.isFunction;
        isLoop = fatherScope.isLoop;
        if (fatherScope.isFunction) {
            returnType = new Type(fatherScope.returnType);
        }
    }

    public Type getFoundation(Type.TypeEnum typeEnum) {
        return switch (typeEnum) {
            case VOID -> new Type().setVoid();
            case BOOL -> new Type().setBool();
            case INT -> new Type().setInt();
            case STRING -> new Type().setString();
            case NULL -> new Type().setNull();
            default -> null;
        };
    }

    public Type getClass(String className) {
        return new Type().setClass(className);
    }

    public Type getArray(Type typeBase, int dim) {
        return new Type().setArray(typeBase, dim);
    }

    public void setVariable(String variableName, Type variableType, Position position) {
        if (variable.containsKey(variableName) && variable.get(variableName).cover) {
            throw new SemanticErrors("Duplicate definition of variable.", position);
        } else {
            variable.put(variableName, new Variable(new Type(variableType), true));//覆盖或者添加
        }
    }

    public Type getVariable(String variableName) {
        Variable var = variable.get(variableName);
        if (var != null) {
            return var.type;
        } else {
            return null;
        }
    }
}
