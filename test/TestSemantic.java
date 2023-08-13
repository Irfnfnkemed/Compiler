package test;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import src.AST.ASTBuilder;
import src.Util.error.ParserErrorListener;
import src.Util.error.Errors;
import src.paser.MxLexer;
import src.paser.MxParser;
import src.semantic.Semantic;

import java.io.*;

public class TestSemantic {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";

    public static void testSemantic() throws Exception {
        String folderPath = "./test/testcases/sema/";
        String filePath;
        try (BufferedReader reader = new BufferedReader(new FileReader("./test/testcases/sema/judgelist.txt"))) {
            while ((filePath = reader.readLine()) != null) {
                System.out.println(YELLOW + filePath + ": ");
                filePath = folderPath + filePath;
                if (check(filePath)) {
                    System.out.println(GREEN + "Pass!");
                } else {
                    System.out.println(RED + "Fail!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static boolean check(String fileName) throws Exception {
        boolean success = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int lineNumber = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                if (lineNumber == 6) {
                    String result = line.substring(line.indexOf(":") + 2);
                    success = result.equals("Success");
                    break; // 找到第六行后退出循环
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream inputStream = new FileInputStream(fileName);
            MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new ParserErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new ParserErrorListener());
            ParseTree ctx = parser.program();
            ASTBuilder AST = new ASTBuilder(ctx);
            new Semantic(AST.ASTProgram).check();
        } catch (Errors errors) {
            //System.err.println(errors.toString());
            return !success;
        }
        return success;
    }
}

