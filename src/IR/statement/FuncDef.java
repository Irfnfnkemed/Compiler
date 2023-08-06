package src.IR.statement;

import src.IR.instruction.Instruction;
import src.Util.type.Type;

import java.util.ArrayList;
import java.util.List;

public class FuncDef extends IRStatement {
    public Type type;
    public String functionName;
    public List<Type> parameterTypeList;
    public List<String> parameterNameList;
    public List<Instruction> irList;

    public FuncDef() {
        irList = new ArrayList<>();
    }

    public void push(Type parameterType, String parameterName) {
        parameterTypeList.add(parameterType);
        parameterNameList.add(parameterName + "-" + type.position.line + "-" + type.position.column);
    }

    public void push(Instruction instruction) {
        irList.add(instruction);
    }
}
