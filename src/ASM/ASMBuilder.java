package src.ASM;

import src.ASM.instruction.*;
import src.ASM.instruction.CALL;
import src.ASM.instruction.LABEL;
import src.ASM.instruction.RET;
import src.ASM.instruction.binary.*;
import src.ASM.instruction.binaryImme.*;
import src.IR.IRProgram;
import src.IR.instruction.*;
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;
import src.Util.reg.Reg;

import java.util.Objects;

import static java.lang.Math.min;

public class ASMBuilder {
    public ASMProgram asmProgram;

    public ASMBuilder(IRProgram irProgram) {
        asmProgram = new ASMProgram();
        for (var stmt : irProgram.stmtList) {
            if (stmt instanceof GlobalVarDef) {
                asmProgram.sectionData.pushGlobal(((GlobalVarDef) stmt).varName.substring(1));
            } else if (stmt instanceof FuncDef) {
                ////////////////////////////////////////
                if (((FuncDef) stmt).functionName.charAt(1) == '.') {
                    continue;
                }
                asmProgram.sectionText.pushGlobal(((FuncDef) stmt).functionName.substring(1));
                Reg reg = new Reg(asmProgram.sectionText);
                reg.collect((FuncDef) stmt);
                //需要的栈字节数，是16的倍数（15 = 12 + 3，前者是ra,s1-s11，后者是为了向上取整）
                int stackSize = reg.setStack(((FuncDef) stmt).allocaSize, reg.tmpVarScope.max, ((FuncDef) stmt).maxCallPara);
                asmProgram.sectionText.pushInstr(new LABEL(((FuncDef) stmt).functionName.substring(1)));
                asmProgram.sectionText.pushInstr(new ADDI("sp", "sp", -stackSize));
                asmProgram.sectionText.pushInstr(new SW("ra", stackSize - 4));
                for (int i = 1; i <= 11; ++i) {
                    asmProgram.sectionText.pushInstr(new SW("s" + i, stackSize - ((i + 1) << 2)));
                }

                //处理参数
                int size = ((FuncDef) stmt).parameterTypeList.size();
                if (size > 0) {
                    if (((FuncDef) stmt).isClassMethod) {
                        reg.getTmpVar.put("%this", "a0");
                        reg.setHeap("%this");
                    } else {
                        reg.getTmpVar.put("%0", "a0");
                        if (Objects.equals(((FuncDef) stmt).parameterTypeList.get(0).unitName, "ptr")) {
                            reg.setHeap("%0");
                        }
                    }
                }
                for (int i = 1; i < min(size, 8); ++i) {
                    if (((FuncDef) stmt).isClassMethod) {
                        reg.getTmpVar.put("%" + (i - 1), "a" + i);
                        if (Objects.equals(((FuncDef) stmt).parameterTypeList.get(i).unitName, "ptr")) {
                            reg.setHeap("%" + (i - 1));
                        }
                    } else {
                        reg.getTmpVar.put("%" + i, "a" + i);
                        if (Objects.equals(((FuncDef) stmt).parameterTypeList.get(i).unitName, "ptr")) {
                            reg.setHeap("%" + i);
                        }
                    }
                }
                if (size > 8) {//栈上传递变量
                    for (int i = 8; i < size; ++i) {
                        if (((FuncDef) stmt).isClassMethod) {
                            reg.setStackVar("%" + (i - 1), (i - 8) << 2);
                            if (Objects.equals(((FuncDef) stmt).parameterTypeList.get(i).unitName, "ptr")) {
                                reg.setHeap("%" + (i - 1));
                            }
                        } else {
                            reg.setStackVar("%" + i, (i - 8) << 2);
                            if (Objects.equals(((FuncDef) stmt).parameterTypeList.get(i).unitName, "ptr")) {
                                reg.setHeap("%" + i);
                            }
                        }
                    }
                }


                for (var irInstr : ((FuncDef) stmt).irList) {
                    reg.flushReg();
                    visitInstr(asmProgram.sectionText, reg, irInstr);
                    //TODO
                    ++reg.nowId;
                }
                for (int i = 11; i >= 0; --i) {
                    asmProgram.sectionText.pushInstr(new LW("s" + i, stackSize - 4 * i - 4));
                }
                asmProgram.sectionText.pushInstr(new LW("ra", stackSize - 4));
                asmProgram.sectionText.pushInstr(new ADDI("sp", "sp", stackSize));
                asmProgram.sectionText.pushInstr(new RET());
            }
        }
    }

