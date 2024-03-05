package src.optimize.ADCE;

import src.IR.instruction.*;
import src.IR.statement.FuncDef;
import src.optimize.Mem2Reg.Dom;
import src.optimize.Mem2Reg.PutPhi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FunctionADCE {
    public HashSet<String> domBr;//from+‘->'+to
    public HashMap<String, Instruction> defSet;//变量名->定义
    public List<Instruction> activeInstr;
    public Dom dom;
    public FuncDef funcDef;

    public FunctionADCE(Dom dom_) {
        domBr = new HashSet<>();
        activeInstr = new ArrayList<>();
        dom = dom_;
        funcDef = dom_.cfgDom.funcDef;
        setDomBr();
        setDefAndActive();
    }

    public void setDomBr() {
        for (var info : dom.domMap.values()) {
            for (String from : info.domFrontier) {
                domBr.add(from + "->" + info.blockName);
            }
        }
    }

    public void setDefAndActive() {
        for (var instr : funcDef.irList) {
            if (instr instanceof Binary) {
                defSet.put(((Binary) instr).output, instr);
            } else if (instr instanceof Icmp) {
                defSet.put(((Icmp) instr).output, instr);
            } else if (instr instanceof Load) {
                defSet.put(((Load) instr).toVarName, instr);
            } else if (instr instanceof Store) {
                activeInstr.add(instr);
            } else if (instr instanceof Call) {
                if (((Call) instr).resultVar != null) {
                    defSet.put(((Call) instr).resultVar, instr);
                }
                activeInstr.add(instr);
            } else if (instr instanceof Phi) {
                defSet.put(((Phi) instr).result, instr);
            } else if (instr instanceof Getelementptr) {
                defSet.put(((Getelementptr) instr).result, instr);
            } else if (instr instanceof Ret) {
                activeInstr.add(instr);
            }
        }
    }

}
