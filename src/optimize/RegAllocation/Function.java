package src.optimize.RegAllocation;

import src.ASM.instruction.ASMInstr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Function {
    public GraphColor color;
    public List<ASMInstr> asmInstrList;
    public HashSet<String> savedReg;
    public HashSet<String> unsavedReg;
    public int stackSize;
    public boolean call = false;//是否调用函数


    public Function(List<ASMInstr> asmInstrList_, HashSet<String> globalVar_) {
        asmInstrList = asmInstrList_;
        HashMap<String, Integer> stack = new HashMap<>();
        while (true) {
            CFGReg cfgReg = new CFGReg(asmInstrList, globalVar_);
            RIG rig = new RIG(cfgReg);
            GraphColor graphColor = new GraphColor(rig, cfgReg.globalVar, stack);
            if (graphColor.graphColor()) {
                color = graphColor;
                break;
            }
        }
        savedReg = new HashSet<>();
        unsavedReg = new HashSet<>();
        for (String reg : color.used) {
            if (reg.charAt(0) == 's') {
                savedReg.add(reg);
            } else {
                unsavedReg.add(reg);
            }
        }
    }
}
