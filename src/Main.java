package src;

import static test.TestIR.testIR;

public class Main {
    public static void main(String[] args) throws Exception {
//        Scanner scanner = new Scanner(System.in);
//        try {
//            // 创建一个文件输出流，并指定输出文件的路径
//            FileOutputStream fileOutputStream = new FileOutputStream("./src/builtin/test.ll");
//
//            // 创建一个 PrintStream 对象，用于将输出写入文件输出流
//            PrintStream printStream = new PrintStream(fileOutputStream);
//
//            // 将 System.out 设置为新的 PrintStream 对象
//            System.setOut(printStream);
//
//            String name = "./src/test";
//            InputStream inputStream = new FileInputStream(name);
//            MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream));
//            lexer.removeErrorListeners();
//            lexer.addErrorListener(new ParserErrorListener());
//            MxParser parser = new MxParser(new CommonTokenStream(lexer));
//            parser.removeErrorListeners();
//            parser.addErrorListener(new ParserErrorListener());
//            ParseTree ctx = parser.program();
//            ASTBuilder AST = new ASTBuilder(ctx);
//            Semantic semantic = new Semantic(AST.ASTProgram);
//            semantic.check();
//            var a = new IRBuilder(AST.ASTProgram, semantic.globalScope);
//            var b = new IRPrinter(a.irProgram);
//            b.print();
//        } catch (Errors errors) {
//            System.err.println(errors.toString());
//        }
        // testSemantic();
        testIR();
    }
}

