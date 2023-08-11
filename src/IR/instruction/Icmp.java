package src.IR.instruction;

import src.Util.type.IRType;
import src.Util.type.Type;

public class Icmp extends Instruction {
    public String cond;
    public IRType irType;
    public String operandLeft, operandRight, output;
    public long valueLeft, valueRight;
    private boolean onRight = true;

    public Icmp(String cond_, Type type_) {
        switch (cond_) {
            case "<" -> cond = "slt";
            case ">" -> cond = "sgt";
            case "<=" -> cond = "sle";
            case ">=" -> cond = "sge";
            case "==" -> cond = "eq";
            case "!=" -> cond = "ne";
        }
        irType = new IRType(type_);
    }

    public Icmp(String cond_, IRType irType_) {
        switch (cond_) {
            case "<" -> cond = "slt";
            case ">" -> cond = "sgt";
            case "<=" -> cond = "sle";
            case ">=" -> cond = "sge";
            case "==" -> cond = "eq";
            case "!=" -> cond = "ne";
        }
        irType = irType_;
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
