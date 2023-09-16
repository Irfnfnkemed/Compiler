package src.ASM;

import src.ASM.instruction.*;
import src.ASM.instruction.CALL;
import src.ASM.instruction.LABEL;
import src.ASM.instruction.RET;
import src.ASM.instruction.binary.*;
import src.ASM.instruction.binaryImme.*;
import src.IR.IRProgram;
import src.IR.instruction.*;
import src.IR.statement.ConstString;
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;

import java.util.*;

import static java.lang.Math.min;

public class ASMBuilder {
    public static class FuncNode {//函数调用图
        public String funcName;
        public HashSet<FuncNode> fromNode;//被调用的函数
        public HashSet<FuncNode> toNode;//调用的函数
        public boolean restore = true;//true表示函数调用会改变t0-t7
        public List<CALL> callList;//被调用列表

        public FuncNode(String funcName_) {
            funcName = funcName_;
            fromNode = new HashSet<>();
            toNode = new HashSet<>();
            callList = new ArrayList<>();
        }
    }

    public ASMProgram asmProgram;
    public HashSet<String> globalVar;
    public HashMap<String, FuncNode> funcNodeMap;
    private HashMap<String, FuncDef.PhiInfo> phiMap;
    public Queue<String> inlineQueue;
    public HashMap<String, String> globalVarReg;//存储全局变量的虚拟寄存器
    public int cnt = 0, label = 0;

    public ASMBuilder(IRProgram irProgram) {
        asmProgram = new ASMProgram();
        globalVar = new HashSet<>();
        funcNodeMap = new HashMap<>();
        inlineQueue = new ArrayDeque<>();
        globalVarReg = new HashMap<>();
        for (var stmt : irProgram.stmtList) {
            if (stmt instanceof FuncDef) {
                FuncNode funcNode = getNode(((FuncDef) stmt).functionName.substring(1));
                funcNode.restore = false;
                ((FuncDef) stmt).collectPhi();
                phiMap = ((FuncDef) stmt).phiMap;
                asmProgram.sectionText.pushGlobal(((FuncDef) stmt).functionName.substring(1));
                asmProgram.sectionText.nowFuncName = ((FuncDef) stmt).functionName.substring(1);
                asmProgram.sectionText.pushInstr(new LABEL(((FuncDef) stmt).functionName.substring(1), true));
                Init init = new Init();
                asmProgram.sectionText.pushInstr(init);
                //处理参数
                int size = ((FuncDef) stmt).parameterTypeList.size();
                if (size > 0) {
                    MV mv;
                    if (((FuncDef) stmt).isClassMethod) {
                        mv = new MV("tmp" + cnt++, "%this");
                    } else {
                        mv = new MV("tmp" + cnt++, "%_0");
                    }
                    mv.preColoredFrom = "a0";
                    asmProgram.sectionText.pushInstr(mv);
                    init.paraList.add(mv);
                }
                for (int i = 1; i < min(size, 8); ++i) {
                    MV mv;
                    if (((FuncDef) stmt).isClassMethod) {
                        mv = new MV("tmp" + cnt++, "%_" + (i - 1));
                    } else {
                        mv = new MV("tmp" + cnt++, "%_" + i);
                    }
                    mv.preColoredFrom = "a" + i;
                    asmProgram.sectionText.pushInstr(mv);
                    init.paraList.add(mv);
                }
                if (size > 8) {//栈上传递变量
                    for (int i = 8; i < size; ++i) {
                        LW lw;
                        if (((FuncDef) stmt).isClassMethod) {
                            lw = new LW("tmp" + cnt++, "%_" + (i - 1), (i - 8) << 2);
                        } else {
                            lw = new LW("tmp" + cnt++, "%_" + i, (i - 8) << 2);
                        }
                        lw.preColoredFrom = "stackTop#";
                        asmProgram.sectionText.pushInstr(lw);
                        init.paraList.add(lw);
                    }
                }
                for (var irInstr : ((FuncDef) stmt).irList) {
                    visitInstr(asmProgram.sectionText, irInstr, funcNode, init);
                }
            } else if (stmt instanceof GlobalVarDef) {
                asmProgram.sectionData.pushGlobal(((GlobalVarDef) stmt).varName.substring(1));
                asmProgram.sectionData.pushWord(((GlobalVarDef) stmt).varName.substring(1), (int) ((GlobalVarDef) stmt).value);
                globalVar.add(((GlobalVarDef) stmt).varName.substring(1));
            } else if (stmt instanceof ConstString) {
                for (int i = 0; i < ((ConstString) stmt).constStringList.size(); ++i) {
                    asmProgram.sectionRodata.pushConstString(
                            "constString-" + i, ((ConstString) stmt).constStringList.get(i));
                    globalVar.add("constString-" + i);
                }
            }
        }
        setFuncRestore();
    }

