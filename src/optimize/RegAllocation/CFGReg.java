package src.optimize.RegAllocation;

import src.IR.instruction.*;
import src.IR.statement.FuncDef;
import src.optimize.Block;
import src.optimize.CFGBase;

import java.util.*;

public class CFGReg extends CFGBase {
    public static class BlockLive {
        public Set<String> blockUse;
        public Set<String> blockDef;

        public Set<String> liveIn;
        public Set<String> liveOut;

        public BlockLive() {
            blockUse = new HashSet<>();
            blockDef = new HashSet<>();
            liveIn = new HashSet<>();
            liveOut = new HashSet<>();
        }

        public void addDef(String varName) {
            blockDef.add(varName);
        }

        public void addUse(String varName) {
            if (!blockDef.contains(varName)) {
                blockUse.add(varName);
            }
        }
    }

    public static class PhiInfo {//用于处理关于phi的变量

        public static class PhiBlock {
            public String fromVar, toVar;
            public long value;
            public String label;

            public PhiBlock(String fromVar_, String toVar_, long value_, String label_) {
                fromVar = fromVar_;
                toVar = toVar_;
                value = value_;
                label = label_;
            }
        }

        public HashSet<PhiBlock> phiBlockSet;//节点到跳转目标块，所有需要赋值的phi

        public PhiInfo() {
            phiBlockSet = new HashSet<>();
        }

        public void push(String fromVar_, String toVar_, long value_, String label_) {
            phiBlockSet.add(new PhiBlock(fromVar_, toVar_, value_, label_));
        }
    }


    public Block outBlock;
    public HashMap<String, BlockLive> blockLiveMap;
    public HashMap<String, PhiInfo> phiInfoMap;
    public FuncDef funcDef;


    public CFGReg(FuncDef funcDef_) {
        super(funcDef_);
        blockLiveMap = new HashMap<>();
        phiInfoMap = new HashMap<>();
        funcDef = funcDef_;
        collectPhi();
        getBlockUseDef();
        getBlockInOut();
    }

    public void collectPhi() {
        for (var block : funcBlocks.values()) {
            for (var instr : block.instructionList) {
                if (instr instanceof Phi) {
                    for (var assignBlock : ((Phi) instr).assignBlockList) {
                        PhiInfo phiInfo = phiInfoMap.get(assignBlock.label.substring(1));
                        if (phiInfo == null) {
                            phiInfo = new PhiInfo();
                            phiInfoMap.put(assignBlock.label.substring(1), phiInfo);
                        }
                        phiInfo.push(assignBlock.var, ((Phi) instr).result, assignBlock.value, block.label);
                    }
                }
            }
        }
    }

    public void getBlockUseDef() {
        for (var block : funcBlocks.values()) {//得到每个块的等效use和def
            BlockLive blockLive = new BlockLive();
            blockLiveMap.put(block.label, blockLive);
            for (var inst : block.instructionList) {
                if (inst instanceof Alloca) {
                    blockLive.addDef(((Alloca) inst).varName);
                } else if (inst instanceof Binary) {
                    if (((Binary) inst).operandLeft != null) {
                        blockLive.addUse(((Binary) inst).operandLeft);
                    }
                    if (((Binary) inst).operandRight != null) {
                        blockLive.addUse(((Binary) inst).operandRight);
                    }
                    blockLive.addDef(((Binary) inst).output);
                } else if (inst instanceof Br) {
                    if (((Br) inst).condition != null) {
                        blockLive.addUse(((Br) inst).condition);
                    }
                    var phiInfo = phiInfoMap.get(block.label);
                    if (phiInfo != null) {
                        for (var phiBlock : phiInfo.phiBlockSet) {
                            if (phiBlock.fromVar != null) {
                                blockLive.addUse(phiBlock.fromVar);
                            }
                            blockLive.addDef(phiBlock.toVar);
                        }
                    }
                } else if (inst instanceof Call) {
                    for (var variable : ((Call) inst).callList) {
                        if (variable.varName != null) {
                            blockLive.addUse(variable.varName);
                        }
                    }
                    if (((Call) inst).resultVar != null) {
                        blockLive.addDef(((Call) inst).resultVar);
                    }
                } else if (inst instanceof Getelementptr) {
                    if (((Getelementptr) inst).indexVar != null) {
                        blockLive.addUse(((Getelementptr) inst).indexVar);
                    }
                    blockLive.addUse(((Getelementptr) inst).from);
                    blockLive.addDef(((Getelementptr) inst).result);
                } else if (inst instanceof Icmp) {
                    if (((Icmp) inst).operandLeft != null) {
                        blockLive.addUse(((Icmp) inst).operandLeft);
                    }
                    if (((Icmp) inst).operandRight != null) {
                        blockLive.addUse(((Icmp) inst).operandRight);
                    }
                    blockLive.addDef(((Icmp) inst).output);
                } else if (inst instanceof Load) {
                    blockLive.addUse(((Load) inst).fromPointer);
                    blockLive.addDef(((Load) inst).toVarName);
                } else if (inst instanceof Ret) {
                    if (((Ret) inst).var != null) {
                        blockLive.addUse(((Ret) inst).var);
                    }
                } else if (inst instanceof Store) {
                    if (((Store) inst).valueVar != null) {
                        blockLive.addUse(((Store) inst).valueVar);
                    }
                    blockLive.addDef(((Store) inst).toPointer);
                }
            }
            if (block.suc == 0) {
                outBlock = block;
            }
        }
        if (outBlock == null) {//无出度为0的节点，实际上会死循环
            outBlock = funcBlocks.get(funcDef.labelList.get(funcDef.labelList.size() - 1).labelName);
        }
    }

    public void getBlockInOut() {
        Block block;
        BlockLive blockLive;
        Queue<Block> queue = new ArrayDeque<>();
        HashSet<Block> visited = new HashSet<>();
        boolean flag = true;
        while (flag) {
            flag = false;
            queue.add(outBlock);
            visited.clear();
            while (!queue.isEmpty()) {
                block = queue.poll();
                blockLive = blockLiveMap.get(block.label);
                visited.add(block);
                for (int i = 0; i < block.suc; ++i) {
                    if (blockLive.liveOut.addAll(blockLiveMap.get(block.next[i].label).liveIn)) {
                        flag = true;
                    }
                }
                blockLive.liveIn.addAll(blockLive.blockUse);
                for (String var : blockLive.liveOut) {
                    if (!blockLive.blockDef.contains(var)) {
                        if (blockLive.liveIn.add(var)) {
                            flag = true;
                        }
                    }
                }
                for (int i = 0; i < block.suc; ++i) {
                    if (!visited.contains(block.next[i])) {
                        queue.add(block.next[i]);
                    }
                }
            }
        }
    }
}