    void visitInstr(Section section, Reg reg, Instruction instruction) {
        if (instruction instanceof Alloca) {
            reg.setStackVar(((Alloca) instruction).varName);
        } else if (instruction instanceof Store) {
            visit(section, reg, (Store) instruction);
        } else if (instruction instanceof Load) {
            visit(section, reg, (Load) instruction);
        } else if (instruction instanceof Call) {
            visit(section, reg, (Call) instruction);
        } else if (instruction instanceof Binary) {
            visit(section, reg, (Binary) instruction);
        } else if (instruction instanceof Icmp) {
            visit(section, reg, (Icmp) instruction);
        } else if (instruction instanceof Ret) {
            visit(section, reg, (Ret) instruction);
        } else if (instruction instanceof Label) {
            visit(section, (Label) instruction);
        } else if (instruction instanceof Br) {
            visit(section, (Br) instruction);
        }
    }

    void visit(Section section, Reg reg, Store store) {
        String from, to;
        int offset;
        if (store.valueVar == null) {
            if (store.value == 0) {
                from = "zero";
            } else {
                from = reg.getTmpReg();
                section.pushInstr(new LI(from, (int) store.value));
            }
        } else {
            from = reg.getVarReg(store.valueVar);
        }
        if (reg.isHeap(store.toPointer)) {
            to = reg.getVarReg(store.valueVar);
            offset = 0;
        } else {
            to = "sp";
            offset = reg.getStackAddr(store.toPointer);
        }
        section.pushInstr(new SW(from, to, offset));
        reg.clearTmp();
    }

    void visit(Section section, Label label) {
        if (Objects.equals(label.labelName, "entry")) {
            return;
        }
        section.pushInstr(new LABEL("." + label.labelName));
    }

    void visit(Section section, Reg reg, Load load) {
        String from;
        if (reg.isInReg(load.toVarName)) {
            from = reg.getVarReg(load.toVarName);
        } else {
            from = reg.getTmpReg();
        }
        if (reg.isHeap(load.fromPointer)) {
            section.pushInstr(new LW(reg.getVarReg(load.fromPointer), from, 0));
        } else {
            section.pushInstr(new LW(from, reg.getStackAddr(load.fromPointer)));
        }
        if (!reg.isInReg(load.toVarName)) {
            section.pushInstr(new SW(from, reg.getStackAddr(load.toVarName)));
        }
        reg.clearTmp();
    }

    void visit(Section section, Reg reg, Call call) {
        int size = call.callTypeList.size();
        int tmpVar = 0, tmpConst = 0;
        for (int i = 0; i < min(size, 8); ++i) {
            if (call.callCateList.get(i) == Call.callCate.VAR) {
                reg.getVarReg(call.varNameList.get(tmpVar++), "a" + i);
            } else {
                section.pushInstr(new LI("a" + i, call.constValueList.get(tmpConst++).intValue()));
            }
            reg.clearTmp();
        }
        if (size > 8) {
            String tmp;
            for (int i = 8; i < size; ++i) {
                if (call.callCateList.get(i) == Call.callCate.VAR) {
                    tmp = reg.getVarReg(call.varNameList.get(tmpVar++));
                } else {
                    tmp = reg.getTmpReg();
                    section.pushInstr(new LI(tmp, call.constValueList.get(tmpConst++).intValue()));
                }
                section.pushInstr(new SW(tmp, 4 * i));
            }
            reg.clearTmp();
        }
        section.pushInstr(new CALL(call.functionName.substring(1)));
        if (call.resultVar != null) {
            if (reg.isInReg(call.resultVar)) {
                section.pushInstr(new MV("a0", reg.getVarReg(call.resultVar)));
            } else {
                section.pushInstr(new SW("a0", reg.getStackAddr(call.resultVar)));
            }
            if (Objects.equals(call.irType.unitName, "ptr")) {
                reg.setHeap(call.resultVar);
            }
        }
        reg.clearTmp();
    }

