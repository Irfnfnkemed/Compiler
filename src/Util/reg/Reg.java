package src.Util.reg;

import src.ASM.Section;
import src.ASM.instruction.LW;
import src.ASM.instruction.MV;
import src.IR.statement.FuncDef;

import java.util.*;

public class Reg {
    public TmpVarScope tmpVarScope;
    public ASMStack asmStack;
    public int nowId = 0;//现在处理的指令id
    public boolean[] savedRegister;//s0-s11寄存器，false为空闲
    public boolean[] tmpRegister;//t0-t6寄存器，false为空闲
    public HashMap<String, String> getTmpVar;//临时变量->寄存器
    public List<Integer> freeSaveReg;//空闲的s1-s11寄存器
    public HashSet<String> isHeap;//指向堆空间的指针变量
    public Section section;

    public Reg(Section section_) {
        tmpVarScope = new TmpVarScope();
        savedRegister = new boolean[12];
        tmpRegister = new boolean[7];
        Arrays.fill(savedRegister, false);
        Arrays.fill(tmpRegister, false);
        getTmpVar = new HashMap<>();
        freeSaveReg = new LinkedList<>();
        for (int i = 11; i > 0; --i) {
            freeSaveReg.add(i);
        }
        isHeap = new HashSet<>();
        section = section_;
    }

    public int setStack(int allocaSize, int regMax, int callParaMax) {
        asmStack = new ASMStack(allocaSize, regMax, callParaMax);
        return asmStack.stackSize;
    }

    public void collect(FuncDef funcDef) {
        tmpVarScope.collect(funcDef);
    }

    public void setTmp() {//分配临时变量占据的寄存器/空间
        while (true) {
            var change = tmpVarScope.getChangeReg(nowId);
            if (change == null || !change.newVar) {
                break;
            }
            ++tmpVarScope.nowIndex;
            if (freeSaveReg.size() > 0) {
                getTmpVar.put(change.varName, getSavedReg());//存于寄存器
            } else {
                asmStack.setVar(change.varName);//存于内存
            }
        }
    }

    public void clearTmp() {//清除t0-t7寄存器
        Arrays.fill(tmpRegister, false);
    }

    public void freeTmp() {//释放临时变量占据的寄存器/空间
        while (true) {
            var change = tmpVarScope.getChangeReg(nowId);
            if (change == null || change.newVar) {
                break;
            }
            ++tmpVarScope.nowIndex;
            if (getTmpVar.containsKey(change.varName)) {
                freeSavedReg(getTmpVar.remove(change.varName));//释放寄存器
            } else {
                asmStack.freeVar(change.varName);//释放内存
            }
        }
    }

    public String getVarReg(String varName) {
        var reg = getTmpVar.get(varName);
        if (reg != null) {//变量在寄存器中
            return reg;
        } else {
            int pos = asmStack.getVar(varName);
            String tmp = getTmpReg();
            section.pushInstr(new LW(tmp, pos));
            return tmp;
        }
    }

    public void getVarReg(String varName, String toReg) {//指定加载到目标寄存器中
        var reg = getTmpVar.get(varName);
        if (reg != null) {//变量在寄存器中
            section.pushInstr(new MV(reg, toReg));
        } else {
            int pos = asmStack.getVar(varName);
            section.pushInstr(new LW(toReg, pos));
        }
    }

    public void setStackVar(String varName) {
        asmStack.setVar(varName);
    }

    public void setStackVar(String varName, int addr) {//指定地址，用于函数的栈传参(addr是相对栈顶偏移，实际上在上一函数的栈内)
        asmStack.getVar.put(varName, addr + asmStack.stackSize);
    }

    public int getStackAddr(String varName) {
        return asmStack.getVar(varName);
    }


    public void setHeap(String varName) {
        isHeap.add(varName);
    }

    public boolean isHeap(String varName) {
        return isHeap.contains(varName);
    }


    private void freeSavedReg(String reg) {//释放s1-s11寄存器
        if (reg.charAt(0) == 's') {
            int id = reg.charAt(1) - '0';
            savedRegister[id] = false;
            freeSaveReg.add(id);
        }
    }

    private String getSavedReg() {//得到空闲s1-s11寄存器
        int reg = freeSaveReg.remove(freeSaveReg.size() - 1);
        savedRegister[reg] = true;
        return "s" + reg;
    }

    public String getTmpReg() {//得到空闲t0-t6寄存器
        for (int i = 0; i < 7; ++i) {
            if (!tmpRegister[i]) {
                tmpRegister[i] = true;
                return "t" + i;
            }
        }
        return null;
    }

    public boolean isInReg(String varName) {
        return getTmpVar.containsKey(varName);
    }
}