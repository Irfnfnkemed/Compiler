package src.IR.instruction;

import src.Util.type.IRType;
import src.Util.type.Type;

import java.util.ArrayList;
import java.util.List;

public class Call extends Instruction {
    public enum callCate {
        VAR, CONST
    }

    public IRType irType;
    public String functionName;

    public List<IRType> callTypeList;

    public List<callCate> callCateList;
    public List<String> varNameList;
    public List<Long> constValueList;
    public String resultVar;

    public Call(String functionName_) {
        functionName = functionName_;
        callTypeList = new ArrayList<>();
        callCateList = new ArrayList<>();
        varNameList = new ArrayList<>();
        constValueList = new ArrayList<>();
    }

    public void set(Type type, String anonymousVar) {
        callTypeList.add(new IRType(type));
        callCateList.add(callCate.VAR);
        varNameList.add(anonymousVar);
    }

    public void set(IRType irType, String anonymousVar) {
        callTypeList.add(irType);
        callCateList.add(callCate.VAR);
        varNameList.add(anonymousVar);
    }

    public void set(Type type, long value) {
        callTypeList.add(new IRType(type));
        callCateList.add(callCate.CONST);
        constValueList.add(value);
    }

    public void set(IRType irType, long value) {
        callTypeList.add(irType);
        callCateList.add(callCate.CONST);
        constValueList.add(value);
    }
}
