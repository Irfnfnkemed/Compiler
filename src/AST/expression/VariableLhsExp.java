package src.AST.expression;

import src.AST.ASTVisitor;
import src.Util.type.Type;

public class VariableLhsExp extends Expression {
    public Type type;
    public String typeName;

    public VariableLhsExp(String typeName_) {
        type = new Type();
        typeName = typeName_;
    }
}
