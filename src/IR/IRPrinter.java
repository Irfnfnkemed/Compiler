package src.IR;

import src.IR.instruction.*;
import src.IR.instruction.Binary;
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;
import src.IR.statement.IRStatement;
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
                "declare void @printIntln(i32)\n",
                "declare ptr @getString()\n",
                "declare i32 @getInt()\n",
                "declare ptr @toString(i32)\n",
                "declare i32 @string.length(ptr)\n",
                "declare ptr @string.substring(ptr,i32,i32)\n",
                "declare i32 @string.parseInt(ptr)\n",
                "declare i32 @string.ord(ptr,i32)\n",
                "declare i1 @string.equal(ptr,ptr)\n",
                "declare i1 @string.notEqual(ptr,ptr)\n",
                "declare i1 @string.less(ptr,ptr)\n",
                "declare i1 @string.lessOrEqual(ptr,ptr)\n",
                "declare i1 @string.greater(ptr,ptr)\n",
                "declare i1 @greaterOrEqual(ptr,ptr)\n",
                "declare i32 @array.size(ptr)\n",
                "declare ptr @.malloc(i32)\n");
        irProgram.stmtList.forEach(this::print);
    }

    private void print(IRStatement irStatement) {
        if (irStatement instanceof GlobalVarDef) {
            print((GlobalVarDef) irStatement);
        } else if (irStatement instanceof FuncDef) {
            print((FuncDef) irStatement);
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
            }
        }
    }


    public void print(GlobalVarDef globalVarDef) {
        printOut(globalVarDef.varName, " = global ");
        if (globalVarDef.type.isInt()) {
            printOut("i32 ", Long.toString(globalVarDef.value));
        } else if (globalVarDef.type.isBool()) {
            System.out.print("i1 ");
            System.out.print(globalVarDef.value == 1);
        }
        System.out.print('\n');
        if (globalVarDef.funcDef != null) {
            print(globalVarDef.funcDef);
        }
    }

    public void print(Alloca alloca) {
        printOut(alloca.varName, " = alloca ");
        printType(alloca.irType);
        System.out.print('\n');
    }

    public void print(Store store) {
        System.out.print("store ");
        printType(store.irType);
        System.out.print(' ');
        if (store.valueVar == null) {
            if (Objects.equals(store.irType.unitName, "ptr") || store.irType.len != -1) {
                System.out.print("null");
            } else if (Objects.equals(store.irType.unitName, "i32")) {//
                System.out.print(store.value);
            } else if (Objects.equals(store.irType.unitName, "i1")) {
                System.out.print(store.value == 1);
            }
        } else {
            System.out.print(store.valueVar);
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
        printType(ret.irType);
        printOut(" ", ret.var, "\n");
    }

    public void print(Binary binary) {
        switch (binary.op) {
            case "+" -> printOut(binary.output, " = add i32 ");
            case "-" -> printOut(binary.output, " = sub i32 ");
            case "*" -> printOut(binary.output, " = mul i32 ");
            case "/" -> printOut(binary.output, " = sdiv i32 ");
            case "%" -> printOut(binary.output, " = srem i32 ");
        }
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
        int tmpVar = call.varNameList.size() - 1;
        int tmpConst = call.constValueList.size() - 1;
        IRType typeTmp;
        for (int i = 0; i < call.callTypeList.size(); ++i) {
            typeTmp = call.callTypeList.get(i);
            printType(typeTmp);
            if (call.callCateList.get(i) == Call.callCate.VAR) {
                printOut(" ", call.varNameList.get(tmpVar--));
            } else if (call.callCateList.get(i) == Call.callCate.CONST) {
                System.out.print(" ");
                if (Objects.equals(typeTmp.unitName, "i32")) {
                    System.out.print(call.constValueList.get(tmpConst--));
                } else if (Objects.equals(typeTmp.unitName, "i1")) {
                    System.out.print(call.constValueList.get(tmpConst--) == 1);
                }
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
        printOut(", i32 " + getelementptr.index, "\n");
    }


    public void print(FuncDef funcDef) {
        System.out.print("\ndefine ");
        printType(funcDef.irType);
        printOut(" ", funcDef.functionName, "(");
        for (int i = 0; i < funcDef.parameterTypeList.size(); ++i) {
            printType(funcDef.parameterTypeList.get(i));
            printOut(" %" + i);
            if (i != funcDef.parameterTypeList.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.print(") {\n");
        funcDef.irList.forEach(this::print);
        System.out.print("}\n");
    }

    public void printType(IRType irType) {
        if (irType.len != -1) {
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
}
