package src.optimize.RegAllocation;

import src.ASM.instruction.*;
import src.ASM.instruction.binary.binBase;
import src.ASM.instruction.binaryImme.binImmeBase;

import java.util.*;

import static src.optimize.RegAllocation.GraphColor.REG.*;

public class GraphColor {
    final int k = 27;
    final String[] reg = {
            "t0", "t1", "t2", "t3", "t4", "t5", "t6",
            "a7", "a6", "a5", "a4", "a3", "a2", "a1", "a0",
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11"
    };

    enum REG {
        t0, t1, t2, t3, t4, t5, t6,
        a7, a6, a5, a4, a3, a2, a1, a0,
        s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11,
    }


    public RIG rig;
    public Stack<RIG.RIGNode> selectStack;
    public Stack<RIG.RIGNode> spillStack;
    public TreeSet<RIG.RIGNode> simplifyList;
    public TreeSet<RIG.RIGNode> moveList;
    public HashMap<String, String> coalesceMap;

    public GraphColor(RIG rig_) {
        rig = rig_;
        selectStack = new Stack<>();
        spillStack = new Stack<>();
        simplifyList = new TreeSet<>();
        moveList = new TreeSet<>();
        coalesceMap = new HashMap<>();
    }

    public void init(HashMap<String, RIG.RIGNode> nodes) {
        selectStack.clear();
        spillStack.clear();
        simplifyList.clear();
        moveList.clear();
        coalesceMap.clear();
        for (var node : nodes.values()) {
            node.colour = -1;
            if (node.preColored == null) {
                if (node.aboutMove) {
                    moveList.add(node);
                } else {
                    simplifyList.add(node);
                }
            }
        }
    }

    public boolean simplify() {
        if (simplifyList.isEmpty()) {
            return false;
        }
        var node = simplifyList.first();
        if (node.toNode.size() >= k) {//无法简化
            return false;
        }
        remove(node);
        return true;
    }

