package src.IR.statement;

import java.util.LinkedList;
import java.util.List;

public class ConstString extends IRStatement {
    public List<String> constStringList;//调用下标为i: @constString-i

    public ConstString() {
        constStringList = new LinkedList<>();
    }

    public String push(String content) {
        constStringList.add(content);
        return "@constString-" + (constStringList.size() - 1);
    }
}
