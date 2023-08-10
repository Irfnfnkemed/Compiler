package src.IR.statement;

import src.AST.expression.ParallelExp;
import src.IR.instruction.Alloca;
import src.IR.instruction.Instruction;
import src.Util.position.Position;
import src.Util.type.IRType;
import src.Util.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FuncDef extends IRStatement {
    public static class ifStatus {
        public boolean trueJump = false;
        public boolean trueNotReturn = true;
        public boolean falseJump = false;
        public boolean falseNotReturn = true;
        public boolean onTrue = true;
    }

    public static class loopStatus {
        public boolean jump = false;
    }

    public IRType irType;
    public String functionName;
    public List<IRType> parameterTypeList;
    public List<Instruction> irList;

    public Stack<ifStatus> ifStatusStack;
    public Stack<loopStatus> loopStatusStack;

    public Stack<Boolean> ifAndLoopOrder;//true为if，反之为loop

    public boolean notReturn = true;
    public int initInsertIndex = 0;
    public int allocaIndex = 1;


    public FuncDef() {
        irList = new ArrayList<>();
        parameterTypeList = new ArrayList<>();
        ifStatusStack = new Stack<>();
        loopStatusStack = new Stack<>();
        ifAndLoopOrder = new Stack<>();
    }

    public void pushPara(Type parameterType) {
        parameterTypeList.add(new IRType(parameterType));
    }

    public void push(Instruction instruction) {
        if (instruction instanceof Alloca) {
            irList.add(allocaIndex++, instruction);
        } else {
            irList.add(instruction);
        }
    }

    public void pushIf() {
        ifStatusStack.push(new ifStatus());
        ifAndLoopOrder.push(true);
    }

    public void pushLoop() {
        loopStatusStack.push(new loopStatus());
        ifAndLoopOrder.push(false);
    }

    public ifStatus getIf() {
        if (ifStatusStack.size() != 0) {
            return ifStatusStack.peek();
        }
        return null;
    }

    public loopStatus getLoop() {
        if (loopStatusStack.size() != 0) {
            return loopStatusStack.peek();
        }
        return null;
    }

    public void popIf() {
        ifStatusStack.pop();
        ifAndLoopOrder.pop();
    }

    public void popLoop() {
        loopStatusStack.pop();
        ifAndLoopOrder.pop();
    }

    public boolean isIf() {
        return ifAndLoopOrder.peek();
    }

}
