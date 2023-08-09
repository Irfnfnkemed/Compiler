package src.IR.statement;

import src.AST.expression.ParallelExp;
import src.IR.instruction.Instruction;
import src.Util.position.Position;
import src.Util.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FuncDef extends IRStatement {
    public static class ifStatus {
        public boolean trueReturn = false;
        public boolean trueBreak = false;
        public boolean trueContinue = false;
        public boolean trueJump = false;
        public boolean falseReturn = false;
        public boolean falseBreak = false;
        public boolean falseContinue = false;
        public boolean falseJump = false;
        public boolean onTrue = true;
    }

    public static class loopStatus {
        public boolean loopReturn = false;
        public boolean loopBreak = false;
        public boolean loopContinue = false;
        public boolean jump = false;
        public boolean jumpStep = true;
    }

    public Type type;
    public String functionName;
    public List<Type> parameterTypeList;
    public List<Instruction> irList;

    public Stack<ifStatus> ifStatusStack;
    public Stack<loopStatus> loopStatusStack;

    public Stack<Boolean> ifAndLoopOrder;//true为if，反之为loop


    public FuncDef() {
        irList = new ArrayList<>();
        parameterTypeList = new ArrayList<>();
        ifStatusStack = new Stack<>();
        loopStatusStack = new Stack<>();
        ifAndLoopOrder = new Stack<>();
    }

    public void pushPara(Type parameterType) {
        parameterTypeList.add(parameterType);
    }

    public void push(Instruction instruction) {
        irList.add(instruction);
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
