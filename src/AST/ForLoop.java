package src.AST;

public class ForLoop extends LoopStatement {

    ParallelExp parallelExp;
    VariableDef variableDef;
    Expression conditionExp;
    Expression stepExp;
    Statement stmt;

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