    void visit(Section section, Reg reg, Binary binary) {
        String to;
        if (reg.isInReg(binary.output)) {
            to = reg.getVarReg(binary.output);
        } else {
            to = reg.getTmpReg();
        }
        switch (binary.op) {
            case "add" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new ADD(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    if (binary.operandLeft == null) {
                        section.pushInstr(new ADDI(to, reg.getVarReg(binary.operandRight),
                                (int) binary.valueLeft));
                    } else {
                        section.pushInstr(new ADDI(to, reg.getVarReg(binary.operandLeft),
                                (int) binary.valueRight));
                    }
                }
            }
            case "sub" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new SUB(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    if (binary.operandLeft == null) {
                        String tmp = reg.getTmpReg();
                        section.pushInstr(new LI(tmp, -(int) binary.valueLeft));
                        section.pushInstr(new SUB(tmp, reg.getVarReg(binary.operandRight), to));
                    } else {
                        section.pushInstr(new ADDI(to, reg.getVarReg(binary.operandLeft),
                                -(int) binary.valueRight));
                    }
                }
            }
            case "mul" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new MUL(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    String tmp = reg.getTmpReg();
                    if (binary.operandLeft == null) {
                        section.pushInstr(new LI(tmp, (int) binary.valueLeft));
                        section.pushInstr(new MUL(tmp, reg.getVarReg(binary.operandRight), to));
                    } else {
                        section.pushInstr(new LI(tmp, (int) binary.valueRight));
                        section.pushInstr(new MUL(reg.getVarReg(binary.operandLeft), tmp, to));
                    }
                }
            }
            case "sdiv" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new DIV(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    String tmp = reg.getTmpReg();
                    if (binary.operandLeft == null) {
                        section.pushInstr(new LI(tmp, (int) binary.valueLeft));
                        section.pushInstr(new DIV(tmp, reg.getVarReg(binary.operandRight), to));
                    } else {
                        section.pushInstr(new LI(tmp, (int) binary.valueRight));
                        section.pushInstr(new DIV(reg.getVarReg(binary.operandLeft), tmp, to));
                    }
                }
            }
            case "srem" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new REM(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    String tmp = reg.getTmpReg();
                    if (binary.operandLeft == null) {
                        section.pushInstr(new LI(tmp, (int) binary.valueLeft));
                        section.pushInstr(new REM(tmp, reg.getVarReg(binary.operandRight), to));
                    } else {
                        section.pushInstr(new LI(tmp, (int) binary.valueRight));
                        section.pushInstr(new REM(reg.getVarReg(binary.operandLeft), tmp, to));
                    }
                }
            }
            case "shl" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new SLL(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    if (binary.operandLeft == null) {
                        String tmp = reg.getTmpReg();
                        section.pushInstr(new LI(tmp, (int) binary.valueLeft));
                        section.pushInstr(new SLL(tmp, reg.getVarReg(binary.operandRight), to));
                    } else {
                        section.pushInstr(new SLLI(to, reg.getVarReg(binary.operandLeft),
                                (int) binary.valueRight));
                    }
                }
            }
            case "ashr" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new SRA(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    if (binary.operandLeft == null) {
                        String tmp = reg.getTmpReg();
                        section.pushInstr(new LI(tmp, (int) binary.valueLeft));
                        section.pushInstr(new SRA(tmp, reg.getVarReg(binary.operandRight), to));
                    } else {
                        section.pushInstr(new SRAI(to, reg.getVarReg(binary.operandLeft),
                                (int) binary.valueRight));
                    }
                }
            }
            case "and" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new AND(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    if (binary.operandLeft == null) {
                        section.pushInstr(new ANDI(to, reg.getVarReg(binary.operandRight),
                                (int) binary.valueLeft));
                    } else {
                        section.pushInstr(new ANDI(to, reg.getVarReg(binary.operandLeft),
                                (int) binary.valueRight));
                    }
                }
            }
            case "or" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new OR(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    if (binary.operandLeft == null) {
                        section.pushInstr(new ORI(to, reg.getVarReg(binary.operandRight),
                                (int) binary.valueLeft));
                    } else {
                        section.pushInstr(new ORI(to, reg.getVarReg(binary.operandLeft),
                                (int) binary.valueRight));
                    }
                }
            }
            case "xor" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new XOR(reg.getVarReg(binary.operandLeft),
                            reg.getVarReg(binary.operandRight), to));
                } else {
                    if (binary.operandLeft == null) {
                        section.pushInstr(new XORI(to, reg.getVarReg(binary.operandRight),
                                (int) binary.valueLeft));
                    } else {
                        section.pushInstr(new XORI(to, reg.getVarReg(binary.operandLeft),
                                (int) binary.valueRight));
                    }
                }
            }
        }
        if (!reg.isInReg(binary.output)) {
            section.pushInstr(new SW(to, reg.getStackAddr(binary.output)));
        }
        reg.clearTmp();
    }

    void visit(Section section, Reg reg, Icmp icmp) {
        String to;
        if (reg.isInReg(icmp.output)) {
            to = reg.getVarReg(icmp.output);
        } else {
            to = reg.getTmpReg();
        }
        switch (icmp.cond) {
            case "slt" -> {
                if (icmp.operandLeft != null && icmp.operandRight != null) {
                    section.pushInstr(new SLT(reg.getVarReg(icmp.operandLeft),
                            reg.getVarReg(icmp.operandRight), to));
                } else {
                    if (icmp.operandLeft == null) {
                        section.pushInstr(new SGTI(to, reg.getVarReg(icmp.operandRight),
                                (int) icmp.valueLeft));
                    } else {
                        section.pushInstr(new SLTI(to, reg.getVarReg(icmp.operandLeft),
                                (int) icmp.valueRight));
                    }
                }
            }
            case "sgt" -> {
                if (icmp.operandLeft != null && icmp.operandRight != null) {
                    section.pushInstr(new SGT(reg.getVarReg(icmp.operandLeft),
                            reg.getVarReg(icmp.operandRight), to));
                } else {
                    if (icmp.operandLeft == null) {
                        section.pushInstr(new SLTI(to, reg.getVarReg(icmp.operandRight),
                                (int) icmp.valueLeft));
                    } else {
                        section.pushInstr(new SGTI(to, reg.getVarReg(icmp.operandLeft),
                                (int) icmp.valueRight));
                    }
                }
            }
            case "sle" -> {
                if (icmp.operandLeft != null && icmp.operandRight != null) {
                    section.pushInstr(new SGT(reg.getVarReg(icmp.operandRight),
                            reg.getVarReg(icmp.operandLeft), to));
                    section.pushInstr(new XORI(to, to, 1));
                } else {
                    if (icmp.operandLeft == null) {
                        section.pushInstr(new SLTI(to, reg.getVarReg(icmp.operandRight),
                                (int) icmp.valueLeft));
                        section.pushInstr(new XORI(to, to, 1));
                    } else {
                        section.pushInstr(new SGTI(to, reg.getVarReg(icmp.operandLeft),
                                (int) icmp.valueRight));
                        section.pushInstr(new XORI(to, to, 1));
                    }
                }
            }
            case "sge" -> {
                if (icmp.operandLeft != null && icmp.operandRight != null) {
                    section.pushInstr(new SLT(reg.getVarReg(icmp.operandLeft),
                            reg.getVarReg(icmp.operandRight), to));
                } else {
                    if (icmp.operandLeft == null) {
                        section.pushInstr(new SGTI(to, reg.getVarReg(icmp.operandRight),
                                (int) icmp.valueLeft));
                        section.pushInstr(new XORI(to, to, 1));
                    } else {
                        section.pushInstr(new SLTI(to, reg.getVarReg(icmp.operandLeft),
                                (int) icmp.valueRight));
                        section.pushInstr(new XORI(to, to, 1));
                    }
                }
            }
            case "eq" -> {
                if (icmp.operandLeft != null && icmp.operandRight != null) {
                    section.pushInstr(new XOR(reg.getVarReg(icmp.operandLeft),
                            reg.getVarReg(icmp.operandRight), to));
                    section.pushInstr(new SEQZ(to, to));
                } else {
                    if (icmp.operandLeft == null) {
                        section.pushInstr(new XORI(to, reg.getVarReg(icmp.operandRight),
                                (int) icmp.valueLeft));
                        section.pushInstr(new SEQZ(to, to));
                    } else {
                        section.pushInstr(new XORI(to, reg.getVarReg(icmp.operandLeft),
                                (int) icmp.valueRight));
                        section.pushInstr(new SEQZ(to, to));
                    }
                }
            }
            case "ne" -> {
                if (icmp.operandLeft != null && icmp.operandRight != null) {
                    section.pushInstr(new XOR(reg.getVarReg(icmp.operandLeft),
                            reg.getVarReg(icmp.operandRight), to));
                    section.pushInstr(new SNEZ(to, to));
                } else {
                    if (icmp.operandLeft == null) {
                        section.pushInstr(new XORI(to, reg.getVarReg(icmp.operandRight),
                                (int) icmp.valueLeft));
                        section.pushInstr(new SEQZ(to, to));
                    } else {
                        section.pushInstr(new XORI(to, reg.getVarReg(icmp.operandLeft),
                                (int) icmp.valueRight));
                        section.pushInstr(new SNEZ(to, to));
                    }
                }
            }
        }
        if (!reg.isInReg(icmp.output)) {
            section.pushInstr(new SW(to, reg.getStackAddr(icmp.output)));
        }
        reg.clearTmp();
    }

    void visit(Section section, Reg reg, Ret ret) {
        String from = reg.getVarReg(ret.var);
        section.pushInstr(new MV(from, "a0"));
        reg.clearTmp();
    }

    void visit(Section section, Br br) {
        if (br.condition == null) {
            section.pushInstr(new J("." + br.trueLabel.substring(1)));
        }
    }

}
