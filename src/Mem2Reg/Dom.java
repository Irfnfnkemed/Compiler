package src.mem2Reg;

import java.util.*;

public class Dom {
    public static class DomInfo {//节点的支配集、直接支配节点和支配边界
        public String blockName;
        public HashMap<String, DomInfo> domSet;//支配集
        public DomInfo immeDom;//直接支配节点
        public HashSet<String> domFrontier;//支配边界

        public DomInfo(String blockName_) {
            blockName = blockName_;
            domSet = new HashMap<>();
            domFrontier = new HashSet<>();
        }
    }

    public HashMap<String, DomInfo> domMap;
    public CFG cfg;

    public Dom(CFG cfg_) {
        cfg = cfg_;
        domMap = new HashMap<>();
        buildDomSet();
        buildDomTree();
        buildDomFrontier();
    }

    private void buildDomSet() {
        cfg.funcBlocks.keySet().forEach(label -> domMap.put(label, new DomInfo(label)));
        for (DomInfo domInfo : domMap.values()) {
            if (Objects.equals(domInfo.blockName, "entry")) {
                domInfo.domSet.put("entry", domInfo);
            } else {
                for (String label : domMap.keySet()) {
                    domInfo.domSet.put(label, domMap.get(label));
                }
            }
        }
        boolean flag = true;
        while (flag) {
            flag = false;
            for (var entry : cfg.funcBlocks.entrySet()) {
                if (getIntersection(entry.getValue().prev, entry.getKey())) {
                    flag = true;
                }
            }
        }
    }

    private boolean getIntersection(List<Block> prev, String nowLabel) {//发现前驱的支配集交集有元素要去除，返回true
        if (prev.size() == 0) {
            return false;
        }
        boolean flag = false;
        HashMap<String, Integer> intersection = new HashMap<>();
        for (var block : prev) {
            for (var domSet : domMap.get(block.label).domSet.values()) {
                if (!intersection.containsKey(domSet.blockName)) {
                    intersection.put(domSet.blockName, 1);
                } else {
                    intersection.put(domSet.blockName, intersection.get(domSet.blockName) + 1);
                }
            }
        }
        DomInfo nowDomInfo = domMap.get(nowLabel);
        var iterator = nowDomInfo.domSet.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            Integer num = intersection.get(entry.getKey());
            if (!Objects.equals(entry.getKey(), nowLabel) && (num == null || num != prev.size())) {
                flag = true;
                iterator.remove();
            }
        }
        return flag;
    }

    private void buildDomTree() {
        DomInfo root = domMap.get("entry");
        List<String> already = new ArrayList<>();
        visitNode(root, already);
    }

    private void visitNode(DomInfo node, List<String> already) {
        if (node.immeDom != null) {
            return;
        }
        for (int i = already.size() - 1; i >= 0; --i) {
            var dom = node.domSet.get(already.get(i));
            if (dom != null) {
                node.immeDom = dom;
                break;
            }
        }
        already.add(node.blockName);
        for (int i = 0; i < cfg.funcBlocks.get(node.blockName).suc; ++i) {
            visitNode(domMap.get(cfg.funcBlocks.get(node.blockName).next[i].label), already);
        }
    }

    private void buildDomFrontier() {
        Block nowBlock;
        DomInfo domInfoPre, domInfoNow;
        for (var entry : domMap.entrySet()) {
            nowBlock = cfg.funcBlocks.get(entry.getKey());
            domInfoNow = domMap.get(entry.getKey());
            if (nowBlock.pre > 1) {//汇合点
                for (var preBlock : nowBlock.prev) {
                    domInfoPre = domMap.get(preBlock.label);
                    while (domInfoPre != entry.getValue().immeDom) {
                        domMap.get(domInfoPre.blockName).domFrontier.add(domInfoNow.blockName);
                        domInfoPre = domInfoPre.immeDom;
                    }
                }
            }
        }
    }
}
