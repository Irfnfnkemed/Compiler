package src.AST;

public class Statement extends ASTNode {
    Suite suite;
    SelectStatement selectStatement;
    LoopStatement loopStatement;
    JumpStatement jumpStatement;
    VariableDef variableDef;
    ParallelExp parallelExp;

    public void accept(ASTVisitor visitor) {

    }
}
