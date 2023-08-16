package src.IR.statement;

import src.IR.instruction.*;
import src.Util.type.IRType;
import src.Util.type.Type;

import java.util.LinkedList;
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
    public String label = "%entry";
    public boolean isClassMethod = false;
    public int allocaSize = 0;
    public int maxCallPara = 0;

    public FuncDef() {
        irList = new LinkedList<>();
        parameterTypeList = new LinkedList<>();
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
            ++allocaSize;
        } else {
            irList.add(instruction);
        }
        if (instruction instanceof Label) {
            label = "%" + ((Label) instruction).labelName;
        }
        if (instruction instanceof Call) {
            if (((Call) instruction).callTypeList.size() > maxCallPara) {
                maxCallPara = ((Call) instruction).callTypeList.size();
            }
        }

    }

    public int pop() {//用于弹出对赋值号左侧不必要的指令
        Instruction tmp;
        int tail = irList.size() - 1;
        int minus = 0;//匿名变量编号需要减少的值
        while (true) {
            tmp = irList.get(tail);
            irList.remove(tail--);
            if (tmp instanceof Binary) {
                ++minus;
            } else if (tmp instanceof Load) {
                ++minus;
                break;
            }
        }
        return minus;
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
