package src.optimize.RegAllocation;

import src.ASM.ASMBuilder;
import src.ASM.instruction.*;
import src.ASM.instruction.binary.binBase;
import src.ASM.instruction.binaryImme.ADDI;
import src.ASM.instruction.binaryImme.binImmeBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RegAllocation {
    public HashMap<String, Function> functions;
    int inline = 0;

    public RegAllocation(ASMBuilder asmBuilder) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        functions = new HashMap<>();
        HashMap<String, List<ASMInstr>> ASMInstrMap = new HashMap<>();
        for (var instrList : asmBuilder.asmProgram.sectionText.asmInstrList) {
            ASMInstrMap.put(((LABEL) instrList.get(0)).label, instrList);
        }
        while (!asmBuilder.inlineQueue.isEmpty()) {
            String funcName = asmBuilder.inlineQueue.poll();
            var list = ASMInstrMap.get(funcName);
            ASMInstrMap.remove(funcName);
            Function function = new Function(list, asmBuilder.globalVar);
            if (function.graphColor.used.size() < 10) {
                var funcNode = asmBuilder.getNode(funcName);
                changeParaAndRet((Init) list.get(1));
                for (CALL call : funcNode.callList) {
                    inline(call, list);
                }
                for (var fromNode : funcNode.fromNode) {
                    fromNode.toNode.remove(funcNode);
                    if (fromNode.toNode.size() == 0 && !Objects.equals(fromNode.funcName, "main")) {
                        asmBuilder.inlineQueue.add(fromNode.funcName);
                    }
                }
            } else {
                function.allocateReg();
                functions.put(funcName, function);
            }
        }
        for (int i = 0; i < asmBuilder.asmProgram.sectionText.asmInstrList.size(); ++i) {
            var list = asmBuilder.asmProgram.sectionText.asmInstrList.get(i);
            if (ASMInstrMap.containsKey(((LABEL) list.get(0)).label)) {
                Function function = new Function(asmBuilder.asmProgram.sectionText.asmInstrList.get(i), asmBuilder.globalVar);
                function.allocateReg();
                functions.put(((LABEL) list.get(0)).label, function);
            }
        }
        for (Function function : functions.values()) {
            int max = -1;
            for (var call : function.graphColor.callerRestoreList) {
                var callFunc = functions.get(call.funcName);
                if (callFunc != null && !asmBuilder.funcNodeMap.get(call.funcName).restore) {
                    var iter = call.callerSave.callerReg.iterator();
                    while (iter.hasNext()) {
                        String reg = iter.next();
                        if (!Objects.equals(reg, "t1") && reg.charAt(0) != 'a' &&
                                !call.callerSave.callerReg.contains(reg)) {
                            iter.remove();
                        }
                    }
                }
                int callParaStack = call.callerSave.paraSize > 8 ? call.callerSave.paraSize - 8 : 0;
                if (call.callerSave.callerReg.size() + callParaStack > max) {
                    max = call.callerSave.callerReg.size() + callParaStack;
                }
            }
            if (max > -1) {
                function.call = true;
            } else {
                max = 0;
            }
            function.stackSize = (((function.call ? 1 : 0) + function.savedReg.size() +
                    function.graphColor.stack.size() + max + 3) >> 2) << 4;//+3为了对16字节取整
            Init init = null;
            int tmp = 2 + function.savedReg.size();
            for (int i = 0; i < function.asmInstrList.size(); ++i) {
                var instr = function.asmInstrList.get(i);
                if (instr instanceof LW) {
                    if (Objects.equals(((LW) instr).from, "stack#")) {
                        ((LW) instr).from = "sp";
                        ((LW) instr).offset = function.stackSize - ((tmp + ((LW) instr).offset) << 2);
                    } else if (Objects.equals(((LW) instr).from, "stackTop#")) {
                        ((LW) instr).from = "sp";
                        ((LW) instr).offset = function.stackSize + ((LW) instr).offset;
                    }
                } else if (instr instanceof SW) {
                    if (Objects.equals(((SW) instr).to, "stack#")) {
                        ((SW) instr).to = "sp";
                        ((SW) instr).offset = function.stackSize - ((tmp + ((SW) instr).offset) << 2);
                    } else if (Objects.equals(((SW) instr).to, "stackTop#")) {
                        ((SW) instr).to = "sp";
                        ((SW) instr).offset = function.stackSize + ((SW) instr).offset;
                    }
                } else if (instr instanceof Init) {
                    init = (Init) instr;
                    if (function.stackSize > 0) {
                        init.initList.add(new ADDI("sp", "sp", -function.stackSize));
                    }
                    int initTmp = 1;
                    for (String reg : function.savedReg) {
                        init.initList.add(new SW(reg, "sp", function.stackSize - (initTmp++ << 2)));
                    }
                    if (function.call) {
                        init.initList.add(new SW("ra", "sp", function.stackSize - (initTmp++ << 2)));
                    }
                } else if (instr instanceof Restore) {
                    assert init != null;
                    ((Restore) instr).set(init);
                }
            }
            for (var call : function.graphColor.callerRestoreList) {
                int callTmp = 0, callParaStack = call.callerSave.paraSize > 8 ? call.callerSave.paraSize - 8 : 0;
                for (var reg : call.callerSave.callerReg) {
                    call.callerSave.callerList.add(new SW(reg, "sp", (callParaStack + callTmp) << 2));
                    call.callerList.add(new LW("sp", reg, (callParaStack + callTmp++) << 2));
                }
            }
        }
        for (int i = 0; i < asmBuilder.asmProgram.sectionText.asmInstrList.size(); ++i) {
            var instrList = asmBuilder.asmProgram.sectionText.asmInstrList.get(i);
            if (!functions.containsKey(((LABEL) instrList.get(0)).label)) {
                asmBuilder.asmProgram.sectionText.asmInstrList.remove(i--);
            }
        }
    }

    public void changeParaAndRet(Init init) {
        for (int i = 0; i < init.paraList.size(); ++i) {
            var instr = init.paraList.get(i);
            if (instr instanceof MV) {
                ((MV) instr).from = "inline$para-" + i;
            } else if (instr instanceof LW) {
                ((LW) instr).from = "inline$para-" + i;
                ((LW) instr).offset = -2;
            }
        }
        if (init.retInstr instanceof LI) {
            ((LI) init.retInstr).to = "inline$ret";
        } else if (init.retInstr instanceof MV) {
            ((MV) init.retInstr).to = "inline$ret";
        }
    }

    public void inline(CALL call, List<ASMInstr> list) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {//将函数作为内联函数嵌入
        String postFix = "-inline-" + inline++;
        CallerSave callerSave = null;
        for (int i = 1; i < list.size(); ++i) {
            boolean flagFrom = true, flagTo = true;
            var instr = list.get(i);
            if (instr instanceof LABEL) {
                call.inlineCache.add(new LABEL(((LABEL) instr).label + postFix));
                continue;
            } else if (instr instanceof LI) {
                if (((LI) instr).to.contains("$")) {
                    flagTo = false;
                }
                call.inlineCache.add(new LI(((LI) instr).to + postFix, ((LI) instr).imme));
            } else if (instr instanceof LW) {
                if (((LW) instr).from.contains("$")) {
                    flagFrom = false;
                }
                if (((LW) instr).to.contains("$")) {
                    flagTo = false;
                }
                if (((LW) instr).offset == -2) {
                    call.inlineCache.add(new MV(((LW) instr).from + postFix, ((LW) instr).to + postFix));
                } else if (((LW) instr).offset == -1) {
                    call.inlineCache.add(new LW(((LW) instr).from, ((LW) instr).to + postFix, ((LW) instr).offset));
                } else {
                    call.inlineCache.add(new LW(((LW) instr).from + postFix, ((LW) instr).to + postFix, ((LW) instr).offset));
                }
            } else if (instr instanceof LA) {
                call.inlineCache.add(new LA(((LA) instr).to + postFix, ((LA) instr).fromLabel));
            } else if (instr instanceof SW) {
                if (((SW) instr).from.contains("$")) {
                    flagFrom = false;
                }
                if (((SW) instr).to.contains("$")) {
                    flagTo = false;
                }
                call.inlineCache.add(new SW(((SW) instr).from + postFix, ((SW) instr).to + postFix, ((SW) instr).offset));
            } else if (instr instanceof MV) {
                if (((MV) instr).from.contains("$")) {
                    flagFrom = false;
                }
                if (((MV) instr).to.contains("$")) {
                    flagTo = false;
                }
                call.inlineCache.add(new MV(((MV) instr).from + postFix, ((MV) instr).to + postFix));
            } else if (instr instanceof binBase) {
                Class<?> clazz = instr.getClass();
                Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);
                constructor.setAccessible(true); // 设置Aa为可访问
                Object newObj = constructor.newInstance(((binBase) instr).lhs + postFix,
                        ((binBase) instr).rhs + postFix, ((binBase) instr).to + postFix);
                call.inlineCache.add((ASMInstr) newObj);
            } else if (instr instanceof binImmeBase) {
                Class<?> clazz = instr.getClass();
                Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, String.class, int.class);
                constructor.setAccessible(true); // 设置为可访问
                Object newObj = constructor.newInstance(((binImmeBase) instr).to + postFix,
                        ((binImmeBase) instr).from + postFix, ((binImmeBase) instr).imme);
                call.inlineCache.add((ASMInstr) newObj);
            } else if (instr instanceof SEQZ) {
                call.inlineCache.add(new SEQZ(((SEQZ) instr).from + postFix, ((SEQZ) instr).to + postFix));
            } else if (instr instanceof SNEZ) {
                call.inlineCache.add(new SNEZ(((SNEZ) instr).from + postFix, ((SNEZ) instr).to + postFix));
            } else if (instr instanceof BNEZ) {
                call.inlineCache.add(new BNEZ(((BNEZ) instr).condition + postFix, ((BNEZ) instr).toLabel + postFix));
            } else if (instr instanceof CALL) {
                CALL ASMcall = new CALL(((CALL) instr).func);
                for (String use : ((CALL) instr).useList) {
                    ASMcall.useList.add(use + postFix);
                }
                call.inlineCache.add(ASMcall);
            } else if (instr instanceof CallerSave) {
                callerSave = new CallerSave(((CallerSave) instr).paraSize);
                call.inlineCache.add(callerSave);
            } else if (instr instanceof CallerRestore) {
                call.inlineCache.add(new CallerRestore(callerSave, ((CallerRestore) instr).funcName));
            } else if (instr instanceof J) {
                call.inlineCache.add(new J(((J) instr).toLabel + postFix));
            } else {
                continue;
            }
            var inlineInstr = call.inlineCache.get(call.inlineCache.size() - 1);
            if (flagFrom) {
                inlineInstr.preColoredFrom = instr.preColoredFrom;
            }
            if (flagTo) {
                inlineInstr.preColoredTo = instr.preColoredTo;
            }
        }
        for (int i = 0; i < call.paraList.size(); ++i) {
            var instr = call.paraList.get(i);
            if (instr instanceof LI) {
                ((LI) instr).to = "inline$para-" + i + postFix;
            } else if (instr instanceof MV) {
                ((MV) instr).to = "inline$para-" + i + postFix;
            } else if (instr instanceof SW) {
                ((SW) instr).to = "inline$para-" + i + postFix;
                ((SW) instr).offset = -2;
            }
            instr.preColoredTo = null;//取消预着色
        }
        if (call.retMV != null) {
            call.retMV.from = "inline$ret" + postFix;
            call.retMV.preColoredFrom = null;//取消预着色
        }
    }
}
