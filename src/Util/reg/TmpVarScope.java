package src.Util.reg;

import src.IR.instruction.*;
import src.IR.statement.FuncDef;

import java.util.*;

public class TmpVarScope {
    public static class change {
        public String varName;
        public int id;
        public boolean newVar;//为true，表示开始使用；反之，表示失效

        public change(String varName_, int id_, boolean newVar_) {
            varName = varName_;
            id = id_;
            newVar = newVar_;
        }
    }

    public static class Cmp implements Comparator<change> {
        @Override
        public int compare(change o1, change o2) {
            if (o1.id != o2.id) {
                return o1.id < o2.id ? -1 : 1;
            } else if (o1.newVar != o2.newVar) {
                return o2.newVar ? -1 : 1;
            } else {
                return o1.varName.compareTo(o2.varName);
            }
        }
    }

    public HashMap<String, Integer> beg;

    public HashMap<String, Integer> end;
    public HashSet<String> except;
    public List<change> changeList;
    public static Cmp cmp;
    public int max = 0;//表示临时变量最多需要的同时使用的虚拟寄存器的数量
    int nowIndex = 0;//表示下一条寄存器分配变化(changeList)的下标

    public TmpVarScope() {
        beg = new HashMap<>();
        end = new HashMap<>();
        except = new HashSet<>();
        changeList = new ArrayList<>();
        cmp = new Cmp();
        except.add("%this");
    }

    public void setExcept(String varName) {
        except.add(varName);
    }

    public void setBeg(String var, int id) {
        if (!except.contains(var) && var.charAt(0) != '@') {
            if (beg.containsKey(var)) {
                setEnd(var, id);
            } else {
                beg.put(var, id);
            }
        }
    }

    public void setEnd(String var, int id) {
        if (!except.contains(var) && var.charAt(0) != '@') {
            if (!beg.containsKey(var)) {
                setBeg(var, id);
            } else {
                end.put(var, id);
            }
        }
    }

    public void collect(FuncDef funcDef) {
        int i = 0;
        if (funcDef.isClassMethod) {
            for (int j = 0; j < funcDef.parameterTypeList.size() - 1; ++j) {
                setExcept("%" + j);
            }
        } else {
            for (int j = 0; j < funcDef.parameterTypeList.size(); ++j) {
                setExcept("%" + j);
            }
        }
        for (var inst : funcDef.irList) {
            if (inst.tmpVarScopeEnd != null) {
                for (var tmpVar : inst.tmpVarScopeEnd) {
                    setEnd(tmpVar, i);
                }
            }
            if (inst instanceof Alloca) {
                setExcept(((Alloca) inst).varName);
            } else if (inst instanceof Binary) {
                if (((Binary) inst).operandLeft != null) {
                    setEnd(((Binary) inst).operandLeft, i);
                }
                if (((Binary) inst).operandRight != null) {
                    setEnd(((Binary) inst).operandRight, i);
                }
                setBeg(((Binary) inst).output, i);
            } else if (inst instanceof Br) {
                if (((Br) inst).condition != null) {
                    setEnd(((Br) inst).condition, i);
                }
                var phi = (((Br) inst).funcDef.phiList.get(((Br) inst).nowLabel));
                if (phi != null) {
                    setBeg(phi.toVar, i);
                }
            } else if (inst instanceof Call) {
                for (var varName : ((Call) inst).varNameList) {
                    setEnd(varName, i);
                }
                if (((Call) inst).resultVar != null) {
                    setBeg(((Call) inst).resultVar, i);
                }
            } else if (inst instanceof Getelementptr) {
                if (((Getelementptr) inst).indexVar != null) {
                    setEnd(((Getelementptr) inst).indexVar, i);
                }
                setEnd(((Getelementptr) inst).from, i);
                setBeg(((Getelementptr) inst).result, i);
            } else if (inst instanceof Icmp) {
                if (((Icmp) inst).operandLeft != null) {
                    setEnd(((Icmp) inst).operandLeft, i);
                }
                if (((Icmp) inst).operandRight != null) {
                    setEnd(((Icmp) inst).operandRight, i);
                }
                setBeg(((Icmp) inst).output, i);
            } else if (inst instanceof Load) {
                setEnd(((Load) inst).fromPointer, i);
                setBeg(((Load) inst).toVarName, i);
            } else if (inst instanceof Phi) {
                for (var assignBlock : ((Phi) inst).assignBlockList) {
                    if (assignBlock.var != null) {
                        setEnd(assignBlock.var, i);
                    }
                }
                setBeg(((Phi) inst).result, i);
            } else if (inst instanceof Ret) {
                if (((Ret) inst).var != null) {
                    setEnd(((Ret) inst).var, i);
                }
            } else if (inst instanceof Store) {
                if (((Store) inst).valueVar != null) {
                    setEnd(((Store) inst).valueVar, i);
                }
                setEnd(((Store) inst).toPointer, i);
            }
            ++i;
        }
        sort();
    }

    public void sort() {
        beg.forEach((varName, begId) -> changeList.add(new change(varName, begId, true)));
        end.forEach((varName, endId) -> changeList.add(new change(varName, endId, false)));
        changeList.sort(cmp);
        int now = 0;
        for (var change : changeList) {
            if (change.newVar) {
                ++now;
            } else {
                --now;
            }
            if (now > max) {
                max = now;
            }
        }
    }

    public change getChangeReg(int nowId) {
        if (changeList.size() > nowIndex && changeList.get(nowIndex).id == nowId) {
            return changeList.get(nowIndex);
        } else {
            return null;
        }
    }

}
