package src.optimize.RegAllocation;

import src.ASM.instruction.*;
import src.ASM.instruction.binary.binBase;
import src.ASM.instruction.binaryImme.binImmeBase;

import java.util.*;

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
    public HashSet<String> spillSet;
    public TreeSet<RIG.RIGNode> simplifyList;
    public TreeSet<RIG.RIGNode> moveList;
    public HashMap<String, String> coalesceMap;
    public HashSet<String> globalVar;
    public HashMap<String, Integer> stack;//栈上变量
    public int cnt;

    public GraphColor(RIG rig_, HashSet<String> globalVar_, HashMap<String, Integer> stack_, int cnt_) {
        rig = rig_;
        selectStack = new Stack<>();
        spillSet = new HashSet<>();
        simplifyList = new TreeSet<>();
        moveList = new TreeSet<>();
        coalesceMap = new HashMap<>();
        globalVar = globalVar_;
        stack = stack_;
        cnt = cnt_;
    }

    public void init(HashMap<String, RIG.RIGNode> nodes) {
        selectStack.clear();
        spillSet.clear();
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
                spillSet.add(nowNode.varName);
            }
        }
    }

    public String getReg(String varName) {
        var node = rig.rigNodes.get(varName);
        if (node == null) {
            if (globalVar.contains(varName) || Objects.equals(varName, "stack#") ||
                    Objects.equals(varName, "stackTmp#") || Objects.equals(varName, "stackTop#")) {
                return varName;
            } else {
                return null;
            }
        }
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

    public boolean graphColor() {
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
        if (spillSet.isEmpty()) {
            replace();
            return true;
        } else {
            rewrite();
            return false;
        }
    }

    public void replace() {
        for (int i = 0; i < rig.cfgReg.asmInstrList.size(); ++i) {
            var instr = rig.cfgReg.asmInstrList.get(i);
            if (instr instanceof LI) {
                ((LI) instr).to = getReg(((LI) instr).to);
                if (((LI) instr).to == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof LW) {
                ((LW) instr).from = getReg(((LW) instr).from);
                ((LW) instr).to = getReg(((LW) instr).to);
                if (((LW) instr).from == null || ((LW) instr).to == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof LA) {
                ((LA) instr).to = getReg(((LA) instr).to);
                if (((LA) instr).to == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof SW) {
                ((SW) instr).from = getReg(((SW) instr).from);
                ((SW) instr).to = getReg(((SW) instr).to);
                if (((SW) instr).from == null || ((SW) instr).to == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof MV) {
                ((MV) instr).from = getReg(((MV) instr).from);
                ((MV) instr).to = getReg(((MV) instr).to);
                if (((MV) instr).from == null || ((MV) instr).to == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
                if (Objects.equals(((MV) instr).from, ((MV) instr).to)) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof binBase) {
                ((binBase) instr).lhs = getReg(((binBase) instr).lhs);
                ((binBase) instr).rhs = getReg(((binBase) instr).rhs);
                ((binBase) instr).to = getReg(((binBase) instr).to);
                if (((binBase) instr).lhs == null || ((binBase) instr).rhs == null || ((binBase) instr).to == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof binImmeBase) {
                ((binImmeBase) instr).from = getReg(((binImmeBase) instr).from);
                ((binImmeBase) instr).to = getReg(((binImmeBase) instr).to);
                if (((binImmeBase) instr).from == null || ((binImmeBase) instr).to == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof SEQZ) {
                ((SEQZ) instr).from = getReg(((SEQZ) instr).from);
                ((SEQZ) instr).to = getReg(((SEQZ) instr).to);
                if (((SEQZ) instr).from == null || ((SEQZ) instr).to == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof SNEZ) {
                ((SNEZ) instr).from = getReg(((SNEZ) instr).from);
                ((SNEZ) instr).to = getReg(((SNEZ) instr).to);
                if (((SNEZ) instr).from == null || ((SNEZ) instr).to == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof BNEZ) {
                ((BNEZ) instr).condition = getReg(((BNEZ) instr).condition);
                if (((BNEZ) instr).condition == null) {
                    rig.cfgReg.asmInstrList.remove(i--);
                }
            } else if (instr instanceof CallerSave) {
                for (String varName : ((CallerSave) instr).varName) {
                    ((CallerSave) instr).setCallerReg(getReg(varName));
                }
                int tmp = 0;
                for (String reg : ((CallerSave) instr).callerReg) {
                    rig.cfgReg.asmInstrList.add(i++, new SW(reg, "stackTmp", tmp++));
                }
            } else if (instr instanceof CallerRestore) {
                int tmp = 0;
                for (String reg : ((CallerRestore) instr).callerSave.callerReg) {
                    rig.cfgReg.asmInstrList.add(i++, new LW("stackTmp", reg, tmp++));
                }
            }
        }
    }

    public void rewrite() {
        for (String var : spillSet) {
            stack.put(var, stack.size());
        }
        for (int i = 0; i < rig.cfgReg.asmInstrList.size(); ++i) {
            var instr = rig.cfgReg.asmInstrList.get(i);
            if (instr instanceof MV) {
                Integer from = stack.get(((MV) instr).from), to = stack.get(((MV) instr).to);
                if (from != null) {
                    rig.cfgReg.asmInstrList.add(i++, new LW("stack#", "cnt" + cnt, from));
                    ((MV) instr).from = "cnt" + cnt++;
                }
                if (to != null) {
                    rig.cfgReg.asmInstrList.add(++i, new SW("cnt" + cnt, "stack#", to));
                    ((MV) instr).to = "cnt" + cnt++;
                }
            } else if (instr instanceof binBase) {
                Integer lhs = stack.get(((binBase) instr).lhs), rhs = stack.get(((binBase) instr).rhs),
                        to = stack.get(((binBase) instr).to);
                if (lhs != null) {
                    rig.cfgReg.asmInstrList.add(i++, new LW("stack#", "cnt" + cnt, lhs));
                    ((binBase) instr).lhs = "cnt" + cnt++;
                }
                if (rhs != null) {
                    rig.cfgReg.asmInstrList.add(i++, new LW("stack#", "cnt" + cnt, rhs));
                    ((binBase) instr).rhs = "cnt" + cnt++;
                }
                if (to != null) {
                    rig.cfgReg.asmInstrList.add(++i, new SW("cnt" + cnt, "stack#", to));
                    ((binBase) instr).to = "cnt" + cnt++;
                }
            } else if (instr instanceof binImmeBase) {
                Integer from = stack.get(((binImmeBase) instr).from), to = stack.get(((binImmeBase) instr).to);
                if (from != null) {
                    rig.cfgReg.asmInstrList.add(i++, new LW("stack#", "cnt" + cnt, from));
                    ((binImmeBase) instr).from = "cnt" + cnt++;
                }
                if (to != null) {
                    rig.cfgReg.asmInstrList.add(++i, new SW("cnt" + cnt, "stack#", to));
                    ((binImmeBase) instr).to = "cnt" + cnt++;
                }
            } else if (instr instanceof SEQZ) {
                Integer from = stack.get(((SEQZ) instr).from), to = stack.get(((SEQZ) instr).to);
                if (from != null) {
                    rig.cfgReg.asmInstrList.add(i++, new LW("stack#", "cnt" + cnt, from));
                    ((SEQZ) instr).from = "cnt" + cnt++;
                }
                if (to != null) {
                    rig.cfgReg.asmInstrList.add(++i, new SW("cnt" + cnt, "stack#", to));
                    ((SEQZ) instr).to = "cnt" + cnt++;
                }
            } else if (instr instanceof SNEZ) {
                Integer from = stack.get(((SNEZ) instr).from), to = stack.get(((SNEZ) instr).to);
                if (from != null) {
                    rig.cfgReg.asmInstrList.add(i++, new LW("stack#", "cnt" + cnt, from));
                    ((SNEZ) instr).from = "cnt" + cnt++;
                }
                if (to != null) {
                    rig.cfgReg.asmInstrList.add(++i, new SW("cnt" + cnt, "stack#", to));
                    ((SNEZ) instr).to = "cnt" + cnt++;
                }
            } else if (instr instanceof BNEZ) {
                Integer condition = stack.get(((BNEZ) instr).condition);
                if (condition != null) {
                    rig.cfgReg.asmInstrList.add(i++, new LW("stack#", "cnt" + cnt, condition));
                    ((BNEZ) instr).condition = "cnt" + cnt++;
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
