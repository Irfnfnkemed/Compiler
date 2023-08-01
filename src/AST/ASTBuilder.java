package src.AST;

import org.antlr.v4.runtime.tree.ParseTree;
import src.paser.MxBaseVisitor;
import src.paser.MxParser;
import src.AST.expression.*;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {
    ASTNode ASTprogram;

    public void build(ParseTree ctx) {
        ASTprogram = visit(ctx);
    }

    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        Program program = new Program();
        program.mainDef = (MainDef) visitMainDef(ctx.mainDef());
        ctx.classDef().forEach(ele -> program.classDefList.add((ClassDef) visitClassDef(ele)));
        ctx.functionDef().forEach(ele -> program.functionDefList.add((FunctionDef) visitFunctionDef(ele)));
        ctx.variableDef().forEach(ele -> program.variableDefList.add((VariableDef) visitVariableDef(ele)));
        return program;
    }

    @Override
    public ASTNode visitMainDef(MxParser.MainDefContext ctx) {
        MainDef mainDef = new MainDef();
        mainDef.suite = (Suite) visitSuite(ctx.suite());
        return mainDef;
    }

    @Override
    public ASTNode visitClassDef(MxParser.ClassDefContext ctx) {
        ClassDef classDef = new ClassDef();
        return classDef;
    }

    @Override
    public ASTNode visitVariableDef(MxParser.VariableDefContext ctx) {
        VariableDef variableDef = new VariableDef();
        variableDef.type = (Type) visitType(ctx.typeName());
        System.out.println(variableDef.type.typeEnum);
        System.out.println(variableDef.type.typeName);
        System.out.println(variableDef.type.dim);
        return variableDef;
    }

    @Override
    public ASTNode visitSuite(MxParser.SuiteContext ctx) {
        Suite suite = new Suite();
        ctx.statement().forEach(ele -> suite.statementList.add((Statement) visitStatement(ele)));
        return suite;
    }

    @Override
    public ASTNode visitStatement(MxParser.StatementContext ctx) {
        Statement statement = new Statement();
        if (ctx.parallelExp() != null) {
            ParallelExp s = (ParallelExp) visitParallelExp(ctx.parallelExp());
            int y = 1 + 3;
        }
        return statement;
    }

    @Override
    public ASTNode visitFunctionDef(MxParser.FunctionDefContext ctx) {
        FunctionDef functionDef = new FunctionDef();
        return functionDef;
    }

    public ASTNode visitType(MxParser.TypeNameContext ctx) {
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
        System.out.println("FundationType");
        Type type = new Type(ctx.VOID(), ctx.BOOL(), ctx.INT(), ctx.STRING());
        return type;
    }

    @Override
    public ASTNode visitClassType(MxParser.ClassTypeContext ctx) {
        System.out.println("ClassType");
        Type type = new Type(ctx.Identifier().getSymbol().getText());
        return type;
    }

    @Override
    public ASTNode visitArrayType(MxParser.ArrayTypeContext ctx) {
        System.out.println("ArrayType");
        Type type = new Type((Type) visitType(ctx.typeName()), ctx.brackets().size());
        return type;
    }

    public ASTNode visitExpression(MxParser.ExpressionContext ctx) {
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
        System.out.println("visitFunctionCallLhsExp");
        FunctionCallLhsExp functionCallLhsExp = new FunctionCallLhsExp();
        functionCallLhsExp.functionName = ctx.Identifier().getText();
        functionCallLhsExp.callExpList = (ParallelExp) visitParallelExp(ctx.parallelExp());
        return functionCallLhsExp;
    }

    @Override
    public ASTNode visitBoolExp(MxParser.BoolExpContext ctx) {
        System.out.println("visitBoolExp");
        BoolExp boolExp = new BoolExp(ctx.TRUE(), ctx.FALSE());
        return boolExp;
    }

    @Override
    public ASTNode visitClassMemberLhsExp(MxParser.ClassMemberLhsExpContext ctx) {
        System.out.println("visitClassMemberLhsExp");
        ClassMemberLhsExp classMemberLhsExp = new ClassMemberLhsExp();
        classMemberLhsExp.classVariable = (Expression) visitExpression(ctx.expression());
        classMemberLhsExp.memberName = ctx.Identifier().getText();
        return classMemberLhsExp;
    }

    @Override
    public ASTNode visitNewArrayExp(MxParser.NewArrayExpContext ctx) {
        System.out.println("visitNewArrayExp");
        NewArrayExp newArrayExp = new NewArrayExp();
        newArrayExp.type = (Type) visitType(ctx.typeName());
        ctx.expression().forEach(ele -> newArrayExp.expressionList.add((Expression) visitExpression(ele)));
        newArrayExp.dim = newArrayExp.type.dim + newArrayExp.expressionList.size();
        return newArrayExp;
    }

    @Override
    public ASTNode visitStringExp(MxParser.StringExpContext ctx) {
        System.out.println("visitStringExp");
        StringExp stringExp = new StringExp();
        stringExp.value = ctx.String().getText();
        return stringExp;
    }

    @Override
    public ASTNode visitThisPointerExp(MxParser.ThisPointerExpContext ctx) {
        System.out.println("visitThisPointerExp");
        ThisPointerExp thisPointerExp = new ThisPointerExp();
        return thisPointerExp;
    }

    @Override
    public ASTNode visitPrefixLhsExp(MxParser.PrefixLhsExpContext ctx) {
        System.out.println("visitPrefixLhsExp");
        PrefixLhsExp prefixLhsExp = new PrefixLhsExp();
        prefixLhsExp.exp = (Expression) visitExpression(ctx.expression());
        prefixLhsExp.op = ctx.op.getText();
        return prefixLhsExp;
    }

    @Override
    public ASTNode visitNullExp(MxParser.NullExpContext ctx) {
        System.out.println("visitNullExp");
        NullExp nullExp = new NullExp();
        return nullExp;
    }

    @Override
    public ASTNode visitVariableLhsExp(MxParser.VariableLhsExpContext ctx) {
        System.out.println("visitVariableLhsExp");
        VariableLhsExp variableLhsExp = new VariableLhsExp(ctx.Identifier().getText());
        return variableLhsExp;
    }

    @Override
    public ASTNode visitClassMemFunctionLhsExp(MxParser.ClassMemFunctionLhsExpContext ctx) {
        System.out.println("visitClassMemFunctionLhsExp");
        ClassMemberLhsExp classMemberLhsExp = new ClassMemberLhsExp();
        classMemberLhsExp.memberName = ctx.Identifier().getText();
        classMemberLhsExp.classVariable = (Expression) visitExpression(ctx.expression());
        return classMemberLhsExp;
    }

    @Override
    public ASTNode visitBinaryExp(MxParser.BinaryExpContext ctx) {
        System.out.println("visitBinaryExp");
        BinaryExp binaryExp = new BinaryExp();
        binaryExp.lhs = (Expression) visitExpression(ctx.expression(0));
        binaryExp.rhs = (Expression) visitExpression(ctx.expression(1));
        binaryExp.op = ctx.op.getText();
        return binaryExp;
    }

    @Override
    public ASTNode visitPrimaryExp(MxParser.PrimaryExpContext ctx) {
        System.out.println("visitPrimaryExp");
        PrimaryExp primaryExp = new PrimaryExp();
        primaryExp.exp = (Expression) visitExpression(ctx.expression());
        return primaryExp;
    }

    @Override
    public ASTNode visitPostfixExp(MxParser.PostfixExpContext ctx) {
        System.out.println("visitPostfixExp");
        PostfixExp postfixExp = new PostfixExp();
        postfixExp.exp = (Expression) visitExpression(ctx.expression());
        postfixExp.op = ctx.op.getText();
        return postfixExp;
    }

    @Override
    public ASTNode visitAssignExp(MxParser.AssignExpContext ctx) {
        System.out.println("visitAssignExp");
        AssignExp assignExp = new AssignExp();
        assignExp.lhs = (Expression) visitExpression(ctx.expression(0));
        assignExp.rhs = (Expression) visitExpression(ctx.expression(1));
        return assignExp;
    }

    @Override
    public ASTNode visitNewClassExp(MxParser.NewClassExpContext ctx) {
        System.out.println("visitNewClassExp");
        NewClassExp newClassExp = new NewClassExp();
        return newClassExp;
    }

    @Override
    public ASTNode visitTernaryExp(MxParser.TernaryExpContext ctx) {
        System.out.println("visitTernaryExp");
        TernaryExp ternaryExp = new TernaryExp();
        ternaryExp.lhs = (Expression) visitExpression(ctx.expression(0));
        ternaryExp.mhs = (Expression) visitExpression(ctx.expression(1));
        ternaryExp.rhs = (Expression) visitExpression(ctx.expression(2));
        return ternaryExp;
    }

    @Override
    public ASTNode visitArrayElementLhsExp(MxParser.ArrayElementLhsExpContext ctx) {
        System.out.println("visitArrayElementLhsExp");
        ArrayElementLhsExp arrayElementLhsExp = new ArrayElementLhsExp();
        arrayElementLhsExp.variable = (Expression) visitExpression(ctx.expression(0));
        arrayElementLhsExp.index = (Expression) visitExpression(ctx.expression(1));
        return arrayElementLhsExp;
    }

    @Override
    public ASTNode visitUnaryExp(MxParser.UnaryExpContext ctx) {
        System.out.println("visitUnaryExp");
        UnaryExp unaryExp = new UnaryExp();
        unaryExp.exp = (Expression) visitExpression(ctx.expression());
        unaryExp.op = ctx.op.getText();
        return unaryExp;
    }

    @Override
    public ASTNode visitNumberExp(MxParser.NumberExpContext ctx) {
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
}
