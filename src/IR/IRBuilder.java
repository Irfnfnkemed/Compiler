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
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;
import src.Util.type.Type;

public class IRBuilder implements ASTVisitor {
    public IRProgram irProgram;
    public FuncDef funcMain;
    public IRNode now;
    public int anonymousVar = 0;
    public int anonymousLabel = 0;

    public IRBuilder(Program node) {
        irProgram = new IRProgram();
        funcMain = new FuncDef();
        funcMain.type = new Type();
        funcMain.type.setInt();
        funcMain.functionName = "@main";
        visit(node);
    }

    @Override
    public void visit(Program node) {
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
        Type type = new Type();
        type.setInt();
        funcMain.push(new Alloca(type, "%0"));
        funcMain.push(new Store(type, 0, "%0"));
        anonymousVar = 1;
        anonymousLabel = 0;
        irProgram.push(funcMain);
        var nowTmp = now;
        now = funcMain;
        node.suite.accept(this);
        if (node.scope.notReturn) {
            funcMain.push(new Br("%returnLabel"));
        }
        funcMain.push(new Label("returnLabel"));
        funcMain.push(new Load(type, "%" + anonymousVar, "%0"));
        funcMain.push(new Ret(type, "%" + anonymousVar));
        now = nowTmp;
    }

    @Override
    public void visit(ClassDef node) {

    }

    @Override
    public void visit(Constructor node) {

    }

    @Override
    public void visit(FunctionDef node) {
        funcMain.push(new Alloca(node.type, "%0"));
        funcMain.push(new Store(node.type, 0, "%0"));
        anonymousVar = 1;
        anonymousLabel = 0;
        var nowTmp = now;
        FuncDef funcDef = new FuncDef();
        ((IRProgram) now).push(funcDef);
        now = funcDef;
        funcDef.type = node.type;
        funcDef.functionName = "@" + node.functionName;
        for (int i = 0; i < node.parameterNameList.size(); ++i) {
            funcDef.pushPara(node.parameterTypeList.get(i));
            var varName = var(node.parameterNameList.get(i), node.parameterTypeList.get(i).position.line,
                    node.parameterTypeList.get(i).position.column);
            funcDef.push(new Alloca(node.parameterTypeList.get(i), varName));
            funcDef.push(new Store(node.type, "%" + anonymousVar++, varName));
        }
        node.body.accept(this);
        if (node.scope.notReturn) {
            funcDef.push(new Br("%returnLabel"));
        }
        funcDef.push(new Label("returnLabel"));
        funcMain.push(new Load(node.type, "%" + anonymousVar, "%0"));
        funcMain.push(new Ret(node.type, "%" + anonymousVar));
        now = nowTmp;
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
            globalVarDef.type = node.type;
            if (node.exp != null) {
                globalVarDef.setFuncDef();
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
                    funcMain.push(new Call("@init-" + node.variableName));
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
                if (((FuncDef) now).getLoop() != null && !((FuncDef) now).isIf()) {
                    var tmpLoop = ((FuncDef) now).getLoop();
                    if (tmpIf.trueReturn) {
                        tmpLoop.loopReturn = true;
                    }
                    if (tmpIf.trueBreak) {
                        tmpLoop.loopBreak = true;
                    }
                    if (tmpIf.trueContinue) {
                        tmpLoop.loopContinue = true;
                    }
                }
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
                    if (tmpIf.trueReturn || tmpIf.falseReturn) {
                        tmpLoop.loopReturn = true;
                    }
                    if (tmpIf.trueBreak || tmpIf.falseBreak) {
                        tmpLoop.loopBreak = true;
                    }
                    if (tmpIf.trueContinue || tmpIf.falseContinue) {
                        tmpLoop.loopContinue = true;
                    }
                    if (tmpIf.trueJump && tmpIf.falseJump) {
                        tmpLoop.jump = true;
                    }
                    if ((tmpIf.trueBreak || tmpIf.trueReturn) && (tmpIf.falseBreak || tmpIf.falseReturn)) {
                        tmpLoop.jumpStep = false;
                    }
                } else if (((FuncDef) now).getIf() != null && ((FuncDef) now).isIf()) {
                    var tmpIfPre = ((FuncDef) now).getIf();
                    if (tmpIf.trueReturn || tmpIf.falseReturn) {
                        if (tmpIfPre.onTrue) {
                            tmpIfPre.trueReturn = true;
                        } else {
                            tmpIfPre.falseReturn = true;
                        }
                    }
                    if (tmpIf.trueBreak || tmpIf.falseBreak) {
                        if (tmpIfPre.onTrue) {
                            tmpIfPre.trueBreak = true;
                        } else {
                            tmpIfPre.falseBreak = true;
                        }
                    }
                    if (tmpIf.trueContinue || tmpIf.falseContinue) {
                        if (tmpIfPre.onTrue) {
                            tmpIfPre.trueContinue = true;
                        } else {
                            tmpIfPre.falseContinue = true;
                        }
                    }
                    if (tmpIf.trueJump && tmpIf.falseJump) {
                        if (tmpIfPre.onTrue) {
                            tmpIfPre.trueJump = true;
                        } else {
                            tmpIfPre.falseJump = true;
                        }
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
            }
            ((FuncDef) now).push(new Br(exp.popVar(), body, to));
        } else {
            ((FuncDef) now).push(new Br(body));
        }
        ((FuncDef) now).push(new Label(body.substring(1)));
        node.stmt.accept(this);
        if (!tmp.jump) {
            ((FuncDef) now).push(new Br(step));
        }
        if (!(tmp.jump && !tmp.loopContinue)) {
            ((FuncDef) now).push(new Label(step.substring(1)));
            if (node.stepExp != null) {
                node.stepExp.accept(this);
            }
            ((FuncDef) now).push(new Br(condition));
        }
        if (!(tmp.loopReturn && !tmp.loopBreak && !tmp.loopContinue)) {
            if (!tmp.jumpStep && !tmp.loopReturn) {
                ((FuncDef) now).push(new Br(to));
            }
            ((FuncDef) now).push(new Label(to.substring(1)));
        }
        ((FuncDef) now).popLoop();
        if (((FuncDef) now).getIf() != null && ((FuncDef) now).isIf()) {
            var tmpIf = ((FuncDef) now).getIf();
            if (tmp.loopReturn) {
                if (tmpIf.onTrue) {
                    tmpIf.trueReturn = true;
                    if (!tmp.loopContinue && !tmp.loopBreak) {
                        tmpIf.trueJump = true;
                    }
                } else {
                    tmpIf.falseReturn = true;
                    if (!tmp.loopContinue && !tmp.loopBreak) {
                        tmpIf.falseJump = true;
                    }
                }
            }
        } else if (((FuncDef) now).getLoop() != null && !((FuncDef) now).isIf()) {
            var tmpLoop = ((FuncDef) now).getLoop();
            if (tmp.loopReturn && !tmp.loopContinue && !tmp.loopBreak) {
                tmpLoop.loopReturn = true;
                tmpLoop.jump = true;
                tmpLoop.jumpStep = false;
            }
        }
    }

