package src.IR;

import src.IR.instruction.*;
import src.IR.instruction.Binary;
import src.IR.statement.*;
import src.Util.type.IRType;
import src.Util.type.Type;

import java.util.Objects;

public class IRPrinter {
    public IRProgram irProgram;

    public IRPrinter(IRProgram irProgram_) {
        irProgram = irProgram_;
    }

    public void print() {
        printOut("declare void @print(ptr)\n",
                "declare void @println(ptr)\n",
                "declare void @printInt(i32)\n",
                "declare void @printlnInt(i32)\n",
                "declare ptr @getString()\n",
                "declare i32 @getInt()\n",
                "declare ptr @toString(i32)\n",
                "declare i32 @string.length(ptr)\n",
                "declare ptr @string.substring(ptr,i32,i32)\n",
                "declare i32 @string.parseInt(ptr)\n",
                "declare i32 @string.ord(ptr,i32)\n",
                "declare ptr @string.add(ptr,ptr)\n",
                "declare i1 @string.equal(ptr,ptr)\n",
                "declare i1 @string.notEqual(ptr,ptr)\n",
                "declare i1 @string.less(ptr,ptr)\n",
                "declare i1 @string.lessOrEqual(ptr,ptr)\n",
                "declare i1 @string.greater(ptr,ptr)\n",
                "declare i1 @greaterOrEqual(ptr,ptr)\n",
                "declare i32 @array.size(ptr)\n",
                "declare ptr @.malloc(i32)\n\n");
        irProgram.stmtList.forEach(this::print);
    }

    private void print(IRStatement irStatement) {
        if (irStatement instanceof GlobalVarDef) {
            print((GlobalVarDef) irStatement);
        } else if (irStatement instanceof FuncDef) {
            print((FuncDef) irStatement);
        } else if (irStatement instanceof ConstString) {
            print((ConstString) irStatement);
            System.out.print('\n');
        } else if (irStatement instanceof ClassTypeDef) {
            print((ClassTypeDef) irStatement);
        }
    }

    private void print(Instruction instruction) {
        if (instruction instanceof Label) {
            print((Label) instruction);
        } else {
            System.out.print("    ");
            if (instruction instanceof Alloca) {
                print((Alloca) instruction);
            } else if (instruction instanceof Store) {
                print((Store) instruction);
            } else if (instruction instanceof Load) {
                print((Load) instruction);
            } else if (instruction instanceof Ret) {
                print((Ret) instruction);
            } else if (instruction instanceof Binary) {
                print((Binary) instruction);
            } else if (instruction instanceof Call) {
                print((Call) instruction);
            } else if (instruction instanceof Br) {
                print((Br) instruction);
            } else if (instruction instanceof Getelementptr) {
                print((Getelementptr) instruction);
            } else if (instruction instanceof Icmp) {
                print((Icmp) instruction);
            } else if (instruction instanceof Phi) {
                print((Phi) instruction);
            }
        }
    }

    public void print(ConstString constString) {
        String tmp;
        for (int i = 0; i < constString.constStringList.size(); ++i) {
            tmp = constString.constStringList.get(i);
            System.out.print("@constString-");
            System.out.print(i);
            System.out.print(" = private unnamed_addr constant [");
            char[] chatTmp = tmp.toCharArray();
            int len = chatTmp.length - 1;
            for (int j = 1; j < chatTmp.length - 1; ++j) {
                char ch = chatTmp[j];
                if (ch == '\\') {
                    ++j;
                    --len;
                }
            }
            System.out.print(len);
            System.out.print(" x i8] c\"");
            for (int j = 1; j < chatTmp.length - 1; ++j) {
                char ch = chatTmp[j];
                if (ch == '\\') {
                    ch = chatTmp[++j];
                    if (ch == 'n') {
                        System.out.print("\\0A");
                    } else if (ch == '\\') {
                        System.out.print("\\\\");
                    } else if (ch == '\"') {
                        System.out.print("\\22");
                    }
                } else {
                    System.out.print(ch);
                }
            }
            System.out.print("\\00\"\n");
        }
    }

    public void print(GlobalVarDef globalVarDef) {
        printOut(globalVarDef.varName, " = global ");
        printTypeAndValue(globalVarDef.irType, globalVarDef.value);
        System.out.print('\n');
    }

    public void print(Alloca alloca) {
        printOut(alloca.varName, " = alloca ");
        printType(alloca.irType);
        System.out.print('\n');
    }

    public void print(Store store) {
        System.out.print("store ");
        if (store.valueVar == null) {
            printTypeAndValue(store.irType, store.value);
        } else {
            printType(store.irType);
            printOut(" ", store.valueVar);
        }
        printOut(", ptr ", store.toPointer, "\n");
    }

    public void print(Load load) {
        printOut(load.toVarName, " = load ");
        printType(load.irType);
        printOut(", ptr ", load.fromPointer, "\n");
    }

    public void print(Ret ret) {
        printOut("ret ");
        if (ret.irType == null) {
            printOut("void\n");
        } else {
            printType(ret.irType);
            printOut(" ", ret.var, "\n");
        }
    }

    public void print(Binary binary) {
        printOut(binary.output, " = ", binary.op, " i32 ");
        if (binary.operandLeft == null) {
            System.out.print(binary.valueLeft);
        } else {
            System.out.print(binary.operandLeft);
        }
        System.out.print(", ");
        if (binary.operandRight == null) {
            System.out.print(binary.valueRight);
        } else {
            System.out.print(binary.operandRight);
        }
        System.out.print('\n');
    }

