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
        for (var stmt : node.statementList) {
            stmt.accept(this);
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
            int tmp = anonymousLabel;
            ++anonymousLabel;
            if (node.falseStmt == null ||
                    (node.falseStmt.suite != null && node.falseStmt.suite.statementList.size() == 0)) {
                ((FuncDef) now).push(new Br(exp.popVar(), "%trueLabel-" + tmp, "%toLabel-" + tmp));
                ((FuncDef) now).push(new Label("trueLabel-" + tmp));
                node.trueStmt.accept(this);
                if (node.trueStmt.scope.notReturn) {
                    ((FuncDef) now).push(new Br("%toLabel-" + tmp));
                }
                ((FuncDef) now).push(new Label("toLabel-" + tmp));
            } else {
                ((FuncDef) now).push(new Br(exp.popVar(), "%trueLabel-" + tmp, "%falseLabel-" + tmp));
                ((FuncDef) now).push(new Label("trueLabel-" + tmp));
                node.trueStmt.accept(this);
                if (node.trueStmt.scope.notReturn) {
                    ((FuncDef) now).push(new Br("%toLabel-" + tmp));
                }
                ((FuncDef) now).push(new Label("falseLabel-" + tmp));
                node.falseStmt.accept(this);
                if (node.falseStmt.scope.notReturn) {
                    ((FuncDef) now).push(new Br("%toLabel-" + tmp));
                }
                if (node.trueStmt.scope.notReturn || node.falseStmt.scope.notReturn) {
                    ((FuncDef) now).push(new Label("toLabel-" + tmp));
                }
            }
        }
    }

    @Override
    public void visit(ForLoop node) {

    }

    @Override
    public void visit(WhileLoop node) {

    }

    @Override
    public void visit(BreakStmt node) {

    }

    @Override
    public void visit(ContinueStmt node) {

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
