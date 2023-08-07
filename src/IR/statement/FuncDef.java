package src.IR.statement;

import src.AST.expression.ParallelExp;
import src.IR.instruction.Instruction;
import src.Util.position.Position;
import src.Util.type.Type;

import java.util.ArrayList;
import java.util.List;

public class FuncDef extends IRStatement {
    public Type type;
    public String functionName;
    public List<Type> parameterTypeList;
    public List<Instruction> irList;

    public FuncDef() {
        irList = new ArrayList<>();
        parameterTypeList = new ArrayList<>();
    }

    public void pushPara(Type parameterType) {
        parameterTypeList.add(parameterType);
    }

    public void push(Instruction instruction) {
        irList.add(instruction);
    }
}