    @Override
    public void visit(WhileLoop node) {

    }

    @Override
    public void visit(BreakStmt node) {
        ((FuncDef) now).push(new Br(var("loopTo", node.scope.loopPos.line, node.scope.loopPos.column)));
        if (((FuncDef) now).getLoop() != null && !((FuncDef) now).isIf()) {
            ((FuncDef) now).getLoop().loopBreak = true;
            ((FuncDef) now).getLoop().jump = true;
            ((FuncDef) now).getLoop().jumpStep = false;
        } else if (((FuncDef) now).getIf() != null && ((FuncDef) now).isIf()) {
            var tmp = ((FuncDef) now).getIf();
            if (tmp.onTrue) {
                tmp.trueBreak = true;
                tmp.trueJump = true;
            } else {
                tmp.falseBreak = true;
                tmp.falseJump = true;
            }
        }
    }

    @Override
    public void visit(ContinueStmt node) {
        ((FuncDef) now).push(new Br(var("loopStep", node.scope.loopPos.line, node.scope.loopPos.column)));
        if (((FuncDef) now).getLoop() != null && !((FuncDef) now).isIf()) {
            ((FuncDef) now).getLoop().loopContinue = true;
            ((FuncDef) now).getLoop().jump = true;
        } else if (((FuncDef) now).getIf() != null && ((FuncDef) now).isIf()) {
            var tmp = ((FuncDef) now).getIf();
            if (tmp.onTrue) {
                tmp.trueContinue = true;
                tmp.trueJump = true;
            } else {
                tmp.falseContinue = true;
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
            String toVar;
            now = nowTmp;
            Store store;
            if (exp.isConst) {
                store = new Store(node.returnExp.type, exp.popValue(), "%0");
            } else {
                store = new Store(node.returnExp.type, exp.popVar(), "%0");
            }
            ((FuncDef) now).push(store);
        }
        ((FuncDef) now).push(new Br("%returnLabel"));
        if (((FuncDef) now).getLoop() != null && !((FuncDef) now).isIf()) {
            ((FuncDef) now).getLoop().loopReturn = true;
            ((FuncDef) now).getLoop().jump = true;
            ((FuncDef) now).getLoop().jumpStep = false;
        } else if (((FuncDef) now).getIf() != null && ((FuncDef) now).isIf()) {
            var tmp = ((FuncDef) now).getIf();
            if (tmp.onTrue) {
                tmp.trueReturn = true;
                tmp.trueJump = true;
            } else {
                tmp.falseReturn = true;
                tmp.falseJump = true;
            }
        }
    }

    @Override
    public void visit(ParallelExp node) {

    }

    @Override
    public void visit(PrimaryExp node) {

    }

    @Override
    public void visit(ClassMemberLhsExp node) {

    }

    @Override
    public void visit(ClassMemFunctionLhsExp node) {

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
        call.type = node.type;
        if (!node.type.isVoid()) {
            call.resultVar = "%" + anonymousVar;
        }
        ((Exp) now).set("%" + anonymousVar++);
        ((Exp) now).push(call);
    }

    @Override
    public void visit(ArrayElementLhsExp node) {

    }

    @Override
    public void visit(AssignExp node) {

    }

    @Override
    public void visit(BinaryExp node) {
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
                /////////////
            }
        } else {
            Binary binary = new Binary();
            binary.op = node.op;
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

    }

    @Override
    public void visit(NewArrayExp node) {

    }

    @Override
    public void visit(NewClassExp node) {

    }

    @Override
    public void visit(PostfixExp node) {

    }

    @Override
    public void visit(PrefixLhsExp node) {

    }

    @Override
    public void visit(TernaryExp node) {

    }

    @Override
    public void visit(UnaryExp node) {

    }

    @Override
    public void visit(VariableLhsExp node) {
        ((Exp) now).set("%" + anonymousVar);
        ((Exp) now).push(new Load(node.type, "%" + anonymousVar,
                var(node.variableName, node.line, node.column)));
        anonymousVar++;
    }

    @Override
    public void visit(ThisPointerExp node) {

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

    }

    @Override
    public void visit(NullExp node) {

    }

    public String var(String varName, int line, int column) {
        if (line == 0 && column == 0) {
            return "@" + varName;
        } else {
            return "%" + varName + "-" + line + "-" + column;
        }
    }

}
