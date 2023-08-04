package src;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import src.AST.ASTBuilder;
import src.Util.AntlrErrorListener;
import src.Util.error.Errors;
import src.Util.error.MxVistorErrors;
import src.paser.MxLexer;
import src.paser.MxParser;
import src.paser.MxVisitor;
import src.semantic.Semantic;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int select = scanner.nextInt();
        if (select == 1) {
            try {
                String name = "./src/test";
                InputStream inputStream = new FileInputStream(name);
                MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream));
                lexer.removeErrorListeners();
                lexer.addErrorListener(new AntlrErrorListener());
                MxParser parser = new MxParser(new CommonTokenStream(lexer));
                parser.removeErrorListeners();
                parser.addErrorListener(new AntlrErrorListener());
                ParseTree ctx = parser.program();
                ASTBuilder build = new ASTBuilder();
                build.build(ctx);
                Semantic semantic = new Semantic(build.ASTProgram);
                semantic.visit(build.ASTProgram);
            } catch (Errors errors) {
                System.err.println(errors.toString());
            }
        } else {
            // 指定要遍历的文件夹路径
            String folderPath = "./testcases/sema";

            // 调用遍历文件夹的方法
            traverseFolder(new File(folderPath));
        }
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
            lexer.addErrorListener(new AntlrErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new AntlrErrorListener());
            ParseTree ctx = parser.program();
            ASTBuilder build = new ASTBuilder();
            build.build(ctx);
            build.build(ctx);
            Semantic semantic = new Semantic(build.ASTProgram);
            semantic.visit(build.ASTProgram);
        } catch (Errors errors) {
            // System.err.println(errors.toString());
            return !success;
        }
        return success;
    }
}

