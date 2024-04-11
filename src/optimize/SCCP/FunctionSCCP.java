package src.optimize.SCCP;

import src.IR.instruction.*;
import src.IR.statement.FuncDef;
import src.optimize.SCCP.SCCPNode.EdgeNode;
import src.optimize.SCCP.SCCPNode.SCCPNode;
import src.optimize.SCCP.SCCPNode.SSANode;

import java.util.*;

public class FunctionSCCP {
    public Queue<SCCPNode> workList;
    public HashMap<String, Integer> blockPos;//块名->块的label下标
    public HashSet<String> edgeVisited;// from块名+'#'+to块名
    public HashSet<String> blockVisited;
    public HashMap<String, List<Instruction>> SSAInstr;//变量名->use
    public HashSet<Instruction> SSAPutAlready;
    public HashMap<String, LatticeCell> SSALattice;
    public FuncDef funcDef;

    public FunctionSCCP(FuncDef funcDef_) {
        workList = new ArrayDeque<>();
        blockPos = new HashMap<>();
        edgeVisited = new HashSet<>();
        blockVisited = new HashSet<>();
        SSAInstr = new HashMap<>();
        SSAPutAlready = new HashSet<>();
        SSALattice = new HashMap<>();
        funcDef = funcDef_;
        setInfo();
        sccp();
        recollect();
    }

    private void setInfo() {
        // 初始化参数
        int size = funcDef.parameterTypeList.size();
        if (size > 0) {
            if (funcDef.isClassMethod) {
                findLattice("%this").status = LatticeCell.UNCERTAIN_NEW;
            } else {
                findLattice("%_0").status = LatticeCell.UNCERTAIN_NEW;
            }
        }
        for (int i = 1; i < size; ++i) {
            if (funcDef.isClassMethod) {
                findLattice("%_" + (i - 1)).status = LatticeCell.UNCERTAIN_NEW;
            } else {
                findLattice("%_" + i).status = LatticeCell.UNCERTAIN_NEW;
            }
        }
        for (int i = 0; i < funcDef.irList.size(); ++i) {
            Instruction instr = funcDef.irList.get(i);
            if (instr instanceof Label) {
                blockPos.put(((Label) instr).labelName, i + 1);
            }
        }
    }

    private void putSSA(String varName, Instruction instr) {
        if (varName == null || SSAPutAlready.contains(instr)) {
            return;
        }
        SSAInstr.computeIfAbsent(varName, k -> new ArrayList<>()).add(instr);
    }

    private LatticeCell findLattice(String varName) {
        if (varName == null) {
            return null;
        }
        LatticeCell tmp = SSALattice.get(varName);
        if (tmp == null) {
            SSALattice.put(varName, tmp = new LatticeCell(varName));
        }
        return tmp;
    }

    private void sccp() {
        workList.add(new EdgeNode(null, "entry"));
        while (!workList.isEmpty()) {
            SCCPNode now = workList.poll();
            if (now instanceof EdgeNode) {
                String mark = ((EdgeNode) now).fromBlock + '#' + ((EdgeNode) now).toBlock;
                if (!edgeVisited.contains(mark)) {
                    edgeVisited.add(mark);
                    int pos = blockPos.get(((EdgeNode) now).toBlock);
                    while (true) {
                        var tmp = funcDef.irList.get(pos);
                        if (tmp instanceof Phi) {
                            visitPhi((Phi) tmp);
                            ++pos;
                        } else {
                            break;
                        }
                    }
                    if (!blockVisited.contains(((EdgeNode) now).toBlock)) {
                        blockVisited.add(((EdgeNode) now).toBlock);
                        while (true) {
                            var instr = funcDef.irList.get(pos);
                            if (instr instanceof Br) {
                                visitBr((Br) instr);
                                break;
                            }
                            if (instr instanceof Ret) {
                                break;
                            } else {
                                visitInstr(instr);
                                ++pos;
                            }
                        }
                    }
                }
            } else if (now instanceof SSANode) {
                var useList = SSAInstr.get(((SSANode) now).varName);
                if (useList != null) {
                    for (var instr : useList) {
                        if (instr instanceof Phi) {
                            visitPhi((Phi) instr);
                        } else if (instr instanceof Br) {
                            visitBr((Br) instr);
                        } else {
                            visitInstr(instr);
                        }
                    }
                }
            }
        }
    }

