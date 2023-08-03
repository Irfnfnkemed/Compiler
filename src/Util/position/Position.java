package src.Util.position;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Position {
    public int line;
    public int column;

    public Position(ParserRuleContext ctx) {
        line = ctx.getStart().getLine();
        column = ctx.getStart().getCharPositionInLine();
    }
}
