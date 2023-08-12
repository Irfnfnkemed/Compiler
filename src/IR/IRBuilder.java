package src.IR;

import src.AST.ASTVisitor;
import src.AST.Program;
import src.AST.definition.*;
import src.AST.definition.variableDef.InitVariable;
import src.AST.definition.variableDef.VariableDef;
import src.AST.expression.*;
import src.AST.statement.Statement;
import src.AST.statement.Suite;
import src.AST.statement.jumpStatement.BreakStmt;
import src.AST.statement.jumpStatement.ContinueStmt;
import src.AST.statement.jumpStatement.ReturnStmt;
import src.AST.statement.loopStatement.ForLoop;
import src.AST.statement.loopStatement.WhileLoop;
import src.AST.statement.selectStatement.SelectStatement;
import src.IR.instruction.*;
import src.IR.instruction.Binary;
import src.IR.statement.ClassTypeDef;
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;
import src.Util.scope.GlobalScope;
import src.Util.type.IRType;
import src.Util.type.Type;

import java.util.Objects;

public class IRBuilder implements ASTVisitor {
    public IRProgram irProgram;
    public GlobalScope globalScope;
    public FuncDef funcMain;
    public IRNode now;
    public int anonymousVar = 0;
    public int anonymousLabel = 0;

    public IRBuilder(Program node, GlobalScope globalScope_) {
        globalScope = globalScope_;
        irProgram = new IRProgram();
        funcMain = new FuncDef();
        funcMain.irType = new IRType().setI32();
        funcMain.functionName = "@main";
        visit(node);
    }

