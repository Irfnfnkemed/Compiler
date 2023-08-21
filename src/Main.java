package src;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import src.ASM.ASMBuilder;
import src.ASM.ASMPrinter;
import src.AST.ASTBuilder;
import src.IR.IRBuilder;
import src.IR.IRPrinter;
import src.IR.statement.FuncDef;
import src.Mem2Reg.CFG;
import src.Mem2Reg.Dom;
import src.Mem2Reg.PutPhi;
import src.Util.error.Errors;
import src.Util.error.ParserErrorListener;
import src.parser.MxLexer;
import src.parser.MxParser;
import src.semantic.Semantic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static test.TestIR.testIR;

//import static test.TestIR.testIR;
//import static test.TestSemantic.testSemantic;

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
            var a = new IRBuilder(AST.ASTProgram, semantic.globalScope);
            var b = new IRPrinter(a.irProgram);
            // 创建一个文件输出流，并指定输出文件的路径
            FileOutputStream fileOutputStream = new FileOutputStream("./src/builtin/test_standard.ll");

            // 创建一个 PrintStream 对象，用于将输出写入文件输出流
            PrintStream printStream = new PrintStream(fileOutputStream);

            // 将 System.out 设置为新的 PrintStream 对象
            System.setOut(printStream);
            b.print();
            for (var stmt : a.irProgram.stmtList) {
                if (stmt instanceof FuncDef) {
                    var cfg = new CFG((FuncDef) stmt);
                    var dom = new Dom(cfg);
                    var putPhi = new PutPhi(dom,(FuncDef) stmt);
                    int p = 34;
                }
            }
            // 创建一个文件输出流，并指定输出文件的路径
             fileOutputStream = new FileOutputStream("./src/builtin/test.ll");

            // 创建一个 PrintStream 对象，用于将输出写入文件输出流
             printStream = new PrintStream(fileOutputStream);

            // 将 System.out 设置为新的 PrintStream 对象
            System.setOut(printStream);
            b.print();
//            var c = new ASMBuilder(a.irProgram);
//            var d = new ASMPrinter(c.asmProgram);
//            // 创建一个文件输出流，并指定输出文件的路径
//            fileOutputStream = new FileOutputStream("./src/builtin/test.s");
//
//            // 创建一个 PrintStream 对象，用于将输出写入文件输出流
//            printStream = new PrintStream(fileOutputStream);
//
//            // 将 System.out 设置为新的 PrintStream 对象
//            System.setOut(printStream);
//            d.print();
        } catch (Errors errors) {
            System.err.println(errors.toString());
        }
         //testSemantic();
       // testIR();
    }
}

