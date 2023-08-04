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
        String folderPath = "./test/testcases/sema";
        traverseFolder(new File(folderPath));
    }

    public static void traverseFolder(File folder) throws Exception {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    traverseFolder(file);
                }
            }
        } else {
            System.out.println(YELLOW + folder.getAbsolutePath().substring(folder.getAbsolutePath().indexOf("sema\\") + 5) + ": ");
            if (check(folder.getAbsolutePath())) {
                System.out.println(GREEN + "Pass!");
            } else {
                System.out.println(RED + "Fail!");
            }
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
            ASTBuilder build = new ASTBuilder();
            build.build(ctx);
            build.build(ctx);
            Semantic semantic = new Semantic(build.ASTProgram);
            semantic.visit(build.ASTProgram);
        } catch (Errors errors) {
            //System.err.println(errors.toString());
            return !success;
        }
        return success;
    }
}

