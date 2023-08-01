package src.AST;

import java.util.ArrayList;
import java.util.List;

public class ParallelExp extends ASTNode {
    public List<Expression> expList;

    public ParallelExp() {
        expList = new ArrayList<>();
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
