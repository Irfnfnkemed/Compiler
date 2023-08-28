package src.optimize.RegAllocation;

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
    public Stack<RIG.RIGNode> spillStack;
    public TreeSet<RIG.RIGNode> simplifyList;
    public TreeSet<RIG.RIGNode> moveList;
    public HashMap<String, String> coalesceMap;
    public HashMap<String, RIG.RIGNode> nodes;

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
            if (node.aboutMove) {
                moveList.add(node);
            } else {
                simplifyList.add(node);
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
                        moveList.remove(nowMvNode);
                        simplifyList.add(nowMvNode);
                    }
                }
                for (var toNode : nowNode.toNode.values()) {//合并边
                    mvNode.toNode.put(toNode.varName, toNode);
                    if (toNode.toNode.containsKey(mvNode.varName)) {
                        if (toNode.aboutMove) {
                            moveList.remove(toNode);
                            toNode.toNode.remove(nowNode.varName);
                            moveList.add(toNode);
                        } else {
                            simplifyList.remove(toNode);
                            toNode.toNode.remove(nowNode.varName);
                            simplifyList.add(toNode);
                        }
                    } else {
                        toNode.toNode.remove(nowNode.varName);
                        toNode.toNode.put(mvNode.varName, mvNode);
                        if (toNode.mvNode.containsKey(mvNode.varName)) {
                            toNode.mvNode.remove(mvNode.varName);
                            mvNode.mvNode.remove(toNode.varName);
                            if (toNode.mvNode.size() == 0) {
                                toNode.aboutMove = false;//不再传送相关
                                moveList.remove(toNode);
                                simplifyList.add(toNode);
                            }
                            if (mvNode.mvNode.size() == 0) {
                                mvNode.aboutMove = false;//不再传送相关
                            }
                        }
                    }
                }
                if (mvNode.aboutMove) {
                    moveList.add(mvNode);
                } else {
                    simplifyList.add(mvNode);
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
                moveList.remove(mvNode);
                simplifyList.add(mvNode);
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
        for (var mvNode : node.mvNode.values()) {
            mvNode.mvNode.remove(node.varName);
            if (mvNode.mvNode.size() == 0) {//不再传送相关
                mvNode.aboutMove = false;
                moveList.remove(mvNode);
                simplifyList.add(mvNode);
            }
        }
    }

    public boolean judgeGeorge(RIG.RIGNode fromNode, RIG.RIGNode mvNode) {
        for (var toNode : fromNode.toNode.values()) {
            if (toNode.toNode.size() >= k && !mvNode.toNode.containsKey(toNode.varName)) {
                return false;
            }
        }
        return true;
    }


    public boolean setColor(RIG.RIGNode rigNode) {
        int tmp = 0;
        for (var node : rigNode.toNode.values()) {
            if (coalesceMap.containsKey(node.varName)) {
                node = nodes.get(find(node.varName));
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
        nowNode = selectStack.pop();
        nowNode.colour = 1;
        while (!selectStack.isEmpty()) {
            nowNode = selectStack.pop();
            if (!setColor(nowNode)) {
                spillStack.push(nowNode);
            }
        }
    }

    public void graphColor() {
        for (var entry : rig.rigNodes.values()) {
            assert check(entry);
            nodes = entry;
            init(entry);
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

            }
        }
    }

    public boolean check(HashMap<String, RIG.RIGNode> p) {
        for (var entry : p.values()) {
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
