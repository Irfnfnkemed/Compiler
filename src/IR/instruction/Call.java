package src.IR.instruction;

import src.Util.type.IRType;
import src.Util.type.Type;

import java.util.ArrayList;
import java.util.List;

public class Call extends Instruction {
    public static class variable {
        public String varName;
        public long varValue;

        public variable(String varName_) {
            varName = varName_;
        }

        public variable(long varValue_) {
            varValue = varValue_;
        }
    }

    public IRType irType;
    public String functionName;

    public List<IRType> callTypeList;

    public List<variable> callList;
    public String resultVar;

    public Call(String functionName_) {
        functionName = functionName_;
        callTypeList = new ArrayList<>();
        callList = new ArrayList<>();
    }

    public void set(Type type, String anonymousVar) {
        callTypeList.add(new IRType(type));
        callList.add(new variable(anonymousVar));
    }

    public void set(IRType irType, String anonymousVar) {
        callTypeList.add(irType);
        callList.add(new variable(anonymousVar));
    }

    public void set(Type type, long value) {
        callTypeList.add(new IRType(type));
        callList.add(new variable(value));
    }

    public void set(IRType irType, long value) {
        callTypeList.add(irType);
        callList.add(new variable(value));
    }
}
