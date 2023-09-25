package src.optimize.LoopInvariant;

import src.IR.instruction.*;
import src.IR.statement.FuncDef;

import java.util.*;


public class LoopInvariant {
    public static class Loop {
        public FuncDef funcDef;//当前funcDef
        public Br br;//结束时的指令(br)，用于后续插入
        public Queue<Integer> subBlock;//循环体中的块的起始位置
        public boolean hasFatherLoop = false;//本身是否在循环中

        public Loop(Br br_, FuncDef funcDef_) {
            br = br_;
            funcDef = funcDef_;
            subBlock = new ArrayDeque<>();
        }
    }

    public List<Loop> allLoop;
    Stack<Integer> loopPos;
    HashSet<String> notLoopInvariant;//不是循环不变量的变量
    HashSet<String> defInLoop;//在loop中store的变量
    HashMap<String, HashSet<String>> useGlobalVar;//函数名->用到的全局变量

    public LoopInvariant(HashMap<String, HashSet<String>> useGlobalVar_) {
        allLoop = new ArrayList<>();
        loopPos = new Stack<>();
        notLoopInvariant = new HashSet<>();
        defInLoop = new HashSet<>();
        useGlobalVar = useGlobalVar_;
    }

    public void setLoopEntry(Br br, FuncDef funcDef) {
        Loop loop = new Loop(br, funcDef);
        loop.hasFatherLoop = !loopPos.empty();
        allLoop.add(loop);
        loopPos.push(allLoop.size() - 1);
    }

    public void setLoopEnd() {
        loopPos.pop();
    }

    public void addSubBlock(int pos) {
        allLoop.get(loopPos.peek()).subBlock.add(pos);
    }


    public void moveLoopInvariant() {
        Instruction instr;
        Loop loop;
        for (int i = allLoop.size() - 1; i >= 0; --i) {
            loop = allLoop.get(i);
            for (int pos : loop.subBlock) {
                while (true) {
                    instr = loop.funcDef.irList.get(pos++);
                    if (instr instanceof Label) {
                        break;
                    }
                    if (instr instanceof Store) {
                        defInLoop.add(((Store) instr).toPointer);
                    }
                }
            }
            while (!loop.subBlock.isEmpty()) {
                int pos = loop.subBlock.poll();
                while (true) {
                    instr = loop.funcDef.irList.get(pos++);
                    if (instr instanceof Label) {
                        break;
                    }
                    judgeLoopInvariant(instr, loop.br);
                }
            }
            if (!loop.hasFatherLoop) {
                notLoopInvariant.clear();
                defInLoop.clear();
            }
        }
    }

    public void judgeLoopInvariant(Instruction instr, Br br) {
        if (instr instanceof Binary) {
            if (judgeInvariant(((Binary) instr).operandLeft) && judgeInvariant(((Binary) instr).operandRight)) {
                br.pushCache(instr);
            } else {
                notLoopInvariant.add(((Binary) instr).output);
            }
        } else if (instr instanceof Icmp) {
            if (judgeInvariant(((Icmp) instr).operandLeft) && judgeInvariant(((Icmp) instr).operandRight)) {
                br.pushCache(instr);
            } else {
                notLoopInvariant.add(((Icmp) instr).output);
            }
        } else if (instr instanceof Call) {
            notLoopInvariant.add(((Call) instr).resultVar);
            var func = useGlobalVar.get(((Call) instr).functionName.substring(1));
            if (func != null) {
                for (var globalVar : func) {
                    defInLoop.add("@" + globalVar);
                }
            }
        } else if (instr instanceof Getelementptr) {
            notLoopInvariant.add(((Getelementptr) instr).result);
        } else if (instr instanceof Phi) {
            notLoopInvariant.add(((Phi) instr).result);
        } else if (instr instanceof Load) {
            if (defInLoop.contains(((Load) instr).fromPointer) || notLoopInvariant.contains(((Load) instr).fromPointer)) {
                notLoopInvariant.add(((Load) instr).toVarName);
            } else {
                br.pushCache(instr);
            }
        } else if (instr instanceof Br) {
            if (instr.cache != null) {
                for (var instrCache : instr.cache) {
                    judgeLoopInvariant(instrCache, br);
                }
            }
        }
    }

    public boolean judgeInvariant(String var) {
        if (var == null) {
            return true;
        }
        return !notLoopInvariant.contains(var);
    }

    public boolean isLoop() {
        return !loopPos.empty();
    }
}
