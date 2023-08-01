package src.AST;

import org.antlr.v4.runtime.tree.ParseTree;
import src.paser.MxBaseVisitor;
import src.paser.MxParser;
import src.AST.expression.*;

import java.util.Arrays;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {
    ASTNode ASTprogram;

    public void build(ParseTree ctx) {
        ASTprogram = visit(ctx);
    }

    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        if (ctx == null) {
            return null;
        }
        Program program = new Program();
        program.mainDef = (MainDef) visitMainDef(ctx.mainDef());
        ctx.classDef().forEach(ele -> program.classDefList.add((ClassDef) visitClassDef(ele)));
        ctx.functionDef().forEach(ele -> program.functionDefList.add((FunctionDef) visitFunctionDef(ele)));
        ctx.variableDef().forEach(ele -> program.variableDefList.add((VariableDef) visitVariableDef(ele)));
        return program;
    }

    @Override
    public ASTNode visitMainDef(MxParser.MainDefContext ctx) {
        if (ctx == null) {
            return null;
        }
        MainDef mainDef = new MainDef();
        mainDef.suite = (Suite) visitSuite(ctx.suite());
        return mainDef;
    }

    @Override
    public ASTNode visitClassDef(MxParser.ClassDefContext ctx) {
        if (ctx == null) {
            return null;
        }
        ClassDef classDef = new ClassDef();
        classDef.className = ctx.Identifier().getText();
        ctx.variableDef().forEach(ele -> classDef.variableDefList.add((VariableDef) visitVariableDef(ele)));
        ctx.functionDef().forEach(ele -> classDef.functionDefList.add((FunctionDef) visitFunctionDef(ele)));
        return classDef;
    }

    @Override
    public ASTNode visitVariableDef(MxParser.VariableDefContext ctx) {
        if (ctx == null) {
            return null;
        }
        VariableDef variableDef = new VariableDef();
        variableDef.type = (Type) visitType(ctx.typeName());
        ctx.initVariable().forEach(ele -> variableDef.initVariablelist.add((InitVariable) visitInitVariable(ele)));
        return variableDef;
    }

    @Override
    public ASTNode visitInitVariable(MxParser.InitVariableContext ctx) {
        if (ctx == null) {
            return null;
        }
        InitVariable initVariable = new InitVariable();
        initVariable.variableName = ctx.Identifier().getText();
        initVariable.exp = (Expression) visitExpression(ctx.expression());
        return initVariable;
    }

    @Override
    public ASTNode visitSuite(MxParser.SuiteContext ctx) {
        if (ctx == null) {
            return null;
        }
        Suite suite = new Suite();
        ctx.statement().forEach(ele -> suite.statementList.add((Statement) visitStatement(ele)));
        return suite;
    }

    @Override
    public ASTNode visitStatement(MxParser.StatementContext ctx) {
        if (ctx == null) {
            return null;
        }
        Statement statement = new Statement();
        statement.parallelExp = (ParallelExp) visitParallelExp(ctx.parallelExp());
        statement.jumpStatement = (JumpStatement) visitJumpStatement(ctx.jumpStatement());
        statement.loopStatement = (LoopStatement) visitLoopStatement(ctx.loopStatement());
        statement.selectStatement = (SelectStatement) visitSelectStatement(ctx.selectStatement());
        statement.variableDef = (VariableDef) visitVariableDef(ctx.variableDef());
        return statement;
    }

    @Override
    public ASTNode visitFunctionDef(MxParser.FunctionDefContext ctx) {
        if (ctx == null) {
            return null;
        }
        FunctionDef functionDef = new FunctionDef();
        functionDef.type = (Type) visitType(ctx.typeName(0));
        functionDef.functionName = ctx.Identifier(0).getText();
        ctx.typeName().stream().skip(1).forEach(ele -> functionDef.parameterTypeList.add((Type) visitType(ele)));
        ctx.Identifier().stream().skip(1).forEach(ele -> functionDef.parameterNameList.add(ele.getText()));
        functionDef.body = (Suite) visitSuite(ctx.suite());
        return functionDef;
    }

    public ASTNode visitType(MxParser.TypeNameContext ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx instanceof MxParser.FundationTypeContext) {
            return visitFundationType((MxParser.FundationTypeContext) ctx);
        } else if (ctx instanceof MxParser.ClassTypeContext) {
            return visitClassType((MxParser.ClassTypeContext) ctx);
        } else if (ctx instanceof MxParser.ArrayTypeContext) {
            return visitArrayType((MxParser.ArrayTypeContext) ctx);
        }
        return null;
    }

    @Override
    public ASTNode visitFundationType(MxParser.FundationTypeContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("FundationType");
        Type type = new Type(ctx.VOID(), ctx.BOOL(), ctx.INT(), ctx.STRING());
        return type;
    }

    @Override
    public ASTNode visitClassType(MxParser.ClassTypeContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("ClassType");
        Type type = new Type(ctx.Identifier().getSymbol().getText());
        return type;
    }

    @Override
    public ASTNode visitArrayType(MxParser.ArrayTypeContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("ArrayType");
        Type type = new Type((Type) visitType(ctx.typeName()), ctx.brackets().size());
        return type;
    }

    public ASTNode visitExpression(MxParser.ExpressionContext ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx instanceof MxParser.VariableLhsExpContext) {
            return visitVariableLhsExp((MxParser.VariableLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.ThisPointerExpContext) {
            return visitThisPointerExp((MxParser.ThisPointerExpContext) ctx);
        } else if (ctx instanceof MxParser.NumberExpContext) {
            return visitNumberExp((MxParser.NumberExpContext) ctx);
        } else if (ctx instanceof MxParser.StringExpContext) {
            return visitStringExp((MxParser.StringExpContext) ctx);
        } else if (ctx instanceof MxParser.BoolExpContext) {
            return visitBoolExp((MxParser.BoolExpContext) ctx);
        } else if (ctx instanceof MxParser.NullExpContext) {
            return visitNullExp((MxParser.NullExpContext) ctx);
        } else if (ctx instanceof MxParser.PrimaryExpContext) {
            return visitPrimaryExp((MxParser.PrimaryExpContext) ctx);
        } else if (ctx instanceof MxParser.ClassMemberLhsExpContext) {
            return visitClassMemberLhsExp((MxParser.ClassMemberLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.ClassMemFunctionLhsExpContext) {
            return visitClassMemFunctionLhsExp((MxParser.ClassMemFunctionLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.ArrayElementLhsExpContext) {
            return visitArrayElementLhsExp((MxParser.ArrayElementLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.FunctionCallLhsExpContext) {
            return visitFunctionCallLhsExp((MxParser.FunctionCallLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.PostfixExpContext) {
            return visitPostfixExp((MxParser.PostfixExpContext) ctx);
        } else if (ctx instanceof MxParser.PrefixLhsExpContext) {
            return visitPrefixLhsExp((MxParser.PrefixLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.UnaryExpContext) {
            return visitUnaryExp((MxParser.UnaryExpContext) ctx);
        } else if (ctx instanceof MxParser.BinaryExpContext) {
            return visitBinaryExp((MxParser.BinaryExpContext) ctx);
        } else if (ctx instanceof MxParser.TernaryExpContext) {
            return visitTernaryExp((MxParser.TernaryExpContext) ctx);
        } else if (ctx instanceof MxParser.AssignExpContext) {
            return visitAssignExp((MxParser.AssignExpContext) ctx);
        } else if (ctx instanceof MxParser.NewClassExpContext) {
            return visitNewClassExp((MxParser.NewClassExpContext) ctx);
        } else if (ctx instanceof MxParser.NewArrayExpContext) {
            return visitNewArrayExp((MxParser.NewArrayExpContext) ctx);
        } else {
            return null;
        }
    }

    @Override
    public ASTNode visitFunctionCallLhsExp(MxParser.FunctionCallLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitFunctionCallLhsExp");
        FunctionCallLhsExp functionCallLhsExp = new FunctionCallLhsExp();
        functionCallLhsExp.functionName = ctx.Identifier().getText();
        functionCallLhsExp.callExpList = (ParallelExp) visitParallelExp(ctx.parallelExp());
        return functionCallLhsExp;
    }

    @Override
    public ASTNode visitBoolExp(MxParser.BoolExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitBoolExp");
        BoolExp boolExp = new BoolExp(ctx.TRUE(), ctx.FALSE());
        return boolExp;
    }

    @Override
    public ASTNode visitClassMemberLhsExp(MxParser.ClassMemberLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitClassMemberLhsExp");
        ClassMemberLhsExp classMemberLhsExp = new ClassMemberLhsExp();
        classMemberLhsExp.classVariable = (Expression) visitExpression(ctx.expression());
        classMemberLhsExp.memberName = ctx.Identifier().getText();
        return classMemberLhsExp;
    }

    @Override
    public ASTNode visitNewArrayExp(MxParser.NewArrayExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitNewArrayExp");
        NewArrayExp newArrayExp = new NewArrayExp();
        newArrayExp.type = (Type) visitType(ctx.typeName());
        ctx.expression().forEach(ele -> newArrayExp.expressionList.add((Expression) visitExpression(ele)));
        newArrayExp.dim = newArrayExp.type.dim + newArrayExp.expressionList.size();
        return newArrayExp;
    }

    @Override
    public ASTNode visitStringExp(MxParser.StringExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitStringExp");
        StringExp stringExp = new StringExp();
        stringExp.value = ctx.String().getText();
        return stringExp;
    }

    @Override
    public ASTNode visitThisPointerExp(MxParser.ThisPointerExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitThisPointerExp");
        ThisPointerExp thisPointerExp = new ThisPointerExp();
        return thisPointerExp;
    }

    @Override
    public ASTNode visitPrefixLhsExp(MxParser.PrefixLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitPrefixLhsExp");
        PrefixLhsExp prefixLhsExp = new PrefixLhsExp();
        prefixLhsExp.exp = (Expression) visitExpression(ctx.expression());
        prefixLhsExp.op = ctx.op.getText();
        return prefixLhsExp;
    }

    @Override
    public ASTNode visitNullExp(MxParser.NullExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitNullExp");
        NullExp nullExp = new NullExp();
        return nullExp;
    }

    @Override
    public ASTNode visitVariableLhsExp(MxParser.VariableLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitVariableLhsExp");
        VariableLhsExp variableLhsExp = new VariableLhsExp(ctx.Identifier().getText());
        return variableLhsExp;
    }

    @Override
    public ASTNode visitClassMemFunctionLhsExp(MxParser.ClassMemFunctionLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitClassMemFunctionLhsExp");
        ClassMemberLhsExp classMemberLhsExp = new ClassMemberLhsExp();
        classMemberLhsExp.memberName = ctx.Identifier().getText();
        classMemberLhsExp.classVariable = (Expression) visitExpression(ctx.expression());
        return classMemberLhsExp;
    }

    @Override
    public ASTNode visitBinaryExp(MxParser.BinaryExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitBinaryExp");
        BinaryExp binaryExp = new BinaryExp();
        binaryExp.lhs = (Expression) visitExpression(ctx.expression(0));
        binaryExp.rhs = (Expression) visitExpression(ctx.expression(1));
        binaryExp.op = ctx.op.getText();
        return binaryExp;
    }

    @Override
    public ASTNode visitPrimaryExp(MxParser.PrimaryExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitPrimaryExp");
        PrimaryExp primaryExp = new PrimaryExp();
        primaryExp.exp = (Expression) visitExpression(ctx.expression());
        return primaryExp;
    }

    @Override
    public ASTNode visitPostfixExp(MxParser.PostfixExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitPostfixExp");
        PostfixExp postfixExp = new PostfixExp();
        postfixExp.exp = (Expression) visitExpression(ctx.expression());
        postfixExp.op = ctx.op.getText();
        return postfixExp;
    }

    @Override
    public ASTNode visitAssignExp(MxParser.AssignExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitAssignExp");
        AssignExp assignExp = new AssignExp();
        assignExp.lhs = (Expression) visitExpression(ctx.expression(0));
        assignExp.rhs = (Expression) visitExpression(ctx.expression(1));
        return assignExp;
    }

    @Override
    public ASTNode visitNewClassExp(MxParser.NewClassExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitNewClassExp");
        NewClassExp newClassExp = new NewClassExp();
        return newClassExp;
    }

    @Override
    public ASTNode visitTernaryExp(MxParser.TernaryExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitTernaryExp");
        TernaryExp ternaryExp = new TernaryExp();
        ternaryExp.lhs = (Expression) visitExpression(ctx.expression(0));
        ternaryExp.mhs = (Expression) visitExpression(ctx.expression(1));
        ternaryExp.rhs = (Expression) visitExpression(ctx.expression(2));
        return ternaryExp;
    }

    @Override
    public ASTNode visitArrayElementLhsExp(MxParser.ArrayElementLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitArrayElementLhsExp");
        ArrayElementLhsExp arrayElementLhsExp = new ArrayElementLhsExp();
        arrayElementLhsExp.variable = (Expression) visitExpression(ctx.expression(0));
        arrayElementLhsExp.index = (Expression) visitExpression(ctx.expression(1));
        return arrayElementLhsExp;
    }

    @Override
    public ASTNode visitUnaryExp(MxParser.UnaryExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitUnaryExp");
        UnaryExp unaryExp = new UnaryExp();
        unaryExp.exp = (Expression) visitExpression(ctx.expression());
        unaryExp.op = ctx.op.getText();
        return unaryExp;
    }

    @Override
    public ASTNode visitNumberExp(MxParser.NumberExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        System.out.println("visitNumberExp");
        NumberExp numberExp = new NumberExp(ctx.DecNumber().getText());
        return numberExp;
    }


    @Override
    public ASTNode visitParallelExp(MxParser.ParallelExpContext ctx) {
        System.out.println("visitParallelExp");
        if (ctx == null) {
            return null;
        }
        ParallelExp parallelExp = new ParallelExp();
        ctx.expression().forEach(ele -> parallelExp.expList.add((Expression) visitExpression(ele)));
        return parallelExp;
    }

    @Override
    public ASTNode visitSelectStatement(MxParser.SelectStatementContext ctx) {
        if (ctx == null) {
            return null;
        }
        SelectStatement selectStatement = new SelectStatement();
        selectStatement.judgeExp = (Expression) visitExpression(ctx.expression());
        selectStatement.trueStmt = (Statement) visitStatement(ctx.statement(0));
        if (ctx.statement().size() == 2) {
            selectStatement.falseStmt = (Statement) visitStatement(ctx.statement(1));
        }
        return selectStatement;
    }

    @Override
    public ASTNode visitWhileLoop(MxParser.WhileLoopContext ctx) {
        if (ctx == null) {
            return null;
        }
        WhileLoop whileLoop = new WhileLoop();
        whileLoop.judgeExp = (Expression) visitExpression(ctx.expression());
        whileLoop.stmt = (Statement) visitStatement(ctx.statement());
        return whileLoop;
    }

    @Override
    public ASTNode visitForLoop(MxParser.ForLoopContext ctx) {
        if (ctx == null) {
            return null;
        }
        ForLoop forLoop = new ForLoop();
        forLoop.parallelExp = (ParallelExp) visitParallelExp(ctx.parallelExp());
        forLoop.variableDef = (VariableDef) visitVariableDef(ctx.variableDef());
        forLoop.conditionExp = (Expression) visitExpression(ctx.condition);
        forLoop.stepExp = (Expression) visitExpression(ctx.step);
        forLoop.stmt = (Statement) visitStatement(ctx.statement());
        return forLoop;
    }

    public ASTNode visitLoopStatement(MxParser.LoopStatementContext ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx instanceof MxParser.WhileLoopContext) {
            return visitWhileLoop((MxParser.WhileLoopContext) ctx);
        } else if (ctx instanceof MxParser.ForLoopContext) {
            return visitForLoop((MxParser.ForLoopContext) ctx);
        } else {
            return null;
        }
    }

    public ASTNode visitJumpStatement(MxParser.JumpStatementContext ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx instanceof MxParser.ReturnStmtContext) {
            return visitReturnStmt((MxParser.ReturnStmtContext) ctx);
        } else if (ctx instanceof MxParser.BreakStmtContext) {
            return visitBreakStmt((MxParser.BreakStmtContext) ctx);
        } else if (ctx instanceof MxParser.ContinueStmtContext) {
            return visitContinueStmt((MxParser.ContinueStmtContext) ctx);
        } else {
            return null;
        }
    }

    @Override
    public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        ReturnStmt returnStmt = new ReturnStmt();
        returnStmt.returnExp = (Expression) visitExpression(ctx.expression());
        return returnStmt;
    }

    @Override
    public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return new BreakStmt();
    }

    @Override
    public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return new ContinueStmt();
    }
}
