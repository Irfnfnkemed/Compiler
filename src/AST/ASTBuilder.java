package src.AST;

import org.antlr.v4.runtime.tree.ParseTree;
import src.AST.definition.*;
import src.AST.statement.Suite;
import src.AST.statement.jumpStatement.*;
import src.AST.statement.loopStatement.*;
import src.AST.statement.selectStatement.SelectStatement;
import src.AST.statement.Statement;
import src.Util.type.Type;
import src.AST.definition.variableDef.InitVariable;
import src.AST.definition.variableDef.VariableDef;
import src.Util.position.Position;
import src.paser.MxBaseVisitor;
import src.paser.MxParser;
import src.AST.expression.*;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {
    public Program ASTProgram;

    public void build(ParseTree ctx) {
        ASTProgram = (Program) visit(ctx);
    }

    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        if (ctx == null) {
            return null;
        }
        Program program = new Program();
        program.position = new Position(ctx);
        ctx.definition().forEach(ele -> program.defList.add((Definition) visitDefinition(ele)));
        return program;
    }

    @Override
    public ASTNode visitMainDef(MxParser.MainDefContext ctx) {
        if (ctx == null) {
            return null;
        }
        MainDef mainDef = new MainDef();
        mainDef.position = new Position(ctx);
        mainDef.suite = (Suite) visitSuite(ctx.suite());
        return mainDef;
    }

    @Override
    public ASTNode visitClassDef(MxParser.ClassDefContext ctx) {
        if (ctx == null) {
            return null;
        }
        ClassDef classDef = new ClassDef();
        classDef.position = new Position(ctx);
        classDef.className = ctx.Identifier().getText();
        classDef.constructor = (Constructor) visitConstructor(ctx.constructor());
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
        variableDef.position = new Position(ctx);
        variableDef.type = visitType(ctx.typeName());
        ctx.initVariable().forEach(ele -> {
            var varInit = (InitVariable) visitInitVariable(ele);
            varInit.type = new Type(variableDef.type);
            variableDef.initVariablelist.add(varInit);
        });
        return variableDef;
    }

    @Override
    public ASTNode visitInitVariable(MxParser.InitVariableContext ctx) {
        if (ctx == null) {
            return null;
        }
        InitVariable initVariable = new InitVariable();
        initVariable.position = new Position(ctx);
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
        suite.position = new Position(ctx);
        ctx.statement().forEach(ele -> suite.statementList.add((Statement) visitStatement(ele)));
        return suite;
    }

    @Override
    public ASTNode visitStatement(MxParser.StatementContext ctx) {
        if (ctx == null) {
            return null;
        }
        Statement statement = new Statement();
        statement.position = new Position(ctx);
        statement.suite=(Suite)visitSuite(ctx.suite());
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
        functionDef.position = new Position(ctx);
        functionDef.type = visitType(ctx.typeName(0));
        functionDef.functionName = ctx.Identifier(0).getText();
        ctx.typeName().stream().skip(1).forEach(ele -> functionDef.parameterTypeList.add((Type) visitType(ele)));
        ctx.Identifier().stream().skip(1).forEach(ele -> functionDef.parameterNameList.add(ele.getText()));
        functionDef.body = (Suite) visitSuite(ctx.suite());
        return functionDef;
    }

    public Type visitType(MxParser.TypeNameContext ctx) {
        if (ctx == null) {
            return null;
        }
        Type type;
        if (ctx instanceof MxParser.FoundationTypeContext) {
            type = FoundationType((MxParser.FoundationTypeContext) ctx);
        } else if (ctx instanceof MxParser.ClassTypeContext) {
            type = ClassType((MxParser.ClassTypeContext) ctx);
        } else if (ctx instanceof MxParser.ArrayTypeContext) {
            type = ArrayType((MxParser.ArrayTypeContext) ctx);
        } else {
            return null;
        }
        type.position = new Position(ctx);
        return type;
    }


    public Type FoundationType(MxParser.FoundationTypeContext ctx) {
        if (ctx == null) {
            return null;
        }
        Type type = new Type();
        if (ctx.VOID() != null) {
            return type.setVoid();
        } else if (ctx.BOOL() != null) {
            return type.setBool();
        } else if (ctx.INT() != null) {
            return type.setInt();
        } else if (ctx.STRING() != null) {
            return type.setString();
        } else {
            return null;
        }
    }

    public Type ClassType(MxParser.ClassTypeContext ctx) {
        if (ctx == null) {
            return null;
        }
        Type type = new Type();
        return type.setClass(ctx.Identifier().getText());
    }

    public Type ArrayType(MxParser.ArrayTypeContext ctx) {
        if (ctx == null) {
            return null;
        }
        Type type = new Type();
        return type.setArray(visitType(ctx.typeName()), ctx.brackets().size());
    }

    public ASTNode visitExpression(MxParser.ExpressionContext ctx) {
        if (ctx == null) {
            return null;
        }
        Expression expression;
        if (ctx instanceof MxParser.VariableLhsExpContext) {
            expression = (Expression) visitVariableLhsExp((MxParser.VariableLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.ThisPointerExpContext) {
            expression = (Expression) visitThisPointerExp((MxParser.ThisPointerExpContext) ctx);
        } else if (ctx instanceof MxParser.NumberExpContext) {
            expression = (Expression) visitNumberExp((MxParser.NumberExpContext) ctx);
        } else if (ctx instanceof MxParser.StringExpContext) {
            expression = (Expression) visitStringExp((MxParser.StringExpContext) ctx);
        } else if (ctx instanceof MxParser.BoolExpContext) {
            expression = (Expression) visitBoolExp((MxParser.BoolExpContext) ctx);
        } else if (ctx instanceof MxParser.NullExpContext) {
            expression = (Expression) visitNullExp((MxParser.NullExpContext) ctx);
        } else if (ctx instanceof MxParser.PrimaryExpContext) {
            expression = (Expression) visitPrimaryExp((MxParser.PrimaryExpContext) ctx);
        } else if (ctx instanceof MxParser.ClassMemberLhsExpContext) {
            expression = (Expression) visitClassMemberLhsExp((MxParser.ClassMemberLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.ClassMemFunctionLhsExpContext) {
            expression = (Expression) visitClassMemFunctionLhsExp((MxParser.ClassMemFunctionLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.ArrayElementLhsExpContext) {
            expression = (Expression) visitArrayElementLhsExp((MxParser.ArrayElementLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.FunctionCallLhsExpContext) {
            expression = (Expression) visitFunctionCallLhsExp((MxParser.FunctionCallLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.PostfixExpContext) {
            expression = (Expression) visitPostfixExp((MxParser.PostfixExpContext) ctx);
        } else if (ctx instanceof MxParser.PrefixLhsExpContext) {
            expression = (Expression) visitPrefixLhsExp((MxParser.PrefixLhsExpContext) ctx);
        } else if (ctx instanceof MxParser.UnaryExpContext) {
            expression = (Expression) visitUnaryExp((MxParser.UnaryExpContext) ctx);
        } else if (ctx instanceof MxParser.BinaryExpContext) {
            expression = (Expression) visitBinaryExp((MxParser.BinaryExpContext) ctx);
        } else if (ctx instanceof MxParser.TernaryExpContext) {
            expression = (Expression) visitTernaryExp((MxParser.TernaryExpContext) ctx);
        } else if (ctx instanceof MxParser.AssignExpContext) {
            expression = (Expression) visitAssignExp((MxParser.AssignExpContext) ctx);
        } else if (ctx instanceof MxParser.NewClassExpContext) {
            expression = (Expression) visitNewClassExp((MxParser.NewClassExpContext) ctx);
        } else if (ctx instanceof MxParser.NewArrayExpContext) {
            expression = (Expression) visitNewArrayExp((MxParser.NewArrayExpContext) ctx);
        } else {
            return null;
        }
        expression.position = new Position(ctx);
        return expression;
    }

    @Override
    public ASTNode visitFunctionCallLhsExp(MxParser.FunctionCallLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
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
        BoolExp boolExp = new BoolExp(ctx.TRUE(), ctx.FALSE());
        boolExp.type = new Type();
        boolExp.type.setBool();
        return boolExp;
    }

    @Override
    public ASTNode visitClassMemberLhsExp(MxParser.ClassMemberLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        ClassMemberLhsExp classMemberLhsExp = new ClassMemberLhsExp();
        classMemberLhsExp.classVariable = (Expression) visitExpression(ctx.expression());
        classMemberLhsExp.memberName = ctx.Identifier().getText();
        classMemberLhsExp.isAssign = true;
        return classMemberLhsExp;
    }

    @Override
    public ASTNode visitNewArrayExp(MxParser.NewArrayExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        NewArrayExp newArrayExp = new NewArrayExp();
        newArrayExp.baseType = visitType(ctx.typeName());
        ctx.expression().forEach(ele -> newArrayExp.expressionList.add((Expression) visitExpression(ele)));
        newArrayExp.type = new Type(newArrayExp.baseType);
        newArrayExp.type.dim = newArrayExp.baseType.dim + ctx.brackets().size() + newArrayExp.expressionList.size();
        return newArrayExp;
    }

    @Override
    public ASTNode visitStringExp(MxParser.StringExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        StringExp stringExp = new StringExp();
        stringExp.value = ctx.String().getText();
        stringExp.type = new Type();
        stringExp.type.setString();
        return stringExp;
    }

    @Override
    public ASTNode visitThisPointerExp(MxParser.ThisPointerExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        ThisPointerExp thisPointerExp = new ThisPointerExp();
        return thisPointerExp;
    }

    @Override
    public ASTNode visitPrefixLhsExp(MxParser.PrefixLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
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
        NullExp nullExp = new NullExp();
        nullExp.type = new Type();
        nullExp.type.setNull();
        return nullExp;
    }

    @Override
    public ASTNode visitVariableLhsExp(MxParser.VariableLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        VariableLhsExp variableLhsExp = new VariableLhsExp(ctx.Identifier().getText());
        return variableLhsExp;
    }

    @Override
    public ASTNode visitClassMemFunctionLhsExp(MxParser.ClassMemFunctionLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        ClassMemFunctionLhsExp classMemFunctionLhsExp = new ClassMemFunctionLhsExp();
        classMemFunctionLhsExp.classVariable = (Expression) visitExpression(ctx.expression());
        classMemFunctionLhsExp.memberFuncName = ctx.Identifier().getText();
        classMemFunctionLhsExp.callList = (ParallelExp) visitParallelExp(ctx.parallelExp());
        return classMemFunctionLhsExp;
    }

    @Override
    public ASTNode visitBinaryExp(MxParser.BinaryExpContext ctx) {
        if (ctx == null) {
            return null;
        }
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
        PrimaryExp primaryExp = new PrimaryExp();
        primaryExp.exp = (Expression) visitExpression(ctx.expression());
        primaryExp.isAssign = primaryExp.exp.isAssign;
        return primaryExp;
    }

    @Override
    public ASTNode visitPostfixExp(MxParser.PostfixExpContext ctx) {
        if (ctx == null) {
            return null;
        }
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
        NewClassExp newClassExp = new NewClassExp();
        newClassExp.type = (Type) visitType(ctx.typeName());
        return newClassExp;
    }

    @Override
    public ASTNode visitTernaryExp(MxParser.TernaryExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        TernaryExp ternaryExp = new TernaryExp();
        ternaryExp.condition = (Expression) visitExpression(ctx.expression(0));
        ternaryExp.trueExp = (Expression) visitExpression(ctx.expression(1));
        ternaryExp.falseExp = (Expression) visitExpression(ctx.expression(2));
        return ternaryExp;
    }

    @Override
    public ASTNode visitArrayElementLhsExp(MxParser.ArrayElementLhsExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        ArrayElementLhsExp arrayElementLhsExp = new ArrayElementLhsExp();
        arrayElementLhsExp.variable = (Expression) visitExpression(ctx.expression(0));
        arrayElementLhsExp.index = (Expression) visitExpression(ctx.expression(1));
        arrayElementLhsExp.isAssign = true;
        return arrayElementLhsExp;
    }

    @Override
    public ASTNode visitUnaryExp(MxParser.UnaryExpContext ctx) {
        if (ctx == null) {
            return null;
        }
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
        NumberExp numberExp = new NumberExp(ctx.DecNumber().getText());
        numberExp.type = new Type();
        numberExp.type.setInt();
        return numberExp;
    }


    @Override
    public ASTNode visitParallelExp(MxParser.ParallelExpContext ctx) {
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
        selectStatement.position = new Position(ctx);
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
        LoopStatement loopStatement;
        if (ctx instanceof MxParser.WhileLoopContext) {
            loopStatement = (LoopStatement) visitWhileLoop((MxParser.WhileLoopContext) ctx);
        } else if (ctx instanceof MxParser.ForLoopContext) {
            loopStatement = (LoopStatement) visitForLoop((MxParser.ForLoopContext) ctx);
        } else {
            return null;
        }
        loopStatement.position = new Position(ctx);
        return loopStatement;
    }

    public ASTNode visitJumpStatement(MxParser.JumpStatementContext ctx) {
        if (ctx == null) {
            return null;
        }
        JumpStatement jumpStatement;
        if (ctx instanceof MxParser.ReturnStmtContext) {
            jumpStatement = (JumpStatement) visitReturnStmt((MxParser.ReturnStmtContext) ctx);
        } else if (ctx instanceof MxParser.BreakStmtContext) {
            jumpStatement = (JumpStatement) visitBreakStmt((MxParser.BreakStmtContext) ctx);
        } else if (ctx instanceof MxParser.ContinueStmtContext) {
            jumpStatement = (JumpStatement) visitContinueStmt((MxParser.ContinueStmtContext) ctx);
        } else {
            return null;
        }
        jumpStatement.position = new Position(ctx);
        return jumpStatement;
    }

    @Override
    public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        if (ctx == null) {
            return null;
        }
        ReturnStmt returnStmt = new ReturnStmt();
        returnStmt.returnExp = (Expression) visitExpression(ctx.expression());
        return returnStmt;
    }

    @Override
    public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        if (ctx == null) {
            return null;
        }
        BreakStmt breakStmt = new BreakStmt();
        return breakStmt;
    }

    @Override
    public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        if (ctx == null) {
            return null;
        }
        ContinueStmt continueStmt = new ContinueStmt();
        return continueStmt;
    }

    @Override
    public ASTNode visitConstructor(MxParser.ConstructorContext ctx) {
        if (ctx == null) {
            return null;
        }
        Constructor constructor = new Constructor();
        constructor.position = new Position(ctx);
        constructor.type = visitType(ctx.typeName());
        constructor.suite = (Suite) visitSuite(ctx.suite());
        return constructor;
    }

    @Override
    public ASTNode visitDefinition(MxParser.DefinitionContext ctx) {
        if (ctx == null) {
            return null;
        }
        Definition definition = new Definition();
        definition.mainDef = (MainDef) visitMainDef(ctx.mainDef());
        definition.classDef = (ClassDef) visitClassDef(ctx.classDef());
        definition.functionDef = (FunctionDef) visitFunctionDef(ctx.functionDef());
        definition.variableDef = (VariableDef) visitVariableDef(ctx.variableDef());
        definition.position = new Position(ctx);
        return definition;
    }
}
