package src.AST.expression;

import src.AST.ASTVisitor;
import src.AST.type.Type;

public class VariableLhsExp extends Expression {
    public Type type;
    public String typeName;

    public VariableLhsExp(String typeName_) {
        type = new Type(false);
        typeName = typeName_;
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
