package src.optimize.RegAllocation;

import src.ASM.Section;
import src.ASM.instruction.*;
import src.ASM.instruction.binary.*;
import src.ASM.instruction.binaryImme.*;

import java.util.*;

public class CFGReg {
    public HashMap<String, HashMap<String, BlockReg>> blocks;//函数名->(块名->Block节点)
    public HashMap<String, BlockReg> outBlocks;//出口(出度为0)
    public Section section;

    public CFGReg(Section section_) {
        section = section_;
        blocks = new HashMap<>();
        outBlocks = new HashMap<>();
        buildCFG();
        getBlockUseDef();
        getBlockInOut();
        RIG rig = new RIG(this);
    }


    public void buildCFG() {
        BlockReg nowBlockReg = null;
        HashMap<String, BlockReg> nowFuncBlockMap = null;
        for (var instr : section.asmInstrList) {//建图
            if (instr instanceof LABEL) {
                if (((LABEL) instr).isFuncBeg) {
                    nowFuncBlockMap = new HashMap<>();
                    blocks.put(((LABEL) instr).label, nowFuncBlockMap);
                }
                assert nowFuncBlockMap != null;
                nowBlockReg = nowFuncBlockMap.get(((LABEL) instr).label);
                if (nowBlockReg == null) {
                    nowBlockReg = new BlockReg(((LABEL) instr).label);
                    nowFuncBlockMap.put(nowBlockReg.label, nowBlockReg);
                }
            } else {
                assert nowBlockReg != null;
                nowBlockReg.pushASM(instr);
                if (instr instanceof BNEZ) {
                    var nextBlock = nowFuncBlockMap.get(((BNEZ) instr).toLabel);
                    if (nextBlock == null) {
                        nextBlock = new BlockReg(((BNEZ) instr).toLabel);
                        nowFuncBlockMap.put(nextBlock.label, nextBlock);
                    }
                    nextBlock.setPre(nowBlockReg);
                    nowBlockReg.setSuc(nextBlock);
                } else if (instr instanceof J) {
                    var nextBlock = nowFuncBlockMap.get(((J) instr).toLabel);
                    if (nextBlock == null) {
                        nextBlock = new BlockReg(((J) instr).toLabel);
                        nowFuncBlockMap.put(nextBlock.label, nextBlock);
                    }
                    nextBlock.setPre(nowBlockReg);
                    nowBlockReg.setSuc(nextBlock);
                }
            }
        }
    }


    public void getBlockUseDef() {
        for (var funcEntry : blocks.entrySet()) {//得到每个块的等效use和def
            BlockReg nowBlock = null;
            for (var entry : funcEntry.getValue().entrySet()) {
                nowBlock = entry.getValue();
                for (var instr : nowBlock.instructionList) {
                    if (instr instanceof LI) {
                        if (!((LI) instr).ignoreDef) {
                            nowBlock.blockLive.addDef(((LI) instr).to);
                            instr.def = ((LI) instr).to;
                        }
                    } else if (instr instanceof LW) {
                        nowBlock.blockLive.addUse(((LW) instr).from);
                        nowBlock.blockLive.addDef(((LW) instr).to);
                        instr.use[0] = ((LW) instr).from;
                        instr.def = ((LW) instr).to;
                        instr.useNum = 1;
                    } else if (instr instanceof SW) {
                        nowBlock.blockLive.addUse(((SW) instr).from);
                        nowBlock.blockLive.addDef(((SW) instr).to);
                        instr.use[0] = ((SW) instr).from;
                        instr.def = ((SW) instr).to;
                        instr.useNum = 1;
                    } else if (instr instanceof MV) {
                        if (!((MV) instr).ignoreUse) {
                            nowBlock.blockLive.addUse(((MV) instr).from);
                            instr.use[0] = ((MV) instr).from;
                            instr.useNum = 1;
                        } else {
                            instr.useNum = 0;
                        }
                        if (!((MV) instr).ignoreDef) {
                            nowBlock.blockLive.addDef(((MV) instr).to);
                            instr.def = ((MV) instr).to;
                        }
                    } else if (instr instanceof binBase) {
                        nowBlock.blockLive.addUse(((binBase) instr).lhs);
                        nowBlock.blockLive.addUse(((binBase) instr).rhs);
                        nowBlock.blockLive.addDef(((binBase) instr).to);
                        instr.use[0] = ((binBase) instr).lhs;
                        instr.use[1] = ((binBase) instr).rhs;
                        instr.def = ((binBase) instr).to;
                        instr.useNum = 2;
                    } else if (instr instanceof binImmeBase) {
                        nowBlock.blockLive.addUse(((binImmeBase) instr).from);
                        nowBlock.blockLive.addDef(((binImmeBase) instr).to);
                        instr.use[0] = ((binImmeBase) instr).from;
                        instr.def = ((binImmeBase) instr).to;
                        instr.useNum = 1;
                    } else if (instr instanceof SEQZ) {
                        nowBlock.blockLive.addUse(((SEQZ) instr).from);
                        nowBlock.blockLive.addDef(((SEQZ) instr).to);
                        instr.use[0] = ((SEQZ) instr).from;
                        instr.def = ((SEQZ) instr).to;
                        instr.useNum = 1;
                    } else if (instr instanceof SNEZ) {
                        nowBlock.blockLive.addUse(((SNEZ) instr).from);
                        nowBlock.blockLive.addDef(((SNEZ) instr).to);
                        instr.use[0] = ((SNEZ) instr).from;
                        instr.def = ((SNEZ) instr).to;
                        instr.useNum = 1;
                    } else if (instr instanceof BNEZ) {
                        nowBlock.blockLive.addUse(((BNEZ) instr).condition);
                        instr.use[0] = ((BNEZ) instr).condition;
                        instr.useNum = 1;
                    }
                }
                nowBlock.blockLive.liveIn.addAll(nowBlock.blockLive.blockUse);
                if (nowBlock.suc == 0) {
                    outBlocks.put(funcEntry.getKey(), nowBlock);
                }
            }
            if (!outBlocks.containsKey(funcEntry.getKey())) {//无出度为0的节点，实际上会死循环
                outBlocks.put(funcEntry.getKey(), nowBlock);
            }
        }
    }

    public void getBlockInOut() {
        BlockReg blockReg, outBlock;
        Queue<BlockReg> queue = new ArrayDeque<>();
        HashSet<BlockReg> visited = new HashSet<>();
        for (var funcEntry : blocks.entrySet()) {
            outBlock = outBlocks.get(funcEntry.getKey());
            boolean flag = true;
            while (flag) {
                flag = false;
                queue.add(outBlock);
                visited.clear();
                while (!queue.isEmpty()) {
                    blockReg = queue.poll();
                    for (int i = 0; i < blockReg.suc; ++i) {
                        if (blockReg.blockLive.liveOut.addAll(funcEntry.getValue().get(blockReg.next[i].label).blockLive.liveIn)) {
                            flag = true;
                        }
                    }
                    for (String var : blockReg.blockLive.liveOut) {
                        if (!blockReg.blockLive.blockDef.contains(var)) {
                            if (blockReg.blockLive.liveIn.add(var)) {
                                flag = true;
                            }
                        }
                    }
                    for (int i = 0; i < blockReg.pre; ++i) {
                        if (!visited.contains(blockReg.prev.get(i))) {
                            queue.add(blockReg.prev.get(i));
                            visited.add(blockReg.prev.get(i));
                        }
                    }
                }
            }
        }
    }
}
