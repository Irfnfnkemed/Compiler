package src.IR.instruction;

import org.stringtemplate.v4.ST;
import src.Util.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class Call extends Instruction {
    public enum callCate {
        VAR, CONST
    }

    public Type type;
    public String functionName;

    public List<Type> callTypeList;

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
        callTypeList.add(type);
        callCateList.add(callCate.VAR);
        varNameList.add(anonymousVar);
    }

    public void set(Type type, long value) {
        callTypeList.add(type);
        callCateList.add(callCate.CONST);
        constValueList.add(value);
    }
}