    void visitInstr(Section section, Instruction instruction, FuncNode funcNode, Init init) {
        if (instruction instanceof Label) {
            visit(section, (Label) instruction);
        } else if (instruction instanceof Store) {
            visit(section, (Store) instruction);
        } else if (instruction instanceof Load) {
            visit(section, (Load) instruction);
        } else if (instruction instanceof Binary) {
            visit(section, (Binary) instruction);
        } else if (instruction instanceof Icmp) {
            visit(section, (Icmp) instruction);
        } else if (instruction instanceof Call) {
            visit(section, (Call) instruction, funcNode);
        } else if (instruction instanceof Br) {
            visit(section, (Br) instruction);
        } else if (instruction instanceof Getelementptr) {
            visit(section, (Getelementptr) instruction);
        } else if (instruction instanceof Ret) {
            visit(section, (Ret) instruction, init);
        }
    }

    void visit(Section section, Label label) {
        if (Objects.equals(label.labelName, "entry")) {
            return;
        }
        section.pushInstr(new LABEL(label.labelName));
    }

    void visit(Section section, Store store) {
        String from, to;
        boolean isStore = true;
        if (store.valueVar == null) {
            from = "tmp" + cnt++;
            section.pushInstr(new LI(from, (int) store.value));
        } else {
            if (store.valueVar.charAt(0) == '@') {
//                from = globalVarReg.get(store.valueVar.substring(1));
//                if (from == null) {
                from = "tmp" + cnt++;
                if (store.valueVar.contains("-")) {
                    section.pushInstr(new LA(from, store.valueVar.substring(1)));
                } else {
                    section.pushInstr(new LW(store.valueVar.substring(1), from));
                }
//                }
            } else {
                from = store.valueVar;
            }
        }
        if (store.toPointer.charAt(0) == '@') {
//            to = globalVarReg.get(store.toPointer.substring(1));
//            if (to == null) {
            to = "tmp" + cnt++;
            section.pushInstr(new LA(to, store.toPointer.substring(1)));
//            } else {
//                isStore = false;
//            }
        } else {
            to = store.toPointer;
        }
//        if (isStore) {
        section.pushInstr(new SW(from, to, 0));
//        } else {
//            section.pushInstr(new MV(from, to));
//        }
    }

    void visit(Section section, Load load) {
        if (load.fromPointer.charAt(0) == '@') {
//            String varReg = globalVarReg.get(load.fromPointer.substring(1));
//            if (varReg != null) {
//                section.pushInstr(new MV(varReg, load.toVarName));
//            } else {
            String varReg;
            varReg = "tmp" + cnt++;
            //globalVarReg.put(load.fromPointer.substring(1), varReg);
            if (load.fromPointer.contains("-")) {
                section.pushInstr(new LA(load.toVarName, varReg));
            } else {
                section.pushInstr(new LW(varReg, load.toVarName));
            }
            //}
        } else {
            section.pushInstr(new LW(load.fromPointer, load.toVarName, 0));
        }
    }

