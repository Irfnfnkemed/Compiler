package src.Mem2Reg;

import java.util.*;

public class Dom {
    public static class domSet {//节点的支配集
        public String blockName;
        public List<domSet> domSetList;

        public domSet(String blockName_) {
            blockName = blockName_;
            domSetList = new ArrayList<>();
            domSetList.add(this);
        }
    }

    public HashMap<String, domSet> domMap;

    public Dom(CFG cfg) {
        domMap = new HashMap<>();
        cfg.funcBlocks.forEach((label, block) -> domMap.put(label, new domSet(label)));
        boolean flag = true;
        while (flag) {
            flag = true;
            for (var entry : cfg.funcBlocks.entrySet()) {
                if (!getIntersection(entry.getValue().prev, entry.getKey())) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        }
    }

    public boolean getIntersection(List<Block> prev, String nowLabel) {//发现前驱的支配集交集有新元素，返回true
        if (prev.size() == 0) {
            return false;
        }
        boolean flag = false;
        HashMap<String, Integer> intersection = new HashMap<>();
        for (var block : prev) {
            for (var domSet : domMap.get(block.label).domSetList) {
                if (!intersection.containsKey(domSet.blockName)) {
                    intersection.put(domSet.blockName, 1);
                } else {
                    intersection.put(domSet.blockName, intersection.get(domSet.blockName) + 1);
                }
            }
        }
        boolean newDom = true;
        for (var entry : intersection.entrySet()) {
            if (entry.getValue() == prev.size()) {
                newDom = true;
                for (var domSet : domMap.get(nowLabel).domSetList) {
                    if (Objects.equals(domSet.blockName, entry.getKey())) {
                        newDom = false;
                        break;
                    }
                }
                if (newDom) {
                    flag = true;
                    domMap.get(nowLabel).domSetList.add(domMap.get(entry.getKey()));
                }
            }
        }
        return flag;
    }

}
