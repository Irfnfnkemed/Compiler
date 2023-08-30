package src.optimize.RegAllocation;

import src.ASM.ASMBuilder;

import java.util.HashMap;

public class RegAllocation {
    public RegAllocation(ASMBuilder asmBuilder) {
        HashMap<String, Integer> stack = new HashMap<>();
        int cnt = asmBuilder.cnt;
        for (int i = 0; i < asmBuilder.asmProgram.sectionText.asmInstrList.size(); ++i) {
            stack.clear();
            while (true) {
                CFGReg cfgReg = new CFGReg(asmBuilder.asmProgram.sectionText.asmInstrList.get(i), asmBuilder.globalVar);
                RIG rig = new RIG(cfgReg);
                GraphColor graphColor = new GraphColor(rig, cfgReg.globalVar, stack, cnt);
                if (graphColor.graphColor()) {
                    cnt = graphColor.cnt;
                    break;
                }
            }
            CFGReg cfgReg = new CFGReg(asmBuilder.asmProgram.sectionText.asmInstrList.get(i), asmBuilder.globalVar);
            RIG rig = new RIG(cfgReg);
            GraphColor graphColor = new GraphColor(rig, cfgReg.globalVar, stack, cnt);
            graphColor.assignColor();
            if (graphColor.spillSet.size() > 0) {
                System.err.println("???");
            }
        }
    }
}
