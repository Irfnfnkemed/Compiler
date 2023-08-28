package src.ASM.instruction;

abstract public class ASMInstr {
    public String[] use = new String[2];
    public String def;
    public int useNum = 0;
    public boolean notRemove = false;//在活跃分析时，不会被去除(主要用于最后ret)
    public boolean preColored = false;//是否是预着色的
}
