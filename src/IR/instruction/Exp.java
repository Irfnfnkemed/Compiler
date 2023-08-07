package src.IR.instruction;

import src.IR.statement.FuncDef;

import java.util.Stack;

public class Exp extends Instruction {
    public enum expCate {
        VarName, ConstValue;
    }

    public boolean isConst = true;
    public Stack<expCate> expCateStack;
    public Stack<String> varNameStack;
    public Stack<Long> constValueStack;

    public FuncDef funcDef;

    public Exp(FuncDef funcDef_) {
        funcDef = funcDef_;
        expCateStack = new Stack<>();
        constValueStack = new Stack<>();
        varNameStack = new Stack<>();
    }

    public void set(long value) {
        expCateStack.push(expCate.ConstValue);
        constValueStack.push(value);
    }

    public void set(String anonymousVar) {
        expCateStack.push(expCate.VarName);
        varNameStack.push(anonymousVar);
        isConst = false;
    }

    public boolean isOperandConst() {
        return expCateStack.peek() == expCate.ConstValue;
    }

    public String popVar() {
        expCateStack.pop();
        return varNameStack.pop();
    }

    public long popValue() {
        expCateStack.pop();
        return constValueStack.pop();
    }

    public void push(Instruction instruction) {
        funcDef.push(instruction);
    }

    public void reset() {
        isConst = true;
        expCateStack.clear();
        varNameStack.clear();
        constValueStack.clear();
    }
}