    void visit(Section section, Binary binary) {
        switch (binary.op) {
            case "add" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new ADD(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        if (binary.valueLeft == 0) {
                            section.pushInstr(new MV(binary.operandRight, binary.output));
                        } else {
                            section.pushInstr(new ADDI(binary.output, binary.operandRight, (int) binary.valueLeft));
                        }
                    } else if (binary.operandLeft != null) {
                        if (binary.valueRight == 0) {
                            section.pushInstr(new MV(binary.operandLeft, binary.output));
                        } else {
                            section.pushInstr(new ADDI(binary.output, binary.operandLeft, (int) binary.valueRight));
                        }
                    } else {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new ADDI(binary.output, "tmp" + cnt++, (int) binary.valueRight));
                    }
                }
            }
            case "sub" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new SUB(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        if (binary.valueLeft == 0) {
                            section.pushInstr(new SUB("zero", binary.operandRight, binary.output));
                        } else {
                            section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                            section.pushInstr(new SUB("tmp" + cnt++, binary.operandRight, binary.output));
                        }
                    } else if (binary.operandLeft != null) {
                        if (binary.valueRight == 0) {
                            section.pushInstr(new MV(binary.operandLeft, binary.output));
                        } else {
                            section.pushInstr(new ADDI(binary.output, binary.operandLeft, -(int) binary.valueRight));
                        }
                    } else {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new ADDI(binary.output, "tmp" + cnt++, -(int) binary.valueRight));
                    }
                }
            }
            case "mul" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new MUL(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        if (binary.valueLeft == 0) {
                            section.pushInstr(new MV("zero", binary.output));
                        } else if (binary.valueLeft == 1) {
                            section.pushInstr(new MV(binary.operandRight, binary.output));
                        } else if ((binary.valueLeft & (binary.valueLeft - 1)) == 0) {
                            int shift = -1, tmp = (int) binary.valueLeft;
                            while (tmp != 0) {
                                ++shift;
                                tmp = tmp >> 1;
                            }
                            section.pushInstr(new SLLI(binary.output, binary.operandRight, shift));
                        } else {
                            section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                            section.pushInstr(new MUL("tmp" + cnt++, binary.operandRight, binary.output));
                        }
                    } else if (binary.operandLeft != null) {
                        if (binary.valueRight == 0) {
                            section.pushInstr(new MV("zero", binary.output));
                        } else if (binary.valueRight == 1) {
                            section.pushInstr(new MV(binary.operandLeft, binary.output));
                        } else if ((binary.valueRight & (binary.valueRight - 1)) == 0) {
                            int shift = -1, tmp = (int) binary.valueRight;
                            while (tmp != 0) {
                                ++shift;
                                tmp = tmp >> 1;
                            }
                            section.pushInstr(new SLLI(binary.output, binary.operandLeft, shift));
                        } else {
                            section.pushInstr(new LI("tmp" + cnt, (int) binary.valueRight));
                            section.pushInstr(new MUL(binary.operandLeft, "tmp" + cnt++, binary.output));
                        }
                    } else {
                        section.pushInstr(new LI("tmp" + cnt++, (int) binary.valueLeft));
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueRight));
                        section.pushInstr(new MUL("tmp" + (cnt - 1), "tmp" + cnt++, binary.output));
                    }
                }
            }
            case "sdiv" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new DIV(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new DIV("tmp" + cnt++, binary.operandRight, binary.output));
                    } else if (binary.operandLeft != null) {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueRight));
                        section.pushInstr(new DIV(binary.operandLeft, "tmp" + cnt++, binary.output));
                    } else {
                        section.pushInstr(new LI("tmp" + cnt++, (int) binary.valueLeft));
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueRight));
                        section.pushInstr(new DIV("tmp" + (cnt - 1), "tmp" + cnt++, binary.output));
                    }
                }
            }
            case "srem" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new REM(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new REM("tmp" + cnt++, binary.operandRight, binary.output));
                    } else if (binary.operandLeft != null) {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueRight));
                        section.pushInstr(new REM(binary.operandLeft, "tmp" + cnt++, binary.output));
                    } else {
                        section.pushInstr(new LI("tmp" + cnt++, (int) binary.valueLeft));
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueRight));
                        section.pushInstr(new REM("tmp" + (cnt - 1), "tmp" + cnt++, binary.output));
                    }
                }
            }
            case "shl" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new SLL(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new SLL("tmp" + cnt++, binary.operandRight, binary.output));
                    } else if (binary.operandLeft != null) {
                        section.pushInstr(new SLLI(binary.output, binary.operandLeft, (int) binary.valueRight));
                    } else {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new SLLI(binary.output, "tmp" + cnt++, (int) binary.valueRight));
                    }
                }
            }
            case "ashr" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new SRA(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new SRA("tmp" + cnt++, binary.operandRight, binary.output));
                    } else if (binary.operandLeft != null) {
                        section.pushInstr(new SRAI(binary.output, binary.operandLeft, (int) binary.valueRight));
                    } else {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new SRAI(binary.output, "tmp" + cnt++, (int) binary.valueRight));
                    }
                }
            }
            case "and" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new AND(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        section.pushInstr(new ANDI(binary.output, binary.operandRight, (int) binary.valueLeft));
                    } else if (binary.operandLeft != null) {
                        section.pushInstr(new ANDI(binary.output, binary.operandLeft, (int) binary.valueRight));
                    } else {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new ANDI(binary.output, "tmp" + cnt++, (int) binary.valueRight));
                    }
                }
            }
            case "or" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new OR(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        section.pushInstr(new ORI(binary.output, binary.operandRight, (int) binary.valueLeft));
                    } else if (binary.operandLeft != null) {
                        section.pushInstr(new ORI(binary.output, binary.operandLeft, (int) binary.valueRight));
                    } else {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new ORI(binary.output, "tmp" + cnt++, (int) binary.valueRight));
                    }
                }
            }
            case "xor" -> {
                if (binary.operandLeft != null && binary.operandRight != null) {
                    section.pushInstr(new XOR(binary.operandLeft, binary.operandRight, binary.output));
                } else {
                    if (binary.operandRight != null) {
                        section.pushInstr(new XORI(binary.output, binary.operandRight, (int) binary.valueLeft));
                    } else if (binary.operandLeft != null) {
                        section.pushInstr(new XORI(binary.output, binary.operandLeft, (int) binary.valueRight));
                    } else {
                        section.pushInstr(new LI("tmp" + cnt, (int) binary.valueLeft));
                        section.pushInstr(new XORI(binary.output, "tmp" + cnt++, (int) binary.valueRight));
                    }
                }
            }
        }
    }

    void visit(Section section, Icmp icmp) {
        SLT slt = null;
        SLTI slti = null;
        switch (icmp.cond) {
            case "slt", "sge" -> {
                if (icmp.operandLeft != null && icmp.operandRight != null) {
                    slt = new SLT(icmp.operandLeft, icmp.operandRight, "tmp" + cnt);
                    section.pushInstr(slt);
                } else {
                    if (icmp.operandRight != null) {
                        section.pushInstr(new LI("tmp" + cnt++, (int) icmp.valueLeft));
                        slt = new SLT("tmp" + (cnt - 1), icmp.operandRight, "tmp" + cnt);
                        section.pushInstr(slt);
                    } else if (icmp.operandLeft != null) {
                        slti = new SLTI("tmp" + cnt, icmp.operandLeft, (int) icmp.valueRight);
                        section.pushInstr(slti);
                    } else {
                        section.pushInstr(new LI("tmp" + cnt++, (int) icmp.valueLeft));
                        slti = new SLTI("tmp" + cnt, "tmp" + (cnt - 1), (int) icmp.valueRight);
                        section.pushInstr(slti);
                    }
                }
                if (Objects.equals(icmp.cond, "sge")) {
                    section.pushInstr(new XORI(icmp.output, "tmp" + cnt++, 1));
                } else {
                    if (slt != null) {
                        slt.to = icmp.output;
                    } else {
                        slti.to = icmp.output;
                    }
                }
            }
            case "sgt", "sle" -> {
                if (icmp.operandLeft != null && icmp.operandRight != null) {
                    slt = new SLT(icmp.operandRight, icmp.operandLeft, "tmp" + cnt);
                    section.pushInstr(slt);
                } else {
                    if (icmp.operandRight != null) {
                        slti = new SLTI("tmp" + cnt, icmp.operandRight, (int) icmp.valueLeft);
                        section.pushInstr(slti);
                    } else if (icmp.operandLeft != null) {
                        section.pushInstr(new LI("tmp" + cnt++, (int) icmp.valueRight));
                        slt = new SLT("tmp" + (cnt - 1), icmp.operandLeft, "tmp" + cnt);
                        section.pushInstr(slt);
                    } else {
                        section.pushInstr(new LI("tmp" + cnt++, (int) icmp.valueRight));
                        slti = new SLTI("tmp" + cnt, "tmp" + (cnt - 1), (int) icmp.valueLeft);
                        section.pushInstr(slti);
                    }
                }
                if (Objects.equals(icmp.cond, "sle")) {
                    section.pushInstr(new XORI(icmp.output, "tmp" + cnt++, 1));
                } else {
                    if (slt != null) {
                        slt.to = icmp.output;
                    } else {
                        slti.to = icmp.output;
                    }
                }
            }
            case "eq", "ne" -> {
                if (icmp.operandLeft != null && icmp.operandRight != null) {
                    section.pushInstr(new XOR(icmp.operandLeft, icmp.operandRight, "tmp" + cnt));
                } else {
                    if (icmp.operandRight != null) {
                        section.pushInstr(new XORI("tmp" + cnt, icmp.operandRight, (int) icmp.valueLeft));
                    } else if (icmp.operandLeft != null) {
                        section.pushInstr(new XORI("tmp" + cnt, icmp.operandLeft, (int) icmp.valueRight));
                    } else {
                        section.pushInstr(new LI("tmp" + cnt++, (int) icmp.valueLeft));
                        section.pushInstr(new XORI("tmp" + cnt, "tmp" + (cnt - 1), (int) icmp.valueRight));
                    }
                }
                if (Objects.equals(icmp.cond, "eq")) {
                    section.pushInstr(new SEQZ("tmp" + cnt++, icmp.output));
                } else {
                    section.pushInstr(new SNEZ("tmp" + cnt++, icmp.output));
                }
            }
        }
    }

    void visit(Section section, Call call, FuncNode funcNode) {
        FuncNode callNode = getNode(call.functionName.substring(1));
        callNode.fromNode.add(funcNode);
        funcNode.toNode.add(callNode);
        int size = call.callTypeList.size();
        Call.variable variable;
        CallerSave callerSave = new CallerSave(size);
        CALL ASMcall = new CALL(call.functionName.substring(1));
        for (int i = 0; i < min(size, 8); ++i) {
            variable = call.callList.get(i);
            if (variable.varName != null) {
                String from;
                if (variable.varName.charAt(0) == '@') {
//                    from = globalVarReg.get(variable.varName.substring(1));
//                    if (from == null) {
                    from = "tmp" + cnt++;
                    if (variable.varName.contains("-")) {
                        section.pushInstr(new LA(from, variable.varName.substring(1)));
                    } else {
                        section.pushInstr(new LW(variable.varName.substring(1), from));
                    }
                    //}
                } else {
                    from = variable.varName;
                }
                MV mv = new MV(from, "tmp" + cnt);
                mv.preColoredTo = "a" + i;
                section.pushInstr(mv);
                ASMcall.paraList.add(mv);
            } else {
                LI li = new LI("tmp" + cnt, (int) variable.varValue);
                li.preColoredTo = "a" + i;
                section.pushInstr(li);
                ASMcall.paraList.add(li);
            }
            ASMcall.useList.add("tmp" + cnt++);
        }
        if (size > 8) {
            for (int i = 8; i < size; ++i) {
                variable = call.callList.get(i);
                if (variable.varName != null) {
                    String from;
                    if (variable.varName.charAt(0) == '@') {
                        section.pushInstr(new LW(variable.varName.substring(1), "tmp" + cnt));
                        from = "tmp" + cnt++;
                    } else {
                        from = variable.varName;
                    }
                    SW sw = new SW(from, "tmp" + cnt++, (i - 8) << 2);
                    section.pushInstr(sw);
                    sw.preColoredTo = "sp";
                    ASMcall.paraList.add(sw);
                } else {
                    section.pushInstr(new LI("tmp" + cnt++, (int) variable.varValue));
                    SW sw = new SW("tmp" + (cnt - 1), "tmp" + cnt++, (i - 8) << 2);
                    section.pushInstr(sw);
                    sw.preColoredTo = "sp";
                    ASMcall.paraList.add(sw);
                }
            }
        }
        section.pushInstr(callerSave);
        section.pushInstr(ASMcall);
        callNode.callList.add(ASMcall);
        if (call.resultVar != null) {
            MV mv = new MV("tmp" + cnt, call.resultVar);
            ASMcall.def = "tmp" + cnt++;
            mv.preColoredFrom = "a0";
            section.pushInstr(mv);
            ASMcall.retMV = mv;
        }
        section.pushInstr(new CallerRestore(callerSave, call.functionName.substring(1)));
    }

    void visit(Section section, Br br) {
        var phiInfo = phiMap.get(br.nowLabel.substring(1));
        if (br.condition == null) {
            if (phiInfo != null) {
                visit(section, phiInfo.phiTrueList);
            }
            section.pushInstr(new J(br.trueLabel.substring(1)));
        } else {
            boolean flag = phiInfo != null && phiInfo.phiTrueList.size() > 0;
            if (flag) {
                section.pushInstr(new BNEZ(br.condition, "tmpLabel" + label));
            } else {
                section.pushInstr(new BNEZ(br.condition, br.trueLabel.substring(1)));
            }
            if (phiInfo != null && phiInfo.phiFalseList.size() > 0) {
                visit(section, phiInfo.phiFalseList);
            }
            section.pushInstr(new J(br.falseLabel.substring(1)));
            if (flag) {
                section.pushInstr(new LABEL("tmpLabel" + label++));
                visit(section, phiInfo.phiTrueList);
                section.pushInstr(new J(br.trueLabel.substring(1)));
            }
        }
    }

    void visit(Section section, List<FuncDef.PhiInfo.PhiBlock> phiList) {
        List<String> tmpVarList = new ArrayList<>();//中间变量
        List<String> toVarList = new ArrayList<>();//目标变量
        for (var phi : phiList) {
            if (phi.fromVar == null) {
                LI li = new LI(phi.toVar, (int) phi.value);
                if (phi.toVar.contains(".returnValue")) {
                    li.notRemove = true;
                }
                section.pushInstr(li);
            } else {
                String from;
                if (phi.fromVar.contains("@")) {
                    section.pushInstr(new LA("tmp" + cnt, phi.fromVar.substring(1)));
                    from = "tmp" + cnt++;
                } else {
                    from = phi.fromVar;
                }
                MV mv = new MV(from, "tmp" + cnt);
                if (phi.toVar.contains(".returnValue")) {
                    mv.notRemove = true;
                }
                section.pushInstr(mv);
                tmpVarList.add("tmp" + cnt++);
                toVarList.add(phi.toVar);
            }
        }
        for (int i = 0; i < tmpVarList.size(); ++i) {
            section.pushInstr(new MV(tmpVarList.get(i), toVarList.get(i)));
        }
    }

    void visit(Section section, Getelementptr getelementptr) {
        if (getelementptr.indexVar == null) {
            section.pushInstr(new ADDI(getelementptr.result, getelementptr.from, getelementptr.indexValue << 2));
        } else {
            section.pushInstr(new SLLI("tmp" + cnt, getelementptr.indexVar, 2));
            section.pushInstr(new ADD(getelementptr.from, "tmp" + cnt++, getelementptr.result));
        }
    }

    void visit(Section section, Ret ret, Init init) {
        if (ret.irType != null && ret.irType.unitSize != -1) {
            if (ret.var != null) {
                MV mv = new MV(ret.var, "tmp" + cnt++);
                mv.notRemove = true;
                mv.preColoredTo = "a0";
                section.pushInstr(mv);
                init.retInstr = mv;
            } else {
                LI li = new LI("tmp" + cnt++, ret.value);
                li.notRemove = true;
                li.preColoredTo = "a0";
                section.pushInstr(li);
                init.retInstr = li;
            }
        }
        asmProgram.sectionText.pushInstr(new Restore());
        asmProgram.sectionText.pushInstr(new RET());
    }

    public FuncNode getNode(String funcName) {
        var node = funcNodeMap.get(funcName);
        if (node == null) {
            node = new FuncNode(funcName);
            funcNodeMap.put(funcName, node);
        }
        return node;
    }

    void setFuncRestore() {
        HashSet<FuncNode> visit = new HashSet<>();
        Queue<FuncNode> queue = new ArrayDeque<>();
        for (var func : funcNodeMap.values()) {//将未调用除内建函数外其他函数的函数入队
            if (func.toNode.size() == 0) {
                if (!func.restore && !Objects.equals(func.funcName, "main")) {
                    inlineQueue.add(func.funcName);
                }
            } else {
                boolean flag = true;
                for (var toNode : func.toNode) {
                    if (!toNode.restore) {
                        flag = false;
                        break;
                    }
                }
                if (flag && !Objects.equals(func.funcName, "main")) {
                    inlineQueue.add(func.funcName);
                }
            }
        }
        for (var func : funcNodeMap.values()) {
            if (!visit.contains(func)) {
                if (func.restore) {
                    queue.add(func);
                    visit.add(func);
                    while (!queue.isEmpty()) {
                        FuncNode node = queue.poll();
                        if (node.restore) {
                            node.fromNode.forEach(funcNode -> {
                                if (!visit.contains(funcNode)) {
                                    funcNode.restore = true;
                                    queue.add(funcNode);
                                    visit.add(funcNode);
                                }
                            });
                        }
                    }
                }
            }
        }

    }
}
