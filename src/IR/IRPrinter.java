package src.IR;

import src.IR.instruction.*;
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;
import src.IR.statement.IRStatement;
import src.Util.type.Type;

import java.util.List;

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
        } else if (instruction instanceof Add) {
            print((Add) instruction);
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
        /////////////////////////////
        System.out.print('\n');
    }

    public void print(Add add) {
        printOut(add.output, " = add i32 ");
        if (add.operandLeft == null) {
            System.out.print(add.valueLeft);
        } else {
            System.out.print(add.operandLeft);
        }
        System.out.print(' ');
        if (add.operandRight == null) {
            System.out.print(add.valueRight);
        } else {
            System.out.print(add.operandRight);
        }
        System.out.print('\n');
    }

    public void print(FuncDef funcDef) {
        System.out.print("define ");
        printType(funcDef.type);
        printOut(" ", funcDef.functionName, "(");
        if (funcDef.parameterNameList != null) {
            for (int i = 0; i < funcDef.parameterNameList.size(); ++i) {
                printType(funcDef.parameterTypeList.get(i));
                printOut(" ", funcDef.parameterNameList.get(i));
                if (i != funcDef.parameterNameList.size() - 1) {
                    System.out.print(", ");
                }
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
        if (type.isInt()) {
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
