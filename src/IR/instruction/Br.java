package src.IR.instruction;

public class Br extends Instruction {
    public String condition;
    public String trueLabel;
    public String falseLabel;

    public Br(String toLabel_) {
        trueLabel = toLabel_;
    }

    public Br(String condition_, String trueLabel_, String falseLabel_) {
        condition = condition_;
        trueLabel = trueLabel_;
        falseLabel = falseLabel_;
    }
}
