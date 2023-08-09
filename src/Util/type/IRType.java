package src.Util.type;

public class IRType {
    public int unitSize;//example:i32,unitSize=32; ptr,unitSize = 8; unitSize=-1，表void; 为0，表待定
    public String className;//自定义类
    public int len;//example:[4 x i32],len=4;len=0,表非数组类型

    public IRType(Type type) {
        if (type.typeEnum == Type.TypeEnum.BOOL) {
            unitSize = 1;
        } else if (type.typeEnum == Type.TypeEnum.VOID) {
            unitSize = -1;
        } else if (type.typeEnum == Type.TypeEnum.CLASS) {
            unitSize = 0;
            className = type.typeName;
        } else {
            unitSize = 32;
        }
    }

    public IRType(int unitSize_) {
        unitSize = unitSize_;
    }


}