    public boolean coalesce() {
        if (moveList.isEmpty()) {
            return false;
        }
        var nowNode = moveList.first();
        if (nowNode.preColored != null) {
            return false;
        }
        for (var mvNode : nowNode.mvNode.values()) {
            if (judgeGeorge(nowNode, mvNode)) {
                coalesceMap.put(nowNode.varName, mvNode.varName);
                moveList.remove(nowNode);
                moveList.remove(mvNode);
                for (var nowMvNode : nowNode.mvNode.values()) {//更改合并对象
                    nowMvNode.mvNode.remove(nowNode.varName);//不再传送
                    if (nowMvNode != mvNode && !nowMvNode.toNode.containsKey(mvNode.varName)) {
                        nowMvNode.mvNode.put(mvNode.varName, mvNode);
                        mvNode.mvNode.put(nowMvNode.varName, nowMvNode);
                    }
                    if (nowMvNode.mvNode.size() == 0) {
                        nowMvNode.aboutMove = false;//不再传送相关
                        if (nowMvNode.preColored == null) {
                            moveList.remove(nowMvNode);
                            simplifyList.add(nowMvNode);
                        }
                    }
                }
                for (var toNode : nowNode.toNode.values()) {//合并边
                    if (mvNode != toNode) {
                        mvNode.toNode.put(toNode.varName, toNode);
                    }
                    if (toNode.toNode.containsKey(mvNode.varName)) {
                        if (toNode.preColored != null) {
                            if (toNode.aboutMove) {
                                toNode.toNode.remove(nowNode.varName);
                            } else {
                                toNode.toNode.remove(nowNode.varName);
                            }
                        } else {
                            if (toNode.aboutMove) {
                                moveList.remove(toNode);
                                toNode.toNode.remove(nowNode.varName);
                                moveList.add(toNode);
                            } else {
                                simplifyList.remove(toNode);
                                toNode.toNode.remove(nowNode.varName);
                                simplifyList.add(toNode);
                            }
                        }
                    } else {
                        toNode.toNode.remove(nowNode.varName);
                        if (toNode != mvNode) {
                            toNode.toNode.put(mvNode.varName, mvNode);
                        }
                        if (toNode.mvNode.containsKey(mvNode.varName)) {
                            toNode.mvNode.remove(mvNode.varName);
                            mvNode.mvNode.remove(toNode.varName);
                            if (toNode.mvNode.size() == 0) {
                                toNode.aboutMove = false;//不再传送相关
                                if (toNode.preColored == null) {
                                    moveList.remove(toNode);
                                    simplifyList.add(toNode);
                                }
                            }
                            if (mvNode.mvNode.size() == 0) {
                                mvNode.aboutMove = false;//不再传送相关
                            }
                        }
                    }
                }
                if (mvNode.preColored == null) {
                    if (mvNode.aboutMove) {
                        moveList.add(mvNode);
                    } else {
                        simplifyList.add(mvNode);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean freeze() {
        var nowNode = moveList.pollFirst();
        if (nowNode == null) {
            return false;
        }
        for (var mvNode : nowNode.mvNode.values()) {//放弃传送
            mvNode.mvNode.remove(nowNode.varName);
            if (mvNode.mvNode.size() == 0) {//不再传送相关
                mvNode.aboutMove = false;
                if (mvNode.preColored == null) {
                    moveList.remove(mvNode);
                    simplifyList.add(mvNode);
                }
            }
        }
        nowNode.mvNode.clear();
        nowNode.aboutMove = false;
        simplifyList.add(nowNode);
        return true;
    }

    public boolean spilt() {
        var node = simplifyList.last();
        if (node == null) {
            return false;
        }
        remove(node);
        return true;
    }

    public void remove(RIG.RIGNode node) {
        selectStack.push(node);
        simplifyList.remove(node);
        for (var toNode : node.toNode.values()) {//移除边，重新排序
            if (toNode.preColored != null) {
                toNode.toNode.remove(node.varName);
            } else {
                if (toNode.aboutMove) {
                    moveList.remove(toNode);
                    toNode.toNode.remove(node.varName);
                    moveList.add(toNode);
                } else {
                    simplifyList.remove(toNode);
                    toNode.toNode.remove(node.varName);
                    simplifyList.add(toNode);
                }
            }
        }
        for (var mvNode : node.mvNode.values()) {
            mvNode.mvNode.remove(node.varName);
            if (mvNode.mvNode.size() == 0) {//不再传送相关
                mvNode.aboutMove = false;
                if (mvNode.preColored == null) {
                    moveList.remove(mvNode);
                    simplifyList.add(mvNode);
                }
            }
        }
    }

    public boolean judgeGeorge(RIG.RIGNode fromNode, RIG.RIGNode mvNode) {
        for (var toNode : fromNode.toNode.values()) {
            if (toNode.toNode.size() >= k && !mvNode.toNode.containsKey(toNode.varName)) {
                return false;
            }
        }
        if (mvNode.preColored != null) {//合并目标已经预染色
            for (var toNode : fromNode.toNode.values()) {
                if (toNode.preColored != null && toNode.preColored.equals(mvNode.preColored)) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean setColor(RIG.RIGNode rigNode) {
        if (rigNode.preColored != null) {
            return true;
        }
        int tmp = 0;
        for (var node : rigNode.toNode.values()) {
            if (coalesceMap.containsKey(node.varName)) {
                node = rig.rigNodes.get(find(node.varName));
            }
            if (node.colour == -1) {
                continue;
            }
            tmp = tmp | (1 << node.colour);
        }
        for (int i = 0; i < k; ++i) {
            if ((tmp & 1) == 0) {
                rigNode.colour = i;
                return true;
            }
            tmp = tmp >> 1;
        }
        return false;
    }


    public String find(String varName) {
        String faName = coalesceMap.get(varName);
        if (faName == null) {
            return varName;
        }
        String findName = find(faName);
        coalesceMap.put(varName, findName);
        return findName;
    }

    public void assignColor() {
        RIG.RIGNode nowNode;
        while (!selectStack.isEmpty()) {
            nowNode = selectStack.pop();
            if (!setColor(nowNode)) {
                spillStack.push(nowNode);
            }
        }
    }

    public String getReg(String varName) {
        var node = rig.rigNodes.get(varName);
        if (node.preColored != null) {
            return node.preColored;
        }
        if (node.colour == -1) {
            node = rig.rigNodes.get(find(node.varName));
        }
        if (node.colour == -1) {
            return node.preColored;//预着色点
        }
        return reg[node.colour];
    }

    public void graphColor() {
        if (!check()) {
            System.err.println("!!!!");
        }
        init(rig.rigNodes);
        while (!simplifyList.isEmpty() || !moveList.isEmpty()) {
            if (!simplify()) {
                if (!coalesce()) {
                    if (!freeze()) {
                        if (!spilt()) {
                            assert false;
                        }
                    }
                }
            }
        }
        assignColor();
        if (spillStack.isEmpty()) {
            for (int i = 0; i < rig.cfgReg.asmInstrList.size(); ++i) {
                var instr = rig.cfgReg.asmInstrList.get(i);
                if (instr instanceof LI) {
                    ((LI) instr).to = getReg(((LI) instr).to);
                } else if (instr instanceof LW) {
                    ((LW) instr).from = getReg(((LW) instr).from);
                    ((LW) instr).to = getReg(((LW) instr).to);
                } else if (instr instanceof LA) {
                    ((LA) instr).to = getReg(((LA) instr).to);
                } else if (instr instanceof SW) {
                    ((SW) instr).from = getReg(((SW) instr).from);
                    ((SW) instr).to = getReg(((SW) instr).to);
                } else if (instr instanceof MV) {
                    ((MV) instr).from = getReg(((MV) instr).from);
                    ((MV) instr).to = getReg(((MV) instr).to);
                    if (Objects.equals(((MV) instr).from, ((MV) instr).to)) {
                        rig.cfgReg.asmInstrList.remove(i--);
                    }
                } else if (instr instanceof binBase) {
                    ((binBase) instr).lhs = getReg(((binBase) instr).lhs);
                    ((binBase) instr).rhs = getReg(((binBase) instr).rhs);
                    ((binBase) instr).to = getReg(((binBase) instr).to);
                } else if (instr instanceof binImmeBase) {
                    ((binImmeBase) instr).from = getReg(((binImmeBase) instr).from);
                    ((binImmeBase) instr).to = getReg(((binImmeBase) instr).to);
                } else if (instr instanceof SEQZ) {
                    ((SEQZ) instr).from = getReg(((SEQZ) instr).from);
                    ((SEQZ) instr).to = getReg(((SEQZ) instr).to);
                } else if (instr instanceof SNEZ) {
                    ((SNEZ) instr).from = getReg(((SNEZ) instr).from);
                    ((SNEZ) instr).to = getReg(((SNEZ) instr).to);
                } else if (instr instanceof BNEZ) {
                    ((BNEZ) instr).condition = getReg(((BNEZ) instr).condition);
                } else if (instr instanceof CallerSave) {
                    for (String varName : ((CallerSave) instr).varName) {
                        ((CallerSave) instr).setCallerReg(getReg(varName));
                    }
                    int tmp = 0;
                    for (String reg : ((CallerSave) instr).callerReg) {
                        rig.cfgReg.asmInstrList.add(i++, new SW(reg, "stackTmp" + tmp++, 0));
                    }
                } else if (instr instanceof CallerRestore) {
                    int tmp = 0;
                    for (String reg : ((CallerRestore) instr).callerSave.callerReg) {
                        rig.cfgReg.asmInstrList.add(i++, new LW("stackTmp" + tmp++, reg, 0));
                    }
                }
            }
        }
    }


    public boolean check() {
        for (var entry : rig.rigNodes.values()) {
            for (var mv : entry.mvNode.values()) {
                if (entry.toNode.containsKey(mv.varName)) {
                    return false;
                }
            }
            if (entry.toNode.containsKey(entry.varName) || entry.mvNode.containsKey(entry.varName)) {
                return false;
            }
        }
        return true;
    }
}