    private void visitPhi(Phi phi) {
        for (var assign : phi.assignBlockList) {
            putSSA(assign.var, phi);
        }
        SSAPutAlready.add(phi);
        var lattice = findLattice(phi.result);
        boolean flag = false;
        for (var assign : phi.assignBlockList) {
            if (assign.var == null) {
                if (lattice.update(new LatticeCell(assign.value))) {
                    flag = true;
                }
            } else {
                if (lattice.update(findLattice(assign.var))) {
                    flag = true;
                }
            }
        }
        if (flag) {
            workList.add(new SSANode(phi.result));
        }
    }

    private void visitInstr(Instruction instr) {
        if (instr instanceof Binary) {
            putSSA(((Binary) instr).operandLeft, instr);
            putSSA(((Binary) instr).operandRight, instr);
            var lattice = findLattice(((Binary) instr).output);
            var lattice_lhs = ((Binary) instr).operandLeft == null ?
                    new LatticeCell(((Binary) instr).valueLeft) : findLattice(((Binary) instr).operandLeft);
            var lattice_rhs = ((Binary) instr).operandRight == null ?
                    new LatticeCell(((Binary) instr).valueRight) : findLattice(((Binary) instr).operandRight);
            if (lattice.update(lattice_lhs, lattice_rhs, ((Binary) instr).op)) {
                workList.add(new SSANode(((Binary) instr).output));
            }
        } else if (instr instanceof Icmp) {
            putSSA(((Icmp) instr).operandLeft, instr);
            putSSA(((Icmp) instr).operandRight, instr);
            var lattice = findLattice(((Icmp) instr).output);
            var lattice_lhs = ((Icmp) instr).operandLeft == null ?
                    new LatticeCell(((Icmp) instr).valueLeft) : findLattice(((Icmp) instr).operandLeft);
            var lattice_rhs = ((Icmp) instr).operandRight == null ?
                    new LatticeCell(((Icmp) instr).valueRight) : findLattice(((Icmp) instr).operandRight);
            if (lattice.update(lattice_lhs, lattice_rhs, ((Icmp) instr).cond)) {
                workList.add(new SSANode(((Icmp) instr).output));
            }
        } else if (instr instanceof Load) {
            putSSA(((Load) instr).fromPointer, instr);
            findLattice(((Load) instr).toVarName).status = LatticeCell.UNCERTAIN_NEW;
        } else if (instr instanceof Store) {
            putSSA(((Store) instr).valueVar, instr);
            putSSA(((Store) instr).toPointer, instr);
        } else if (instr instanceof Call) {
            for (var para : ((Call) instr).callList) {
                putSSA(para.varName, instr);
            }
            if (((Call) instr).resultVar != null) {
                findLattice(((Call) instr).resultVar).status = LatticeCell.UNCERTAIN_NEW;
            }
        } else if (instr instanceof Getelementptr) {
            putSSA(((Getelementptr) instr).from, instr);
            putSSA(((Getelementptr) instr).indexVar, instr);
            findLattice(((Getelementptr) instr).result).status = LatticeCell.UNCERTAIN_NEW;
        }
        SSAPutAlready.add(instr);
    }

    private void visitBr(Br br) {
        putSSA(br.condition, br);
        SSAPutAlready.add(br);
        if (br.condition == null) {
            workList.add(new EdgeNode(br.nowLabel.substring(1), br.trueLabel.substring(1)));
        } else {
            var lattice = findLattice(br.condition);
            if (lattice.status == LatticeCell.CONST) {
                if (lattice.constValue == 0) {
                    workList.add(new EdgeNode(br.nowLabel.substring(1), br.falseLabel.substring(1)));
                } else {
                    workList.add(new EdgeNode(br.nowLabel.substring(1), br.trueLabel.substring(1)));
                }
            } else {
                workList.add(new EdgeNode(br.nowLabel.substring(1), br.falseLabel.substring(1)));
                workList.add(new EdgeNode(br.nowLabel.substring(1), br.trueLabel.substring(1)));
            }
        }
    }

