package src.optimize.RegAllocation;

import src.ASM.instruction.ASMInstr;
import src.ASM.instruction.MV;

import java.util.Arrays;
import java.util.HashMap;

public class RIG {
    public static class RIGNode implements Comparable<RIGNode> {
        public HashMap<String, RIGNode> toNode;//无向边连接的对象
        public HashMap<String, RIGNode> mvNode;//传送有关指令
        public String varName;
        public int colour = -1;
        public boolean aboutMove = false;//是否是传送相关的(可能后续被冻结)
        public String preColored;//预着色

        public RIGNode(String varName_) {
            toNode = new HashMap<>();
            mvNode = new HashMap<>();
            varName = varName_;
        }

        public int compareTo(RIGNode obj) {
            if (toNode.size() < obj.toNode.size()) {
                return -1;
            } else if (toNode.size() > obj.toNode.size()) {
                return 1;
            } else return Integer.compare(this.hashCode(), obj.hashCode());
        }
    }

    public CFGReg cfgReg;
    public HashMap<String, RIGNode> rigNodes;

    public RIG(CFGReg cfgReg_) {
        cfgReg = cfgReg_;
        rigNodes = new HashMap<>();
        buildRIG();
        GraphColor graphColor = new GraphColor(this);
        graphColor.graphColor();
    }

    public void buildRIG() {
        ASMInstr asmInstr;
        RIGNode rigNodeNow, rigNodeTo;
        for (var block : cfgReg.blocks.values()) {
            for (int i = block.instructionList.size() - 1; i >= 0; --i) {
                asmInstr = block.instructionList.get(i);
                if (asmInstr.def != null && !asmInstr.notRemove && asmInstr.preColoredFrom == null &&
                        asmInstr.preColoredTo == null && !block.blockLive.liveOut.contains(asmInstr.def)) {
                    block.instructionList.remove(i);
                } else {
                    if (asmInstr.def != null) {
                        block.blockLive.liveOut.remove(asmInstr.def);//此处破坏了liveOut，因为后续不会再用到
                        rigNodeNow = getNode(asmInstr.def);
                        boolean flag = asmInstr instanceof MV;
                        for (var liveVar : block.blockLive.liveOut) {
                            rigNodeTo = getNode(liveVar);
                            rigNodeTo.toNode.put(asmInstr.def, rigNodeNow);
                            rigNodeNow.toNode.put(liveVar, rigNodeTo);
                        }
                        if (flag && asmInstr.use[0] != null && !rigNodeNow.toNode.containsKey(asmInstr.use[0])) {
                            rigNodeTo = getNode(asmInstr.use[0]);
                            rigNodeTo.mvNode.put(asmInstr.def, rigNodeNow);
                            rigNodeNow.mvNode.put(asmInstr.use[0], rigNodeTo);
                            rigNodeTo.aboutMove = true;
                            rigNodeNow.aboutMove = true;
                        }
                        rigNodeNow.preColored = asmInstr.preColoredTo;
                    }
                    if (asmInstr.use[0] != null) {
                        rigNodeNow = getNode(asmInstr.use[0]);
                        rigNodeNow.preColored = asmInstr.preColoredFrom;
                    }
                }
                block.blockLive.liveOut.addAll(Arrays.asList(asmInstr.use).subList(0, asmInstr.useNum));
            }
        }
    }

    public RIGNode getNode(String varName) {
        RIGNode rigNode = rigNodes.get(varName);
        if (rigNode == null) {
            rigNode = new RIGNode(varName);
            rigNodes.put(varName, rigNode);
        }
        return rigNode;
    }
}
