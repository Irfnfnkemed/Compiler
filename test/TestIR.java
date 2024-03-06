package test;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import src.AST.ASTBuilder;
import src.IR.IRBuilder;
import src.IR.IRPrinter;
import src.optimize.ADCE.ADCE;
import src.optimize.Mem2Reg.Mem2Reg;
import src.Util.error.Errors;
import src.Util.error.ParserErrorListener;
import src.parser.MxLexer;
import src.parser.MxParser;
import src.semantic.Semantic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestIR {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static String folderPath = "test/testcases/codegen/";

    public static void testIR() throws Exception {
        System.out.println(YELLOW + "=== Begin testing IR ===");
        create();
        String filePath;
        List<String> fail = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(folderPath + "judgelist.txt"))) {
            while ((filePath = reader.readLine()) != null) {
                System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                System.out.println(YELLOW + filePath + ": ");
                if (generateIR(filePath)) {
                    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                    System.out.println(GREEN + "Pass!");
                } else {
                    fail.add(filePath);
                    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                    System.out.println(RED + "Fail!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.out.println(YELLOW + "=== END ===");
        if (fail.isEmpty()) {
            System.out.println(GREEN + "All pass!");
        } else {
            System.out.println(RED + "Failed:");
            fail.forEach(file -> System.out.println(RED + file));
        }
        delete();
    }

    private static boolean create() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "wsl",
                    "mkdir", "IRgen"
            );
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println(RED + "mkdir false！");
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean delete() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "wsl",
                    "rm", "-rf", "./IRgen"
            );
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println(RED + "rm false！");
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean generateIR(String fileName) throws Exception {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("IRgen/tmp.ll");
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
            IRBuilder irBuilder = new IRBuilder(AST.ASTProgram, semantic.globalScope, semantic.inlineGlobalVar);
            IRPrinter irPrinter = new IRPrinter(irBuilder.irProgram);
            Mem2Reg mem2Reg = new Mem2Reg(irBuilder.irProgram);
            ADCE adce = new ADCE(irBuilder.irProgram);
            irPrinter.print();
            printStream.close();
            String input = extractContentFromFile(folderPath + fileName, "=== input ===");
            String output = extractContentFromFile(folderPath + fileName, "=== output ===");
            int exitCode = extractExitCodeFromFile(folderPath + fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("IRgen/tmp.in"))) {
                writer.write(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "wsl",
                    "clang-15", "src/builtin/builtin.ll", "IRgen/tmp.ll", "-o", "IRgen/code", "-m32"
            );
            Process process = processBuilder.start();
            int processExitCode = process.waitFor();
            if (processExitCode != 0) {
                System.err.println("clang error");
                return false;
            }
            processBuilder = new ProcessBuilder("wsl", "./IRgen/code");

            processBuilder.redirectInput(new File("IRgen/tmp.in"));
            processBuilder.redirectOutput(new File("IRgen/tmp.out"));
            process = processBuilder.start();
            processExitCode = process.waitFor();
            if (processExitCode != exitCode) {
                System.err.println("run error");
                return false;
            }
            byte[] contentBytes = Files.readAllBytes(Paths.get("IRgen/tmp.out"));
            String outputRun = new String(contentBytes);
            outputRun = outputRun.trim();
            if (!outputRun.equals(output)) {
                System.err.println("output error");
                return false;
            } else {
                return true;
            }
        } catch (Errors errors) {
            System.err.println(errors.toString());
            return false;
        }
    }

    private static String extractContentFromFile(String fileName, String startMarker) {
        StringBuilder content = new StringBuilder();
        boolean isReading = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(startMarker)) {
                    isReading = true;
                } else if (line.equals("=== end ===") && isReading) {
                    isReading = false;
                    break;  // 终止循环，避免读取多余内容
                } else if (isReading) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString().trim();
    }

    private static int extractExitCodeFromFile(String fileName) {
        int exitCode = -1;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ExitCode:")) {
                    exitCode = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exitCode;
    }
}