    @Override
    public void visit(Program node) {
        FuncDef funcDef = new FuncDef();
        funcDef.push(new Label("entry"));
        funcDef.irType = new IRType().setPtr();
        funcDef.functionName = "@.newArray";
        funcDef.parameterTypeList.add(new IRType().setI32());
        Call call = new Call("@.malloc");
        Binary binary = new Binary("+");
        binary.set(1);
        binary.set("%0");
        binary.output = "%1";
        funcDef.push(binary);
        call.set(new IRType().setI32(), "%1");
        call.irType = new IRType().setPtr();
        call.resultVar = "%2";
        funcDef.push(call);
        funcDef.push(new Store(new IRType().setI32(), "%0", "%2"));
        funcDef.push(new Getelementptr("%3", new IRType().setPtr(), "%2", -1, 1));
        funcDef.push(new Ret(new IRType().setPtr(), "%3"));
        irProgram.push(funcDef);
        now = irProgram;
        node.defList.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(Definition node) {
        if (node == null) {
            return;
        }
        if (node.mainDef != null) {
            node.mainDef.accept(this);
        } else if (node.classDef != null) {
            node.classDef.accept(this);
        } else if (node.functionDef != null) {
            node.functionDef.accept(this);
        } else if (node.variableDef != null) {
            node.variableDef.accept(this);
        }
    }

    @Override
    public void visit(MainDef node) {
        IRType irType = new IRType().setI32();
        funcMain.irList.add(0, new Label("entry"));
        ++funcMain.initInsertIndex;
        funcMain.push(new Alloca(irType, "%.returnValue"));
        funcMain.push(new Store(irType, 0, "%.returnValue"));
        anonymousVar = 0;
        anonymousLabel = 0;
        irProgram.push(funcMain);
        var nowTmp = now;
        now = funcMain;
        node.suite.accept(this);
        if (node.scope.notReturn) {
            funcMain.push(new Br("%returnLabel"));
        }
        funcMain.push(new Label("returnLabel"));
        funcMain.push(new Load(irType, "%" + anonymousVar, "%.returnValue"));
        funcMain.push(new Ret(irType, "%" + anonymousVar));
        now = nowTmp;
    }

    @Override
    public void visit(ClassDef node) {
        ClassTypeDef classTypeDef = new ClassTypeDef(node.className, node.variableDefList);
        irProgram.push(classTypeDef);
        if (node.constructor == null) {
            node.constructor = new Constructor();
            node.constructor.className = node.className;
        }
        node.constructor.accept(this);
        node.functionDefList.forEach(functionDef -> {
            functionDef.accept(this);
            var funcDef = (FuncDef) irProgram.stmtList.get(irProgram.stmtList.size() - 1);
            funcDef.functionName = "@" + node.className + "." + functionDef.functionName;
            funcDef.parameterTypeList.add(new IRType().setPtr());
            funcDef.isClassMethod = true;
        });
    }

    @Override
    public void visit(Constructor node) {
        anonymousVar = 0;
        anonymousLabel = 0;
        var nowTmp = now;
        FuncDef funcDef = new FuncDef();
        funcDef.push(new Label("entry"));
        funcDef.isClassMethod = true;
        now = funcDef;
        funcDef.irType = new IRType().setPtr();
        funcDef.functionName = "@init-class-" + node.className;
        Call call = new Call("@.malloc");
        call.irType = new IRType().setPtr();
        call.set(new IRType().setI32(), globalScope.classMemberId.get(node.className).memberNum);
        call.resultVar = "%newClassPtr-" + node.className;
        funcDef.push(call);
        if (node.suite != null) {
            node.suite.accept(this);
            if (node.scope.notReturn) {
                funcDef.push(new Br("%returnLabel"));
            }
        } else {
            funcDef.push(new Br("%returnLabel"));
        }
        funcDef.push(new Label("returnLabel"));
        funcDef.push(new Ret(new IRType().setPtr(), "%newClassPtr-" + node.className));
        now = nowTmp;
        ((IRProgram) now).push(funcDef);
    }

    @Override
    public void visit(FunctionDef node) {
        anonymousVar = 0;
        anonymousLabel = 0;
        var nowTmp = now;
        FuncDef funcDef = new FuncDef();
        funcDef.push(new Label("entry"));
        if (!node.type.isVoid()) {
            funcDef.push(new Alloca(node.type, "%.returnValue"));
            funcDef.push(new Store(node.type, 0, "%.returnValue"));
        }
        now = funcDef;
        funcDef.irType = new IRType(node.type);
        funcDef.functionName = "@" + node.functionName;
        for (int i = 0; i < node.parameterNameList.size(); ++i) {
            funcDef.pushPara(node.parameterTypeList.get(i));
            var varName = var(node.parameterNameList.get(i), node.parameterTypeList.get(i).position.line,
                    node.parameterTypeList.get(i).position.column);
            funcDef.push(new Alloca(node.parameterTypeList.get(i), varName));
            funcDef.push(new Store(node.parameterTypeList.get(i), "%" + anonymousVar++, varName));
        }
        node.body.accept(this);
        if (node.scope.notReturn) {
            funcDef.push(new Br("%returnLabel"));
        }
        funcDef.push(new Label("returnLabel"));
        if (!node.type.isVoid()) {
            funcDef.push(new Load(node.type, "%" + anonymousVar, "%.returnValue"));
            funcDef.push(new Ret(node.type, "%" + anonymousVar));
        } else {
            funcDef.push(new Ret());
        }
        now = nowTmp;
        ((IRProgram) now).push(funcDef);
    }

    @Override
    public void visit(VariableDef node) {
        node.initVariablelist.forEach(varDef -> varDef.accept(this));
    }

    @Override
    public void visit(InitVariable node) {
        var tmpNow = now;
        if (node.scope.isGlobal) {
            GlobalVarDef globalVarDef = new GlobalVarDef();
            irProgram.push(globalVarDef);
            globalVarDef.varName = "@" + node.variableName;
            globalVarDef.irType = new IRType(node.type);
            if (node.exp != null) {
                globalVarDef.setFuncDef();
                globalVarDef.funcDef.push(new Label("entry"));
                anonymousVar = 0;
                now = new Exp(globalVarDef.funcDef);
                node.exp.accept(this);
                if (((Exp) now).isConst) {
                    globalVarDef.funcDef = null;
                    globalVarDef.value = ((Exp) now).popValue();
                } else {
                    globalVarDef.funcDef.push(
                            new Store(node.type, ((Exp) now).popVar(), "@" + node.variableName));
                    globalVarDef.funcDef.push(new Ret());
                    funcMain.irList.add(funcMain.initInsertIndex++, new Call("@init-" + node.variableName));
                    ++funcMain.allocaIndex;
                    irProgram.push(globalVarDef.funcDef);
                }
            } else {
                globalVarDef.value = 0;
            }
        } else {
            ((FuncDef) now).push(new Alloca(node.type,
                    var(node.variableName, node.position.line, node.position.column)));
            if (node.exp != null) {
                Exp exp = new Exp(((FuncDef) now));
                now = exp;
                node.exp.accept(this);
                if (exp.isConst) {
                    ((FuncDef) tmpNow).push(new Store(node.type, exp.popValue(),
                            var(node.variableName, node.position.line, node.position.column)));
                } else {
                    ((FuncDef) tmpNow).push(new Store(node.type, exp.popVar(),
                            var(node.variableName, node.position.line, node.position.column)));
                }
            } else {
                ((FuncDef) tmpNow).push(new Store(node.type, 0L,
                        var(node.variableName, node.position.line, node.position.column)));
            }
        }
        now = tmpNow;
    }

    @Override
    public void visit(Suite node) {
        var tmpIf = ((FuncDef) now).getIf();
        var tmpLoop = ((FuncDef) now).getLoop();
        for (var stmt : node.statementList) {
            stmt.accept(this);
            if (tmpIf != null && ((FuncDef) now).isIf()) {
                if (tmpIf.onTrue) {
                    if (tmpIf.trueJump) {
                        break;
                    }
                } else {
                    if (tmpIf.falseJump) {
                        break;
                    }
                }
            }
            if (tmpLoop != null && !((FuncDef) now).isIf()) {
                if (tmpLoop.jump) {
                    break;
                }
            }
            if (!((FuncDef) now).notReturn) {
                break;
            }
        }
    }

    @Override
    public void visit(Statement node) {
        if (node == null) {
            return;
        }
        if (node.variableDef != null) {
            node.variableDef.accept(this);
        } else if (node.suite != null) {
            node.suite.accept(this);
        } else if (node.jumpStatement != null) {
            node.jumpStatement.accept(this);
        } else if (node.loopStatement != null) {
            node.loopStatement.accept(this);
        } else if (node.selectStatement != null) {
            node.selectStatement.accept(this);
        } else if (node.parallelExp != null) {
            node.parallelExp.accept(this);
        }
    }

    @Override
    public void visit(SelectStatement node) {
        var nowTmp = now;
        Exp exp = new Exp((FuncDef) now);
        now = exp;
        node.judgeExp.accept(this);
        now = nowTmp;
        if (exp.isConst) {
            if (exp.popValue() == 1) {
                node.trueStmt.accept(this);
            } else {
                node.falseStmt.accept(this);
            }
        } else {
            ((FuncDef) now).pushIf();
            var tmpIf = ((FuncDef) now).getIf();
            int tmp = anonymousLabel++;
            if (node.falseStmt == null ||
                    (node.falseStmt.suite != null && node.falseStmt.suite.statementList.size() == 0)) {
                ((FuncDef) now).push(new Br(exp.popVar(), "%trueLabel-" + tmp, "%toLabel-" + tmp));
                ((FuncDef) now).push(new Label("trueLabel-" + tmp));
                node.trueStmt.accept(this);
                if (!tmpIf.trueJump) {
                    ((FuncDef) now).push(new Br("%toLabel-" + tmp));
                }
                ((FuncDef) now).push(new Label("toLabel-" + tmp));
                ((FuncDef) now).popIf();
            } else {
                ((FuncDef) now).push(new Br(exp.popVar(), "%trueLabel-" + tmp, "%falseLabel-" + tmp));
                ((FuncDef) now).push(new Label("trueLabel-" + tmp));
                node.trueStmt.accept(this);
                if (!tmpIf.trueJump) {
                    ((FuncDef) now).push(new Br("%toLabel-" + tmp));
                }
                tmpIf.onTrue = false;
                ((FuncDef) now).push(new Label("falseLabel-" + tmp));
                node.falseStmt.accept(this);
                if (!tmpIf.falseJump) {
                    ((FuncDef) now).push(new Br("%toLabel-" + tmp));
                }
                if (!tmpIf.trueJump || !tmpIf.falseJump) {
                    ((FuncDef) now).push(new Label("toLabel-" + tmp));
                }
                ((FuncDef) now).popIf();
                if (((FuncDef) now).getLoop() != null && !((FuncDef) now).isIf()) {
                    var tmpLoop = ((FuncDef) now).getLoop();
                    if (tmpIf.trueJump && tmpIf.falseJump) {
                        tmpLoop.jump = true;
                    }
                } else if (((FuncDef) now).getIf() != null && ((FuncDef) now).isIf()) {
                    var tmpIfPre = ((FuncDef) now).getIf();
                    if (tmpIf.trueJump && tmpIf.falseJump) {
                        if (tmpIfPre.onTrue) {
                            tmpIfPre.trueJump = true;
                        } else {
                            tmpIfPre.falseJump = true;
                        }
                    }
                    if (!tmpIf.trueNotReturn && !tmpIf.falseNotReturn) {
                        if (tmpIfPre.onTrue) {
                            tmpIfPre.trueNotReturn = false;
                        } else {
                            tmpIfPre.falseNotReturn = false;
                        }
                    }
                } else {
                    if (!tmpIf.trueNotReturn && !tmpIf.falseNotReturn) {
                        ((FuncDef) now).notReturn = false;
                    }
                }
            }
        }
    }

    @Override
    public void visit(ForLoop node) {
        ((FuncDef) now).pushLoop();
        var tmp = ((FuncDef) now).getLoop();
        String condition = var("loopCondition", node.scope.loopPos.line, node.scope.loopPos.column);
        String body = var("loopBody", node.scope.loopPos.line, node.scope.loopPos.column);
        String step = var("loopStep", node.scope.loopPos.line, node.scope.loopPos.column);
        String to = var("loopTo", node.scope.loopPos.line, node.scope.loopPos.column);
        if (node.variableDef != null) {
            node.variableDef.accept(this);
        }
        if (node.parallelExp != null) {
            node.parallelExp.accept(this);
        }
        ((FuncDef) now).push(new Br(condition));
        ((FuncDef) now).push(new Label(condition.substring(1)));
        if (node.conditionExp != null) {
            Exp exp = new Exp((FuncDef) now);
            var nowTmp = now;
            now = exp;
            node.conditionExp.accept(this);
            now = nowTmp;
            if (exp.isConst) {
                if (exp.popValue() == 1) {
                    ((FuncDef) now).push(new Br(body));
                } else {
                    ((FuncDef) now).push(new Br(to));
                }
            } else {
                ((FuncDef) now).push(new Br(exp.popVar(), body, to));
            }
        } else {
            ((FuncDef) now).push(new Br(body));
        }
        ((FuncDef) now).push(new Label(body.substring(1)));
        if (node.stmt != null) {
            node.stmt.accept(this);
        }
        if (!tmp.jump) {
            ((FuncDef) now).push(new Br(step));
        }
        ((FuncDef) now).push(new Label(step.substring(1)));
        if (node.stepExp != null) {
            Exp exp = new Exp((FuncDef) now);
            var nowTmp = now;
            now = exp;
            node.stepExp.accept(this);
            now = nowTmp;
        }
        ((FuncDef) now).push(new Br(condition));
        ((FuncDef) now).push(new Label(to.substring(1)));
        ((FuncDef) now).popLoop();
    }

    @Override
    public void visit(WhileLoop node) {
        ForLoop forLoop = new ForLoop();
        forLoop.conditionExp = node.judgeExp;
        forLoop.stmt = node.stmt;
        forLoop.scope = node.scope;
        forLoop.position = node.position;
        forLoop.accept(this);
    }

    @Override
    public void visit(BreakStmt node) {
        ((FuncDef) now).push(new Br(var("loopTo", node.scope.loopPos.line, node.scope.loopPos.column)));
        if (((FuncDef) now).getLoop() != null && !((FuncDef) now).isIf()) {
            ((FuncDef) now).getLoop().jump = true;
        } else if (((FuncDef) now).getIf() != null && ((FuncDef) now).isIf()) {
            var tmp = ((FuncDef) now).getIf();
            if (tmp.onTrue) {
                tmp.trueJump = true;
            } else {
                tmp.falseJump = true;
            }
        }
    }

    @Override
    public void visit(ContinueStmt node) {
        ((FuncDef) now).push(new Br(var("loopStep", node.scope.loopPos.line, node.scope.loopPos.column)));
        if (((FuncDef) now).getLoop() != null && !((FuncDef) now).isIf()) {
            ((FuncDef) now).getLoop().jump = true;
        } else if (((FuncDef) now).getIf() != null && ((FuncDef) now).isIf()) {
            var tmp = ((FuncDef) now).getIf();
            if (tmp.onTrue) {
                tmp.trueJump = true;
            } else {
                tmp.falseJump = true;
            }
        }
    }

    @Override
    public void visit(ReturnStmt node) {
        if (node.returnExp != null) {
            var nowTmp = now;
            Exp exp = new Exp((FuncDef) now);
            now = exp;
            node.returnExp.accept(this);
            now = nowTmp;
            Store store;
            if (exp.isConst) {
                store = new Store(node.returnExp.type, exp.popValue(), "%.returnValue");
            } else {
                store = new Store(node.returnExp.type, exp.popVar(), "%.returnValue");
            }
            ((FuncDef) now).push(store);
        }
        ((FuncDef) now).push(new Br("%returnLabel"));
        if (((FuncDef) now).getLoop() != null && !((FuncDef) now).isIf()) {
            ((FuncDef) now).getLoop().jump = true;
        } else if (((FuncDef) now).getIf() != null && ((FuncDef) now).isIf()) {
            var tmp = ((FuncDef) now).getIf();
            if (tmp.onTrue) {
                tmp.trueJump = true;
                tmp.trueNotReturn = false;
            } else {
                tmp.falseJump = true;
                tmp.falseNotReturn = false;
            }
        } else {
            ((FuncDef) now).notReturn = false;
        }
    }

    @Override
    public void visit(ParallelExp node) {
        var nowTmp = now;
        now = new Exp((FuncDef) now);
        node.expList.forEach(expression -> expression.accept(this));
        now = nowTmp;
    }

    @Override
    public void visit(PrimaryExp node) {
        node.exp.accept(this);
    }

    @Override
    public void visit(ClassMemberLhsExp node) {
        node.classVariable.accept(this);
        ((Exp) now).push(new Getelementptr("%" + anonymousVar,
                new IRType().setClass(node.classVariable.type.typeName), ((Exp) now).popVar(), 0,
                globalScope.getClassMemberId(node.classVariable.type.typeName, node.memberName)));
        ((Exp) now).lhsVar = "%" + anonymousVar;
        ((Exp) now).push(new Load(new IRType(node.type), "%" + (anonymousVar + 1), "%" + anonymousVar));
        ++anonymousVar;
        ((Exp) now).set("%" + anonymousVar++);
    }

    @Override
    public void visit(ClassMemFunctionLhsExp node) {
        node.classVariable.accept(this);
        ((Exp) now).pop();
        Call call = new Call("@" + node.classVariable.type.typeName + "." + node.memberFuncName);
        if (node.callList != null) {
            node.callList.expList.forEach(para -> {
                para.accept(this);
                if (((Exp) now).isOperandConst()) {
                    call.set(para.type, ((Exp) now).popValue());
                } else {
                    call.set(para.type, ((Exp) now).popVar());
                }
            });
        }
        call.set(new IRType().setPtr(), ((Exp) now).lhsVar);
        call.irType = new IRType(node.type);
        if (!node.type.isVoid()) {
            call.resultVar = "%" + anonymousVar;
            ((Exp) now).set("%" + anonymousVar++);
        }
        ((Exp) now).push(call);
    }

    @Override
    public void visit(FunctionCallLhsExp node) {
        Call call = new Call("@" + node.functionName);
        if (node.callExpList != null) {
            node.callExpList.expList.forEach(para -> {
                para.accept(this);
                if (((Exp) now).isOperandConst()) {
                    call.set(para.type, ((Exp) now).popValue());
                } else {
                    call.set(para.type, ((Exp) now).popVar());
                }
            });
        }
        call.irType = new IRType(node.type);
        if (!node.type.isVoid()) {
            call.resultVar = "%" + anonymousVar;
            ((Exp) now).set("%" + anonymousVar++);
        }
        ((Exp) now).push(call);
    }

    @Override
    public void visit(ArrayElementLhsExp node) {
        node.index.accept(this);
        node.variable.accept(this);
        ((Exp) now).pop();
        ((Exp) now).push(new Load(new IRType(node.variable.type), "%" + anonymousVar, ((Exp) now).lhsVar));
        Getelementptr getelementptr = null;
        if (((Exp) now).isOperandConst()) {
            getelementptr = new Getelementptr("%" + (anonymousVar + 1), new IRType(node.type),
                    "%" + anonymousVar, -1, (int) ((Exp) now).popValue());
        } else {
            getelementptr = new Getelementptr("%" + (anonymousVar + 1), new IRType(node.type),
                    "%" + anonymousVar, -1, ((Exp) now).popVar());
        }
        ++anonymousVar;
        ((Exp) now).push(getelementptr);
        ((Exp) now).lhsVar = "%" + anonymousVar;
        ((Exp) now).push(new Load(node.type, "%" + (anonymousVar + 1), "%" + anonymousVar));
        ++anonymousVar;
        ((Exp) now).set("%" + anonymousVar++);
    }

    @Override
    public void visit(AssignExp node) {
        node.rhs.accept(this);
        node.lhs.accept(this);
        ((Exp) now).pop();
        if (((Exp) now).isOperandConst()) {
            ((Exp) now).push(new Store(node.rhs.type, ((Exp) now).popValue(), ((Exp) now).lhsVar));
        } else {
            ((Exp) now).push(new Store(node.rhs.type, ((Exp) now).popVar(), ((Exp) now).lhsVar));
        }
    }

    @Override
    public void visit(BinaryExp node) {
        if (Objects.equals(node.op, "&&")) {
            node.lhs.accept(this);
            if (((Exp) now).isOperandConst()) {
                boolean lhsIsTrue = ((Exp) now).popValue() == 1;
                if (lhsIsTrue) {
                    node.rhs.accept(this);
                } else {
                    ((Exp) now).set(0);
                }
            } else {
                String nowLabel = ((Exp) now).funcDef.label;
                ((Exp) now).push(new Br(((Exp) now).popVar(), "%andRhs-" + anonymousLabel, "%andTo-" + anonymousLabel));
                ((Exp) now).push(new Label("andRhs-" + anonymousLabel));
                node.rhs.accept(this);
                ((Exp) now).push(new Br("%andTo-" + anonymousLabel));
                ((Exp) now).push(new Label("andTo-" + anonymousLabel));
                Phi phi = new Phi(new IRType().setI1(), "%" + anonymousVar);
                phi.push(0, "%" + nowLabel);
                if (((Exp) now).isOperandConst()) {
                    phi.push(((Exp) now).popValue(), "%andRhs-" + anonymousLabel);
                } else {
                    phi.push(((Exp) now).popVar(), "%andRhs-" + anonymousLabel++);
                }
                ((Exp) now).push(phi);
                ((Exp) now).set("%" + anonymousVar++);
            }
            return;
        } else if (Objects.equals(node.op, "||")) {
            node.lhs.accept(this);
            if (((Exp) now).isOperandConst()) {
                boolean lhsIsTrue = ((Exp) now).popValue() == 1;
                if (lhsIsTrue) {
                    ((Exp) now).set(1);
                } else {
                    node.rhs.accept(this);
                }
            } else {
                String nowLabel = ((Exp) now).funcDef.label;
                ((Exp) now).push(new Br(((Exp) now).popVar(), "%orTo-" + anonymousLabel, "%orRhs-" + anonymousLabel));
                ((Exp) now).push(new Label("orRhs-" + anonymousLabel));
                node.rhs.accept(this);
                ((Exp) now).push(new Br("%orTo-" + anonymousLabel));
                ((Exp) now).push(new Label("orTo-" + anonymousLabel));
                Phi phi = new Phi(new IRType().setI1(), "%" + anonymousVar);
                phi.push(1, "%" + nowLabel);
                if (((Exp) now).isOperandConst()) {
                    phi.push(((Exp) now).popValue(), "%orRhs-" + anonymousLabel);
                } else {
                    phi.push(((Exp) now).popVar(), "%orRhs-" + anonymousLabel++);
                }
                ((Exp) now).push(phi);
                ((Exp) now).set("%" + anonymousVar++);
            }
            return;
        }
        node.lhs.accept(this);
        node.rhs.accept(this);
        if (((Exp) now).isConst) {
            switch (node.op) {
                case "+" -> ((Exp) now).set(((Exp) now).popValue() + ((Exp) now).popValue());
                case "-" -> ((Exp) now).set(-((Exp) now).popValue() + ((Exp) now).popValue());
                case "*" -> ((Exp) now).set(((Exp) now).popValue() * ((Exp) now).popValue());
                case "/" -> {
                    long tmp = ((Exp) now).popValue();
                    ((Exp) now).set(((Exp) now).popValue() / tmp);
                }
                case "%" -> {
                    long tmp = ((Exp) now).popValue();
                    ((Exp) now).set(((Exp) now).popValue() % tmp);
                }
                case "<" -> ((Exp) now).set(((Exp) now).popValue() > ((Exp) now).popValue());
                case ">" -> ((Exp) now).set(((Exp) now).popValue() < ((Exp) now).popValue());
                case "<=" -> ((Exp) now).set(((Exp) now).popValue() >= ((Exp) now).popValue());
                case ">=" -> ((Exp) now).set(((Exp) now).popValue() <= ((Exp) now).popValue());
            }
        } else {
            if (node.type.isString()) {
                Call call = null;
                switch (node.op) {
                    case "+" -> call = new Call("@string.add");
                    case "<" -> call = new Call("@string.less");
                    case ">" -> call = new Call("@string.greater");
                    case "<=" -> call = new Call("@string.lessOrEqual");
                    case ">=" -> call = new Call("@string.greaterOrEqual");
                    case "==" -> call = new Call("@string.equal");
                    case "!=" -> call = new Call("@string.notEqual");
                }
                String tmp = ((Exp) now).popVar();
                call.set(new IRType().setPtr(), ((Exp) now).popVar());
                call.set(new IRType().setPtr(), tmp);
                call.irType = new IRType(node.type);
                call.resultVar = "%" + anonymousVar;
                ((Exp) now).push(call);
                ((Exp) now).set("%" + anonymousVar++);
                return;
            }
            switch (node.op) {
                case "+", "-", "*", "/", "%", "<<", ">>", "&", "|", "^" -> {
                    Binary binary = new Binary(node.op);
                    if (((Exp) now).isOperandConst()) {
                        binary.set(((Exp) now).popValue());
                    } else {
                        binary.set(((Exp) now).popVar());
                    }
                    if (((Exp) now).isOperandConst()) {
                        binary.set(((Exp) now).popValue());
                    } else {
                        binary.set(((Exp) now).popVar());
                    }
                    binary.output = "%" + anonymousVar;
                    ((Exp) now).set("%" + anonymousVar++);
                    ((Exp) now).push(binary);
                }
                case "<", ">", "<=", ">=", "==", "!=" -> {
                    Icmp icmp = new Icmp(node.op, node.lhs.type);
                    if (((Exp) now).isOperandConst()) {
                        icmp.set(((Exp) now).popValue());
                    } else {
                        icmp.set(((Exp) now).popVar());
                    }
                    if (((Exp) now).isOperandConst()) {
                        icmp.set(((Exp) now).popValue());
                    } else {
                        icmp.set(((Exp) now).popVar());
                    }
                    icmp.output = "%" + anonymousVar;
                    ((Exp) now).set("%" + anonymousVar++);
                    ((Exp) now).push(icmp);
                }
            }
        }
    }

    @Override
    public void visit(NewArrayExp node) {
        node.expressionList.get(node.expressionList.size() - 1).accept(this);
        if (node.expressionList.size() == node.type.dim) {//根据大小，更改最后一维的空间大小
            if (node.baseType.isBool()) {
                if (((Exp) now).isOperandConst()) {
                    ((Exp) now).set(((Exp) now).popValue() / 32 + 1);
                } else {
                    Binary binary = new Binary("/");
                    binary.operandLeft = ((Exp) now).popVar();
                    binary.valueRight = 32;
                    binary.output = "%" + anonymousVar;
                    ((Exp) now).push(binary);
                    binary = new Binary("+");
                    binary.operandLeft = "%" + anonymousVar++;
                    binary.valueRight = 1;
                    binary.output = "%" + anonymousVar;
                    ((Exp) now).push(binary);
                    ((Exp) now).set("%" + anonymousVar++);
                }
            } else {
                int size = globalScope.classMemberId.get(node.baseType.typeName).memberNum;
                if (((Exp) now).isOperandConst()) {
                    ((Exp) now).set(((Exp) now).popValue() * size);
                } else {
                    Binary binary = new Binary("*");
                    binary.operandLeft = ((Exp) now).popVar();
                    binary.valueRight = size;
                    binary.output = "%" + anonymousVar;
                    ((Exp) now).push(binary);
                    ((Exp) now).set("%" + anonymousVar++);
                }
            }
        }
        for (int i = node.expressionList.size() - 2; i >= 0; --i) {
            node.expressionList.get(i).accept(this);
        }
        String newPtr = newArray(node.expressionList.size());
        for (int i = 0; i < node.expressionList.size(); ++i) {
            ((Exp) now).pop();
        }
        ((Exp) now).set(newPtr);
    }

    public String newArray(int indexDim) {
        String newPtr = null;
        if (indexDim == 0) {
            return null;
        }
        long value = 0;
        String var = null;
        Call call = new Call("@.newArray");
        if (((Exp) now).isOperandConst()) {
            value = ((Exp) now).popValue();
            call.set(new IRType().setI32(), value);
        } else {
            var = ((Exp) now).popVar();
            call.set(new IRType().setI32(), var);
        }
        call.irType = new IRType().setPtr();
        newPtr = call.resultVar = "%" + anonymousVar++;
        ((Exp) now).push(call);
        String loopVar = "%loopVar-newArray-" + anonymousLabel;
        ((Exp) now).push(new Alloca(new IRType().setI32(), loopVar));
        ((Exp) now).push(new Store(new IRType().setI32(), 0, loopVar));
        String condition = "%newArrayCondition-" + anonymousLabel;
        String body = "%newArrayBody-" + anonymousLabel;
        String to = "%newArray-To-" + anonymousLabel++;
        ((Exp) now).push(new Br(condition));
        ((Exp) now).push(new Label(condition.substring(1)));
        ((Exp) now).push(new Load(new IRType().setI32(), "%" + anonymousVar, loopVar));
        Icmp icmp = new Icmp("<", new IRType().setI32());
        icmp.operandLeft = "%" + anonymousVar++;
        if (var == null) {
            icmp.valueRight = value;
        } else {
            icmp.operandRight = var;
        }
        icmp.output = "%" + anonymousVar;
        ((Exp) now).push(icmp);
        ((Exp) now).push(new Br("%" + anonymousVar++, body, to));
        ((Exp) now).push(new Label(body.substring(1)));
        String subNewPtr = newArray(indexDim - 1);
        ((Exp) now).push(new Load(new IRType().setI32(), "%" + anonymousVar, loopVar));
        ((Exp) now).push(new Getelementptr("%" + (anonymousVar + 1),
                new IRType().setPtr(), newPtr, -1, "%" + anonymousVar));
        anonymousVar++;
        ((Exp) now).push(new Store(new IRType().setPtr(), subNewPtr, "%" + anonymousVar++));
        ((Exp) now).push(new Load(new IRType().setI32(), "%" + anonymousVar, loopVar));
        Binary binary = new Binary("+");
        binary.operandLeft = "%" + anonymousVar++;
        binary.valueRight = 1;
        binary.output = "%" + anonymousVar;
        ((Exp) now).push(binary);
        ((Exp) now).push(new Store(new IRType().setI32(), "%" + anonymousVar++, loopVar));
        ((Exp) now).push(new Br(condition));
        ((Exp) now).push(new Label(to.substring(1)));
        if (var == null) {
            ((Exp) now).set(value);
        } else {
            ((Exp) now).set(var);
        }
        return newPtr;
    }

    @Override
    public void visit(NewClassExp node) {
        Call call = new Call("@init-class-" + node.type.typeName);
        call.irType = new IRType().setPtr();
        call.resultVar = "%" + anonymousVar;
        ((Exp) now).push(call);
        ((Exp) now).set("%" + anonymousVar++);
    }

    @Override
    public void visit(PostfixExp node) {
        node.exp.accept(this);
        Binary binary = null;
        if (Objects.equals(node.op, "++")) {
            binary = new Binary("+");
        } else {
            binary = new Binary("-");
        }
        Type type = new Type();
        type.setInt();
        binary.set(1);
        binary.set(((Exp) now).getVar());
        binary.output = "%" + anonymousVar;
        ((Exp) now).push(binary);
        ((Exp) now).push(new Store(type, "%" + anonymousVar++, ((Exp) now).lhsVar));
    }

    @Override
    public void visit(PrefixLhsExp node) {
        node.exp.accept(this);
        Binary binary = null;
        if (Objects.equals(node.op, "++")) {
            binary = new Binary("+");
        } else {
            binary = new Binary("-");
        }
        Type type = new Type();
        type.setInt();
        binary.set(1);
        binary.set(((Exp) now).popVar());
        binary.output = "%" + anonymousVar;
        ((Exp) now).set("%" + anonymousVar);
        ((Exp) now).push(binary);
        ((Exp) now).push(new Store(type, "%" + anonymousVar++, ((Exp) now).lhsVar));
    }

    @Override
    public void visit(TernaryExp node) {
        node.condition.accept(this);
        if (((Exp) now).isOperandConst()) {
            boolean condition = (((Exp) now).popValue() == 1);
            if (condition) {
                node.trueExp.accept(this);
            } else {
                node.falseExp.accept(this);
            }
        } else {
            ((Exp) now).push(new Br(((Exp) now).popVar(), "%trueLabel-" + anonymousLabel, "%falseLabel-" + anonymousLabel));
            ((Exp) now).push(new Label("trueLabel-" + anonymousLabel));
            node.trueExp.accept(this);
            ((Exp) now).push(new Br("%toLabel-" + anonymousLabel));
            ((Exp) now).push(new Label("falseLabel-" + anonymousLabel));
            node.falseExp.accept(this);
            ((Exp) now).push(new Br("%toLabel-" + anonymousLabel));
            ((Exp) now).push(new Label("toLabel-" + anonymousLabel++));
        }
    }

    @Override
    public void visit(UnaryExp node) {
        node.exp.accept(this);
        if (Objects.equals(node.op, "!")) {
            Icmp icmp = new Icmp("==", new IRType().setI1());
            if (((Exp) now).isOperandConst()) {
                icmp.set(((Exp) now).popValue());
            } else {
                icmp.set(((Exp) now).getVar());
            }
            icmp.set(0);
            icmp.output = "%" + anonymousVar;
            ((Exp) now).push(icmp);
            ((Exp) now).set("%" + anonymousVar++);
        } else if (Objects.equals(node.op, "~")) {
            Binary binary = new Binary("^");
            if (((Exp) now).isOperandConst()) {
                binary.set(((Exp) now).popValue());
            } else {
                binary.set(((Exp) now).getVar());
            }
            binary.set(-1);
            binary.output = "%" + anonymousVar;
            ((Exp) now).push(binary);
            ((Exp) now).set("%" + anonymousVar++);
        } else if (Objects.equals(node.op, "-")) {
            Binary binary = new Binary("-");
            if (((Exp) now).isOperandConst()) {
                binary.set(((Exp) now).popValue());
            } else {
                binary.set(((Exp) now).getVar());
            }
            binary.set(0);
            binary.output = "%" + anonymousVar;
            ((Exp) now).push(binary);
            ((Exp) now).set("%" + anonymousVar++);
        }
    }

    @Override
    public void visit(VariableLhsExp node) {
        if (node.scope.isConstructor) {
            ((Exp) now).push(new Getelementptr("%" + anonymousVar, new IRType().setClass(node.scope.classType.typeName),
                    "%newClassPtr-" + node.scope.classType.typeName, 0, node.id));
            ((Exp) now).lhsVar = "%" + anonymousVar;
            ((Exp) now).push(new Load(new IRType(node.type), "%" + (anonymousVar + 1), "%" + anonymousVar));
            ++anonymousVar;
            ((Exp) now).set("%" + anonymousVar++);
        } else if (node.id != -1) {
            ((Exp) now).push(new Load(new IRType().setPtr(), "%" + anonymousVar, "%this"));
            ((Exp) now).push(new Getelementptr("%" + (anonymousVar + 1),
                    new IRType().setClass(node.scope.classType.typeName), "%" + anonymousVar, 0, node.id));
            ++anonymousVar;
            ((Exp) now).lhsVar = "%" + anonymousVar;
            ((Exp) now).push(new Load(new IRType(node.type), "%" + (anonymousVar + 1), "%" + anonymousVar));
            ++anonymousVar;
            ((Exp) now).set("%" + anonymousVar++);
        } else {
            ((Exp) now).set("%" + anonymousVar);
            ((Exp) now).push(new Load(node.type, "%" + anonymousVar++,
                    var(node.variableName, node.line, node.column)));
            ((Exp) now).lhsVar = var(node.variableName, node.line, node.column);
        }
    }

    @Override
    public void visit(ThisPointerExp node) {
        ((Exp) now).push(new Load(new IRType(node.type), "%" + anonymousVar, "%this"));
        ((Exp) now).set("%" + anonymousVar++);
    }

    @Override
    public void visit(BoolExp node) {
        ((Exp) now).set(node.value ? 1 : 0);
    }

    @Override
    public void visit(NumberExp node) {
        ((Exp) now).set(node.value);
    }

    @Override
    public void visit(StringExp node) {
        ((Exp) now).set(irProgram.pushConstString(node.value));
    }

    @Override
    public void visit(NullExp node) {
        ((Exp) now).set(0);
    }

    public String var(String varName, int line, int column) {
        if (line == 0 && column == 0) {
            return "@" + varName;
        } else {
            return "%" + varName + "-" + line + "-" + column;
        }
    }

}
