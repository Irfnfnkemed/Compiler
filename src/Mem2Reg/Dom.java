package src.mem2Reg;

import java.util.*;

import static java.lang.Math.min;

public class Dom {
    private int cnt = 0;

    public class DomInfo {//节点的支配集、直接支配节点和支配边界
        public String blockName;
        public DomInfo immeDom;//直接支配节点
        public DomInfo semiDom;//半支配节点
        public HashSet<String> domFrontier;//支配边界
        public Stack<DomInfo> semiBucket;//被当前节点半支配的节点
        public int dfn = -1;
        public DomInfo dfsFather;

        public DomInfo(String blockName_, DomInfo dfsFather_) {
            blockName = blockName_;
            semiBucket = new Stack<>();
            domFrontier = new HashSet<>();
            dfsFather = dfsFather_;
            semiDom = this;//半支配节点设为自身
            dfn = cnt++;
            dfnList.add(this);
        }
    }

    public HashMap<String, DomInfo> domMap;
    public List<DomInfo> dfnList;//按照dfn从小到大排列
    public int[] fatherDSU;//dfn->并查集中的fa
    public int[] minSdomDfn;//dfn->(对应节点 到 逆dfn序遍历dfs树过程中当前遍历到所有点的LCA 的路径上，sdom的dfn最小的点的dfn)
    public CFG cfg;

    public Dom(CFG cfg_) {
        cfg = cfg_;
        domMap = new HashMap<>();
        dfnList = new ArrayList<>();
        DFS();
        int size = dfnList.size();
        fatherDSU = new int[size];
        minSdomDfn = new int[size];
        Arrays.fill(fatherDSU, -1);
        for (int i = 0; i < size; ++i) {
            minSdomDfn[i] = i;
        }
        LengauerTarjan();
        buildDomFrontier();
    }

    public void DFS() {
        DomInfo rootDom = new DomInfo("entry", null);
        domMap.put(rootDom.blockName, rootDom);
        DFS(rootDom);
    }

    public void DFS(DomInfo domInfo) {
        Block block = cfg.funcBlocks.get(domInfo.blockName);
        for (int i = 0; i < block.suc; ++i) {
            if (!domMap.containsKey(block.next[i].label)) {
                DomInfo nextDomInfo = new DomInfo(block.next[i].label, domInfo);
                domMap.put(block.next[i].label, nextDomInfo);
                DFS(nextDomInfo);
            }
        }
    }

    public int find(int now) {
        if (fatherDSU[now] == -1) {
            return now;
        }
        int tmp = fatherDSU[now];
        fatherDSU[now] = find(fatherDSU[now]);
        if (minSdomDfn[tmp] < minSdomDfn[now]) {
            minSdomDfn[now] = minSdomDfn[tmp];
        }
        return fatherDSU[now];
    }

    public void LengauerTarjan() {
        Block nowBlock;
        DomInfo nowDom, tmpDom;
        for (int i = dfnList.size() - 1; i > 0; --i) {//逆dfn序
            nowDom = dfnList.get(i);
            nowBlock = cfg.funcBlocks.get(nowDom.blockName);
            for (var preBlock : nowBlock.prev) {//求半支配节点
                tmpDom = domMap.get(preBlock.label);
                if (tmpDom.dfn < nowDom.dfn) {
                    if (tmpDom.semiDom.dfn < nowDom.semiDom.dfn) {
                        nowDom.semiDom = tmpDom.semiDom;
                    }
                } else {
                    find(tmpDom.dfn);
                    if (minSdomDfn[tmpDom.dfn] < nowDom.semiDom.dfn) {
                        nowDom.semiDom = dfnList.get(minSdomDfn[tmpDom.dfn]);
                    }
                }
            }
            (nowDom.semiDom).semiBucket.push(nowDom);
            fatherDSU[nowDom.dfn] = nowDom.dfsFather.dfn;
            while (!nowDom.dfsFather.semiBucket.isEmpty()) {
                tmpDom = nowDom.dfsFather.semiBucket.pop();
                find(tmpDom.dfn);
                if (dfnList.get(minSdomDfn[tmpDom.dfn]).semiDom.dfn == tmpDom.semiDom.dfn) {
                    tmpDom.immeDom = tmpDom.semiDom;
                } else {
                    //实际上，应该是dfnList.get(minSdomDfn[tmpDom.dfn]).immeDom，但由于逆序操作，immeDom还未算出，故用其本身暂代
                    tmpDom.immeDom = dfnList.get(minSdomDfn[tmpDom.dfn]);
                }
            }
        }
        for (int i = 1; i < dfnList.size(); ++i) {
            nowDom = dfnList.get(i);
            if (nowDom.immeDom != nowDom.semiDom) {
                nowDom.immeDom = nowDom.immeDom.immeDom;
            }
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