    public void print(Call call) {
        if (call.irType == null || Objects.equals(call.irType.unitName, "void")) {
            System.out.print("call void");
        } else {
            printOut(call.resultVar, " = call ");
            printType(call.irType);
        }
        printOut(" ", call.functionName, "(");
        int tmpVar = 0;
        int tmpConst = 0;
        IRType typeTmp;
        for (int i = 0; i < call.callTypeList.size(); ++i) {
            typeTmp = call.callTypeList.get(i);
            if (call.callCateList.get(i) == Call.callCate.VAR) {
                printType(typeTmp);
                printOut(" ", call.varNameList.get(tmpVar++));
            } else if (call.callCateList.get(i) == Call.callCate.CONST) {
                printTypeAndValue(typeTmp, call.constValueList.get(tmpConst++));
            }
            if (i != call.callTypeList.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.print(")\n");
    }

    public void print(Label label) {
        System.out.print(label.labelName);
        System.out.print(":\n");
    }

    public void print(Br br) {
        if (br.condition == null) {
            printOut("br label ", br.trueLabel, "\n");
        } else {
            printOut("br i1 ", br.condition, ", label ", br.trueLabel, ", label ", br.falseLabel, "\n");
        }
    }

    public void print(Getelementptr getelementptr) {
        printOut(getelementptr.result, " = getelementptr ");
        printType(getelementptr.irType);
        printOut(", ptr ", getelementptr.from);
        if (getelementptr.offset != -1) {
            printOut(", i32 " + getelementptr.offset);
        }
        if (getelementptr.indexVar == null) {
            printOut(", i32 " + getelementptr.indexValue, "\n");
        } else {
            printOut(", i32 ", getelementptr.indexVar, "\n");
        }

    }

    public void print(Icmp icmp) {
        printOut(icmp.output, " = icmp ", icmp.cond, " ");
        printType(icmp.irType);
        System.out.print(" ");
        if (icmp.operandLeft == null) {
            printValue(icmp.irType, icmp.valueLeft);
        } else {
            System.out.print(icmp.operandLeft);
        }
        System.out.print(", ");
        if (icmp.operandRight == null) {
            printValue(icmp.irType, icmp.valueRight);
        } else {
            System.out.print(icmp.operandRight);
        }
        System.out.print('\n');
    }

    public void print(Phi phi) {
        printOut(phi.result, " = phi ");
        printType(phi.irType);
        System.out.print(" ");
        for (int i = 0; i < phi.assignBlockList.size(); ++i) {
            var tmp = phi.assignBlockList.get(i);
            System.out.print("[ ");
            if (tmp.var == null) {
                printValue(phi.irType, tmp.value);
            } else {
                System.out.print(tmp.var);
            }
            printOut(", ", tmp.label, "]");
            if (i != phi.assignBlockList.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.print('\n');
    }

    public void print(FuncDef funcDef) {
        System.out.print("\ndefine ");
        printType(funcDef.irType);
        printOut(" ", funcDef.functionName, "(");
        int size = funcDef.parameterTypeList.size();
        for (int i = 0; i < size - 1; ++i) {
            printType(funcDef.parameterTypeList.get(i));
            printOut(" %" + i, ", ");
        }
        if (size > 0) {
            if (funcDef.isClassMethod) {
                System.out.print("ptr %this");
            } else {
                printType(funcDef.parameterTypeList.get(size - 1));
                System.out.print(" %" + (size - 1));
            }
        }
        System.out.print(") {\n");
        funcDef.irList.forEach(this::print);
        System.out.print("}\n");
    }

    public void print(ClassTypeDef classTypeDef) {
        printOut(classTypeDef.className, " = type { ");
        for (int i = 0; i < classTypeDef.classMemNum; ++i) {
            if (classTypeDef.isPtrList.get(i)) {
                System.out.print("ptr");
            } else {
                System.out.print("i32");
            }
            if (i != classTypeDef.classMemNum - 1) {
                System.out.print(", ");
            }
        }
        System.out.print(" }\n");
    }

    public void printType(IRType irType) {
        if (irType == null || irType.unitSize == -1) {
            System.out.print("void");
        } else if (irType.isArray) {
            System.out.print("ptr");
        } else {
            System.out.print(irType.unitName);
        }
    }


    public void printOut(String... elements) {
        for (String ele : elements) {
            System.out.print(ele);
        }
    }


    public void printTypeAndValue(IRType irType, long value) {
        if (Objects.equals(irType.unitName, "ptr") || irType.isArray) {
            System.out.print("ptr null");
        } else if (Objects.equals(irType.unitName, "i32")) {
            System.out.print("i32 ");
            System.out.print(value);
        } else if (Objects.equals(irType.unitName, "i1")) {
            System.out.print("i1 ");
            System.out.print(value == 1);
        }
    }

    public void printValue(IRType irType, long value) {
        if (Objects.equals(irType.unitName, "ptr") || irType.isArray) {
            System.out.print("null");
        } else if (Objects.equals(irType.unitName, "i32")) {
            System.out.print(value);
        } else if (Objects.equals(irType.unitName, "i1")) {
            System.out.print(value == 1);
        }
    }
}
