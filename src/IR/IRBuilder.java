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
import src.IR.statement.FuncDef;
import src.IR.statement.GlobalVarDef;

import java.util.Objects;

public class IRBuilder implements ASTVisitor {
    public IRProgram irProgram;
    public IRNode now;
    public int anonymousVar = 0;

    public IRBuilder(Program node) {
        irProgram = new IRProgram();
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

    }

    @Override
    public void visit(ClassDef node) {

    }

    @Override
    public void visit(Constructor node) {

    }

    @Override
    public void visit(FunctionDef node) {
        anonymousVar = 0;
        var nowTmp = now;
        FuncDef funcDef = new FuncDef();
        ((IRProgram) now).push(funcDef);
        now = funcDef;
        funcDef.type = node.type;
        funcDef.functionName = "@" + node.functionName;
        for (int i = 0; i < node.parameterNameList.size(); ++i) {
            funcDef.push(node.parameterTypeList.get(i), node.parameterNameList.get(i));
        }
        node.body.accept(this);
        if (funcDef.type.isVoid()) {
            funcDef.push(new Ret());
        }
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
                }
            } else {
                globalVarDef.value = 0;
            }
        } else {
            Alloca alloca = new Alloca(node.type,
                    var(node.variableName, node.position.line, node.position.column));
            ((FuncDef) now).push(alloca);
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
        node.statementList.forEach(stmt -> stmt.accept(this));
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
        if (Objects.equals(node.op, "+")) {
            if (((Exp) now).isConst) {
                ((Exp) now).set(((Exp) now).popValue() + ((Exp) now).popValue());
            } else {
                Add add = new Add();
                if (((Exp) now).isOperandConst()) {
                    add.set(((Exp) now).popValue());
                } else {
                    add.set(((Exp) now).popVar());
                }
                if (((Exp) now).isOperandConst()) {
                    add.set(((Exp) now).popValue());
                } else {
                    add.set(((Exp) now).popVar());
                }
                add.output = "%" + anonymousVar;
                ((Exp) now).set("%" + anonymousVar++);
                ((Exp) now).push(add);
            }
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
