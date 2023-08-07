package src.IR;

import src.IR.instruction.*;
import src.IR.instruction.Binary;
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;
import src.IR.statement.IRStatement;
import src.Util.type.Type;

public class IRPrinter {
    public IRProgram irProgram;

    public IRPrinter(IRProgram irProgram_) {
        irProgram = irProgram_;
    }

    public void print() {
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
        printType(alloca.type);
        System.out.print('\n');
    }

    public void print(Store store) {
        System.out.print("store ");
        printType(store.type);
        System.out.print(' ');
        if (store.valueVar == null) {
            if (store.type.isInt()) {
                System.out.print(store.value);
            } else if (store.type.isBool()) {
                System.out.print(store.value == 0);
            }
        } else {
            System.out.print(store.valueVar);
        }
        printOut(", ptr ", store.toPointer, "\n");
    }

    public void print(Load load) {
        printOut(load.toVarName, " = load ");
        printType(load.type);
        printOut(", ptr ", load.fromPointer, "\n");
    }

    public void print(Ret ret) {
        printOut("ret ");
        printType(ret.type);
        System.out.print(' ');
        if (ret.type != null && !ret.type.isVoid()) {
            if (ret.retVar == null) {
                if (ret.type.isInt()) {
                    System.out.print(ret.retValue);
                } else if (ret.type.isBool()) {
                    System.out.print(ret.retValue == 1);
                }
            } else {
                System.out.print(ret.retVar);
            }
        }
        System.out.print('\n');
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
        if (call.type == null || call.type.isVoid()) {
            System.out.print("call void");
        } else {
            printOut(call.resultVar, " = call ");
            printType(call.type);
        }
        printOut(" ", call.functionName, "(");
        int tmpVar = call.varNameList.size() - 1;
        int tmpConst = call.constValueList.size() - 1;
        Type typeTmp;
        for (int i = 0; i < call.callTypeList.size(); ++i) {
            typeTmp = call.callTypeList.get(i);
            printType(typeTmp);
            if (call.callCateList.get(i) == Call.callCate.VAR) {
                printOut(" ", call.varNameList.get(tmpVar--));
            } else if (call.callCateList.get(i) == Call.callCate.CONST) {
                System.out.print(" ");
                if (typeTmp.isInt()) {
                    System.out.print(call.constValueList.get(tmpConst--));
                } else if (typeTmp.isBool()) {
                    System.out.print(call.constValueList.get(tmpConst--) == 1);
                }
            }
            if (i != call.callTypeList.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.print(")\n");
    }

    public void print(FuncDef funcDef) {
        System.out.print("\ndefine ");
        printType(funcDef.type);
        printOut(" ", funcDef.functionName, "(");
        for (int i = 0; i < funcDef.parameterTypeList.size(); ++i) {
            printType(funcDef.parameterTypeList.get(i));
            printOut(" %" + i);
            if (i != funcDef.parameterTypeList.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.print(") {\nentry:\n");
        funcDef.irList.forEach(instruction -> {
            System.out.print("    ");
            print(instruction);
        });
        System.out.print("}\n");
    }

    public void printType(Type type) {
        if (type == null) {
            System.out.print("void");
        } else if (type.isInt()) {
            System.out.print("i32");
        } else if (type.isBool()) {
            System.out.print("i1");
        } else if (type.isVoid()) {
            System.out.print("void");
        }
    }

    public void printOut(String... elements) {
        for (String ele : elements) {
            System.out.print(ele);
        }
    }
}
