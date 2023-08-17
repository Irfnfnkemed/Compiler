package src.IR;

import src.IR.IRNode;
import src.IR.instruction.Instruction;
import src.IR.statement.FuncDef;

import java.util.Stack;

public class Exp extends IRNode {
    public enum expCate {
        VarName, ConstValue;
    }

    public Stack<expCate> expCateStack;
    public Stack<String> varNameStack;
    public Stack<Long> constValueStack;

    public FuncDef funcDef;
    public String lhsVar;//指向存放左值的空间的指针名

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

    public void set(boolean value) {
        expCateStack.push(expCate.ConstValue);
        constValueStack.push(value ? 1L : 0L);
    }

    public void set(String anonymousVar) {
        expCateStack.push(expCate.VarName);
        varNameStack.push(anonymousVar);
    }

    public boolean isOperandConst() {
        return expCateStack.peek() == expCate.ConstValue;
    }

    public boolean isOperandTwoConst() {
        boolean flag = false;
        var tmp = expCateStack.pop();
        if (tmp == expCate.ConstValue && expCateStack.peek() == expCate.ConstValue) {
            flag = true;
        }
        expCateStack.push(tmp);
        return flag;
    }

    public void pop() {
        if (isOperandConst()) {
            constValueStack.pop();
        } else {
            varNameStack.pop();
        }
        expCateStack.pop();
    }

    public String popVar() {
        expCateStack.pop();
        return varNameStack.pop();
    }

    public String getVar() {
        return varNameStack.peek();
    }

    public long getValue() {
        return constValueStack.peek();
    }

    public long popValue() {
        expCateStack.pop();
        return constValueStack.pop();
    }

    public void push(Instruction instruction) {
        funcDef.push(instruction);
    }

    public void reset() {
        expCateStack.clear();
        varNameStack.clear();
        constValueStack.clear();
    }

    public void setTmpVarEnd(String varName) {
        var tmp = funcDef.irList.get(funcDef.irList.size() - 1);
        tmp.setTmpVarScopeEnd(varName);
    }
}
