package src;

import static test.TestSemantic.testSemantic;

public class Main {
    public static void main(String[] args) throws Exception {
//        Scanner scanner = new Scanner(System.in);
//        try {
//            String name = "./src/test";
//            InputStream inputStream = new FileInputStream(name);
//            MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream));
//            lexer.removeErrorListeners();
//            lexer.addErrorListener(new AntlrErrorListener());
//            MxParser parser = new MxParser(new CommonTokenStream(lexer));
//            parser.removeErrorListeners();
//            parser.addErrorListener(new AntlrErrorListener());
//            ParseTree ctx = parser.program();
//            ASTBuilder build = new ASTBuilder();
//            build.build(ctx);
//            Semantic semantic = new Semantic(build.ASTProgram);
//            semantic.visit(build.ASTProgram);
//        } catch (Errors errors) {
//            System.err.println(errors.toString());
//        }
        testSemantic();
    }

}

