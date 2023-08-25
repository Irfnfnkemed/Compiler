package src.optimize.RegAllocation;

import src.ASM.instruction.ASMInstr;
import src.ASM.instruction.MV;

import java.util.Arrays;
import java.util.HashMap;

public class RIG {
    public static class RIGNode {
        public HashMap<String, RIGNode> toNode;//无向边连接的对象
        public HashMap<String, RIGNode> mvNode;//传送有关指令

        public RIGNode() {
            toNode = new HashMap<>();
            mvNode = new HashMap<>();
        }
    }

    public CFGReg cfgReg;
    public HashMap<String, HashMap<String, RIGNode>> rigNodes;

    public RIG(CFGReg cfgReg_) {
        cfgReg = cfgReg_;
        rigNodes = new HashMap<>();
        buildRIG();
    }

    public void buildRIG() {
        ASMInstr asmInstr;
        RIGNode rigNodeNow, rigNodeTo;
        for (var funcEntry : cfgReg.blocks.entrySet()) {
            HashMap<String, RIGNode> rigNodeMap = new HashMap<>();
            rigNodes.put(funcEntry.getKey(), rigNodeMap);
            for (var block : funcEntry.getValue().values()) {
                for (int i = block.instructionList.size() - 1; i >= 0; --i) {
                    asmInstr = block.instructionList.get(i);
                    if (asmInstr.def != null && !block.blockLive.liveOut.contains(asmInstr.def) && asmInstr.notRemove) {
                        block.instructionList.remove(i);
                    } else {
                        if (asmInstr.def != null) {
                            block.blockLive.liveOut.remove(asmInstr.def);//此处破坏了liveOut，因为后续不会再用到
                            rigNodeNow = getNode(rigNodeMap, asmInstr.def);
                            boolean flag = asmInstr instanceof MV;
                            for (var liveVar : block.blockLive.liveOut) {
                                rigNodeTo = getNode(rigNodeMap, liveVar);
                                rigNodeTo.toNode.put(asmInstr.def, rigNodeNow);
                                rigNodeNow.toNode.put(liveVar, rigNodeTo);
                                if (flag) {
                                    rigNodeTo.mvNode.put(asmInstr.def, rigNodeNow);
                                    rigNodeNow.mvNode.put(liveVar, rigNodeTo);
                                }
                            }
                        }
                        block.blockLive.liveOut.addAll(Arrays.asList(asmInstr.use).subList(0, asmInstr.useNum));
                    }
                }
            }
        }
    }

    public RIGNode getNode(HashMap<String, RIGNode> rigNodeMap, String varName) {
        RIGNode rigNode = rigNodeMap.get(varName);
        if (rigNode == null) {
            rigNode = new RIGNode();
            rigNodeMap.put(varName, rigNode);
        }
        return rigNode;
    }
}
