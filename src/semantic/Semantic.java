package src.semantic;

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
import src.Util.error.SemanticErrors;
import src.Util.scope.GlobalScope;
import src.Util.scope.Scope;
import src.Util.type.Type;

public class Semantic implements ASTVisitor {
    public GlobalScope globalScope;
    public Program ASTProgram;

    public Semantic(Program ASTProgram_) {
        globalScope = new GlobalScope();
        ASTProgram = ASTProgram_;
        for (var def : ASTProgram.defList) {
            if (def.functionDef != null) {
                globalScope.setFunction(def.functionDef.functionName, def.functionDef.type,
                        def.functionDef.parameterTypeList, def.functionDef.position);
            } else if (def.classDef != null) {
                globalScope.setClassName(def.classDef.className, def.classDef.position);
                for (var varList : def.classDef.variableDefList) {
                    varList.initVariablelist.forEach(var -> {
                        globalScope.setClassMember(def.classDef.className,
                                var.variableName, varList.type, var.position);
                    });
                }
                def.classDef.functionDefList.forEach(funcList -> {
                    globalScope.setClassMethod(def.classDef.className, funcList.functionName,
                            funcList.type, funcList.parameterTypeList, funcList.position);
                });
            }
        }
    }

    public void visit(Program node) {
        boolean mainExist = false;
        for (var def : node.defList) {
            if (def.mainDef != null) {
                if (mainExist) {
                    throw new SemanticErrors("Duplicate main function.", def.mainDef.position);
                } else {
                    mainExist = true;
                }
            }
            def.accept(this);
        }
    }

    public void visit(Suite node) {
        if (node == null) {
            return;
        }
        for (var stmt : node.statementList) {
            stmt.scope = new Scope(node.scope);
            stmt.accept(this);
            if (stmt.variableDef != null) {
                stmt.variableDef.initVariablelist.forEach(var ->
                        node.scope.setVariable(var.variableName, stmt.variableDef.type, var.position)
                );
            }
        }
    }

    public void visit(Statement node) {
        if (node == null) {
            return;
        }
        if (node.variableDef != null) {
            node.variableDef.scope = new Scope(node.scope);
            node.variableDef.accept(this);
        } else if (node.suite != null) {
            node.suite.scope = new Scope(node.scope);
            node.suite.accept(this);
        } else if (node.jumpStatement != null) {
            node.jumpStatement.scope = new Scope(node.scope);
            node.jumpStatement.accept(this);
        } else if (node.loopStatement != null) {
            node.loopStatement.scope = new Scope(node.scope);
            node.loopStatement.accept(this);
        } else if (node.selectStatement != null) {
            node.selectStatement.scope = new Scope(node.scope);
            node.selectStatement.accept(this);
        } else if (node.parallelExp != null) {
            node.parallelExp.scope = new Scope(node.scope);
            node.parallelExp.accept(this);
        }
    }

    public void visit(SelectStatement node) {

    }

    public void visit(ForLoop node) {

    }

    public void visit(WhileLoop node) {

    }

    public void visit(BreakStmt node) {

    }

    public void visit(ContinueStmt node) {

    }

    public void visit(ReturnStmt node) {

    }

    public void visit(ArrayElementLhsExp node) {

    }

    public void visit(AssignExp node) {

    }

    public void visit(BinaryExp node) {

    }

    public void visit(BoolExp node) {

    }

    public void visit(ClassMemberLhsExp node) {

    }

    public void visit(ClassMemFunctionLhsExp node) {

    }

    public void visit(FunctionCallLhsExp node) {

    }

    public void visit(NewArrayExp node) {

    }

    public void visit(NewClassExp node) {

    }

    public void visit(NullExp node) {

    }

    public void visit(NumberExp node) {

    }

    public void visit(ParallelExp node) {
        if (node == null) {
            return;
        }
        for (var exp : node.expList) {
            exp.scope = new Scope(node.scope);
            exp.accept(this);
        }
    }

    public void visit(PostfixExp node) {
        if (node == null) {
            return;
        }
        node.exp.scope = new Scope(node.scope);
        node.exp.accept(this);
        if (!node.exp.isAssign) {
            throw new SemanticErrors("Right value error.", node.exp.position);
        }
        if (node.exp.type.typeEnum != Type.TypeEnum.INT || node.exp.type.dim > 0) {
            throw new SemanticErrors("Invalid postfix.", node.position);
        }
        node.type = new Type(node.exp.type);
    }

    public void visit(PrefixLhsExp node) {
        if (node == null) {
            return;
        }
        node.exp.scope = new Scope(node.scope);
        node.exp.accept(this);
        if (!node.exp.isAssign) {
            throw new SemanticErrors("Right value error.", node.exp.position);
        }
        if (node.exp.type.typeEnum != Type.TypeEnum.INT || node.exp.type.dim > 0) {
            throw new SemanticErrors("Invalid prefix.", node.position);
        }
        node.type = new Type(node.exp.type);
    }

    public void visit(PrimaryExp node) {

    }

    public void visit(StringExp node) {

    }

    public void visit(TernaryExp node) {

    }

    public void visit(ThisPointerExp node) {

    }

    public void visit(UnaryExp node) {

    }

    public void visit(VariableLhsExp node) {
        if (node == null) {
            return;
        }
        var typeTmp = node.scope.getVariable(node.variableName);
        if (typeTmp == null) {
            typeTmp = globalScope.getVariable(node.variableName);
            if (typeTmp == null) {
                throw new SemanticErrors("Variable not define.", node.position);
            }
        }
        node.type = new Type(typeTmp);
        node.isAssign = true;
    }

    public void visit(InitVariable node) {

    }

    public void visit(VariableDef node) {

    }

    public void visit(ClassDef node) {

    }

    public void visit(Constructor node) {

    }

    public void visit(FunctionDef node) {

    }

    public void visit(MainDef node) {
        if (node == null) {
            return;
        }
        node.suite.scope = new Scope(node.scope);
        node.suite.accept(this);
    }

    public void visit(Definition node) {
        if (node == null) {
            return;
        }
        if (node.mainDef != null) {
            node.mainDef.scope = new Scope();
            node.mainDef.accept(this);
        } else if (node.classDef != null) {
            node.classDef.scope = new Scope();
            node.classDef.accept(this);
        } else if (node.functionDef != null) {
            node.functionDef.scope = new Scope();
            node.functionDef.accept(this);
        } else if (node.variableDef != null) {
            node.variableDef.scope = new Scope();
            node.variableDef.accept(this);
            node.variableDef.initVariablelist.forEach(var ->
                    globalScope.setVariable(var.variableName, node.variableDef.type, var.position));
        }
    }
}
