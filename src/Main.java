package src;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import src.ASM.ASMBuilder;
import src.ASM.ASMPrinter;
import src.AST.ASTBuilder;
import src.IR.IRBuilder;
import src.IR.IRPrinter;
import src.optimize.Mem2Reg.Mem2Reg;
import src.Util.error.Errors;
import src.Util.error.ParserErrorListener;
import src.optimize.RegAllocation.RegAllocation;
import src.parser.MxLexer;
import src.parser.MxParser;
import src.semantic.Semantic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        try {
            String name = "./src/test";
            InputStream inputStream = new FileInputStream(name);
            MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new ParserErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new ParserErrorListener());
            ParseTree ctx = parser.program();
            ASTBuilder AST = new ASTBuilder(ctx);
            Semantic semantic = new Semantic(AST.ASTProgram);
            semantic.check();
            var a = new IRBuilder(AST.ASTProgram, semantic.globalScope, semantic.inlineGlobalVar);
            var b = new IRPrinter(a.irProgram);
            FileOutputStream fileOutputStream = new FileOutputStream("./src/builtin/test_standard.ll");
            PrintStream printStream = new PrintStream(fileOutputStream);
            System.setOut(printStream);
            b.print();
            Mem2Reg mem2Reg = new Mem2Reg(a.irProgram);
            fileOutputStream = new FileOutputStream("./src/builtin/test.ll");
            printStream = new PrintStream(fileOutputStream);
            System.setOut(printStream);
            b.print();
            var c = new ASMBuilder(a.irProgram, a.useGlobalVar);
            var d = new ASMPrinter(c.asmProgram);
            fileOutputStream = new FileOutputStream("./src/builtin/test_standard.s");
            printStream = new PrintStream(fileOutputStream);
            System.setOut(printStream);
            d.print();
            var e = new RegAllocation(c);
            fileOutputStream = new FileOutputStream("src/builtin/test.s");
            printStream = new PrintStream(fileOutputStream);
            System.setOut(printStream);
            d.print();
        } catch (Errors errors) {
            System.err.println(errors.toString());
        }
        //testSemantic();
        //testIR();
    }
}

