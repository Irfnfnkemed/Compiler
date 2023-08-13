package test;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import src.AST.ASTBuilder;
import src.IR.IRBuilder;
import src.IR.IRPrinter;
import src.Util.error.Errors;
import src.Util.error.ParserErrorListener;
import src.parser.MxLexer;
import src.parser.MxParser;
import src.semantic.Semantic;

import java.io.*;

public class TestIR {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static String folderPath = "./test/testcases/codegen/";

    public static void testIR() throws Exception {
        String filePath;
        try (BufferedReader reader = new BufferedReader(new FileReader(folderPath + "judgelist.txt"))) {
            while ((filePath = reader.readLine()) != null) {
                System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                System.out.println(YELLOW + filePath + ": ");
                if (generateIR(filePath)) {
                    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                    System.out.println(GREEN + "Successfully generate!");
                } else {
                    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                    System.out.println(RED + "Fail to generate!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean generateIR(String fileName) throws Exception {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./test/IRgen/" +
                    fileName.substring(0, fileName.indexOf(".mx")) + ".ll");
            PrintStream printStream = new PrintStream(fileOutputStream);
            System.setOut(printStream);
            InputStream inputStream = new FileInputStream(folderPath + fileName);
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
            IRBuilder irBuilder = new IRBuilder(AST.ASTProgram, semantic.globalScope);
            IRPrinter irPrinter = new IRPrinter(irBuilder.irProgram);
            irPrinter.print();
        } catch (Errors errors) {
            System.err.println(errors.toString());
            return false;
        }
        return true;
    }
}
