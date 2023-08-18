package src.Util.reg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ASMStack {
    public int stackSize;//字节数
    public HashMap<String, Integer> getVar;//变量->存放地址
    public int nowPos;
    public List<Integer> freePos;//已经释放的位置

    public ASMStack(int allocaSize, int regMax, int callParaMax, int savedReg, boolean call) {
        //需要的栈字节数，是16的倍数（+3是为了向上取整）
        int callStackMax = callParaMax > 8 ? callParaMax - 8 : 0;
        int regStackMax = regMax > 11 ? regMax - 11 : 0;
        int callee = savedReg + (call ? 1 : 0);
        stackSize = ((allocaSize + regStackMax + callStackMax + callee + 3) >> 2) << 4;
        getVar = new HashMap<>();
        nowPos = stackSize - 52;//除去ra,s1-s11
        freePos = new ArrayList<>();
    }

    public void setVar(String varName) {
        if (freePos.size() > 0) {
            getVar.put(varName, freePos.remove(freePos.size() - 1));
        } else {
            getVar.put(varName, nowPos);
            nowPos -= 4;
        }
    }

    public int getVar(String varName) {
        return getVar.get(varName);
    }

    public void freeVar(String varName) {
        freePos.add(getVar.get(varName));
    }
}
