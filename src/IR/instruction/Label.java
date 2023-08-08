package src.IR.instruction;

import java.util.ArrayList;
import java.util.List;

public class Label extends Instruction {
    public String labelName;

    public Label(String labelName_) {
        labelName = labelName_;
    }
}
