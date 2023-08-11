package src.IR.instruction;

import src.IR.instruction.Instruction;

public class Binary extends Instruction {
    public String operandLeft, operandRight, output;
    public long valueLeft, valueRight;
    public String op;

    private boolean onRight = true;

    public Binary(String op_) {
        switch (op_) {
            case "+" -> op = "add";
            case "-" -> op = "sub";
            case "*" -> op = "mul";
            case "/" -> op = "sdiv";
            case "%" -> op = "srem";
            case "<<" -> op = "shl";
            case ">>" -> op = "ashr";
            case "&" -> op = "and";
            case "|" -> op = "or";
            case "^" -> op = "xor";
        }
    }

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
