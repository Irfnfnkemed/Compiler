package src.IR.instruction;

public class Add extends Instruction {
    public String operandLeft, operandRight, output;
    public long valueLeft, valueRight;

    private boolean onRight = true;

    public void set(long value) {
        if (onRight) {
            valueRight = value;
            onRight = false;
        } else {
            valueLeft = value;
        }
    }

    public void set(String var) {
        if (onRight) {
            operandRight = var;
            onRight = false;
        } else {
            operandLeft = var;
        }
    }
}
