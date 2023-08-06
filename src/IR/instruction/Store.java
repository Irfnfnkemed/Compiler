package src.IR.instruction;

import src.IR.IRNode;
import src.Util.type.Type;

public class Store extends Instruction {
    public Type type;

    public long value;
    public String valueVar;//匿名变量
    public String toPointer;

    public Store(Type type_, long value_, String toPointer_) {//常量存到局部变量
        type = type_;
        value = value_;
        toPointer = toPointer_;
    }

    public Store(Type type_, String valueVar_, String toPointer_) {//匿名变量值存到局部变量
        type = type_;
        valueVar = valueVar_;
        toPointer = toPointer_;
    }
}