    private void recollect() {
        boolean flag = true;
        List<Instruction> newList = new ArrayList<>();
        for (Instruction instr : funcDef.irList) {
            if (instr instanceof Label) {
                if (blockVisited.contains(((Label) instr).labelName)) {
                    newList.add(instr);
                    flag = true;
                } else {
                    flag = false;
                }
            } else if (!flag) {
                continue;
            }
            if (instr instanceof Phi) {
                if (!findLattice(((Phi) instr).result).removed()) {
                    newList.add(instr);
                    for (var assign : ((Phi) instr).assignBlockList) {
                        if (assign.var != null) {
                            var lattice = findLattice(assign.var);
                            if (lattice.status == LatticeCell.CONST) {
                                assign.var = null;
                                assign.value = lattice.constValue;
                            } else {
                                assign.var = lattice.getVar();
                            }
                        }
                    }
                }
            } else if (instr instanceof Binary) {
                if (!findLattice(((Binary) instr).output).removed()) {
                    newList.add(instr);
                    if (((Binary) instr).operandLeft != null) {
                        var lattice = findLattice(((Binary) instr).operandLeft);
                        if (lattice.status == LatticeCell.CONST) {
                            ((Binary) instr).operandLeft = null;
                            ((Binary) instr).valueLeft = lattice.constValue;
                        } else {
                            ((Binary) instr).operandLeft = lattice.getVar();
                        }
                    }
                    if (((Binary) instr).operandRight != null) {
                        var lattice = findLattice(((Binary) instr).operandRight);
                        if (lattice.status == LatticeCell.CONST) {
                            ((Binary) instr).operandRight = null;
                            ((Binary) instr).valueRight = lattice.constValue;
                        } else {
                            ((Binary) instr).operandRight = lattice.getVar();
                        }
                    }
                }
            } else if (instr instanceof Icmp) {
                if (!findLattice(((Icmp) instr).output).removed()) {
                    newList.add(instr);
                    if (((Icmp) instr).operandLeft != null) {
                        var lattice = findLattice(((Icmp) instr).operandLeft);
                        if (lattice.status == LatticeCell.CONST) {
                            ((Icmp) instr).operandLeft = null;
                            ((Icmp) instr).valueLeft = lattice.constValue;
                        } else {
                            ((Icmp) instr).operandLeft = lattice.getVar();
                        }
                    }
                    if (((Icmp) instr).operandRight != null) {
                        var lattice = findLattice(((Icmp) instr).operandRight);
                        if (lattice.status == LatticeCell.CONST) {
                            ((Icmp) instr).operandRight = null;
                            ((Icmp) instr).valueRight = lattice.constValue;
                        } else {
                            ((Icmp) instr).operandRight = lattice.getVar();
                        }
                    }
                }
            } else if (instr instanceof Load) {
                if (!findLattice(((Load) instr).toVarName).removed()) {
                    newList.add(instr);
                    ((Load) instr).fromPointer = findLattice(((Load) instr).fromPointer).getVar();
                }
            } else if (instr instanceof Store) {
                newList.add(instr);
                if (((Store) instr).valueVar != null) {
                    var lattice = findLattice(((Store) instr).valueVar);
                    if (lattice.status == LatticeCell.CONST) {
                        ((Store) instr).value = lattice.constValue;
                        ((Store) instr).valueVar = null;
                    } else {
                        ((Store) instr).valueVar = lattice.getVar();
                    }
                    ((Store) instr).toPointer = findLattice(((Store) instr).toPointer).getVar();
                }
            } else if (instr instanceof Call) {
                newList.add(instr);
                for (var para : ((Call) instr).callList) {
                    if (para.varName != null) {
                        var lattice = findLattice(para.varName);
                        if (lattice.status == LatticeCell.CONST) {
                            para.varName = null;
                            para.varValue = lattice.constValue;
                        } else {
                            para.varName = lattice.getVar();
                        }
                    }
                }
            } else if (instr instanceof Getelementptr) {
                if (!findLattice(((Getelementptr) instr).result).removed()) {
                    newList.add(instr);
                    if (((Getelementptr) instr).indexVar != null) {
                        var lattice = findLattice(((Getelementptr) instr).indexVar);
                        if (lattice.status == LatticeCell.CONST) {
                            ((Getelementptr) instr).indexVar = null;
                            ((Getelementptr) instr).indexValue = (int) lattice.constValue;
                        } else {
                            ((Getelementptr) instr).indexVar = lattice.getVar();
                        }
                    }
                    ((Getelementptr) instr).from = findLattice(((Getelementptr) instr).from).getVar();
                }
            } else if (instr instanceof Ret) {
                newList.add(instr);
                if (((Ret) instr).var != null) {
                    var lattice = findLattice(((Ret) instr).var);
                    if (lattice.status == LatticeCell.CONST) {
                        ((Ret) instr).var = null;
                        ((Ret) instr).value = (int) lattice.constValue;
                    } else {
                        ((Ret) instr).var = lattice.getVar();
                    }
                }
            } else if (instr instanceof Br) {
                newList.add(instr);
                var lattice = findLattice(((Br) instr).condition);
                if (lattice != null && lattice.status == LatticeCell.CONST) {
                    if (lattice.constValue == 0) {
                        ((Br) instr).trueLabel = ((Br) instr).falseLabel;
                    }
                    ((Br) instr).condition = null;
                }
            }
        }
        funcDef.irList = newList;
    }
}