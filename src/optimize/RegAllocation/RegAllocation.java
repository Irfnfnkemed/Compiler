package src.optimize.RegAllocation;

import src.ASM.ASMBuilder;
import src.ASM.instruction.*;
import src.ASM.instruction.binaryImme.ADDI;

import java.util.HashMap;
import java.util.Objects;

public class RegAllocation {
    public HashMap<String, Function> functions;

    public RegAllocation(ASMBuilder asmBuilder) {
        functions = new HashMap<>();
        for (int i = 0; i < asmBuilder.asmProgram.sectionText.asmInstrList.size(); ++i) {
            Function function = new Function(asmBuilder.asmProgram.sectionText.asmInstrList.get(i), asmBuilder.globalVar);
            functions.put(((LABEL) asmBuilder.asmProgram.sectionText.asmInstrList.get(i).get(0)).label, function);
        }
        for (Function function : functions.values()) {
            int max = -1;
            for (var call : function.color.callerRestoreList) {
                var callFunc = functions.get(call.funcName);
                if (callFunc != null && !asmBuilder.funcNodeMap.get(call.funcName).restore) {
                    boolean flag = call.callerSave.callerReg.contains("t1");
                    call.callerSave.callerReg.retainAll(callFunc.unsavedReg);
                    if (flag) {
                        call.callerSave.callerReg.add("t1");
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
                    function.color.stack.size() + max + 3) >> 2) << 4;//+3为了对16字节取整
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
                    init.initList.add(new ADDI("sp", "sp", -function.stackSize));
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
            for (var call : function.color.callerRestoreList) {
                int callTmp = 0, callParaStack = call.callerSave.paraSize > 8 ? call.callerSave.paraSize - 8 : 0;
                for (var reg : call.callerSave.callerReg) {
                    call.callerSave.callerList.add(new SW(reg, "sp", (callParaStack + callTmp) << 2));
                    call.callerList.add(new LW("sp", reg, (callParaStack + callTmp++) << 2));
                }
            }
        }


    }
}
