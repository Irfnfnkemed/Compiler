package src.Util.reg;

import src.IR.statement.FuncDef;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Reg {
    public TmpVarScope tmpVarScope;
    public int nowId = 0;//现在处理的指令id
    public boolean[] savedRegister;//s0-s11寄存器，false为空闲
    public HashMap<String, Integer> getTmpVar;//临时变量->寄存器编号
    public List<Integer> freeSaveReg;//空闲的s1-s11寄存器

    public Reg() {
        tmpVarScope = new TmpVarScope();
        savedRegister = new boolean[12];
        Arrays.fill(savedRegister, false);
        getTmpVar = new HashMap<>();
        freeSaveReg = new LinkedList<>();
        for (int i = 12; i > 0; --i) {
            freeSaveReg.add(i);
        }
    }

    public void collect(FuncDef funcDef) {
        tmpVarScope.collect(funcDef);
    }

    public void changeReg() {
        while (true) {
            var change = tmpVarScope.changeReg(nowId);
            if (change == null) {
                break;
            }
            if (change.newVar) {
                getTmpVar.put(change.varName, getReg());
            } else {
                freeReg(getTmpVar.remove(change.varName));
            }
        }
    }

    public void freeReg(int reg) {//释放s1-s11寄存器
        savedRegister[reg] = false;
        freeSaveReg.add(reg);
    }

    public int getReg() {//得到空闲s1-s11寄存器
        int reg = freeSaveReg.remove(freeSaveReg.size() - 1);
        savedRegister[reg] = true;
        return reg;
    }

    public String getVarReg(String varName) {
        return "s" + getTmpVar.get(varName);
    }
}
