package src.ASM;

import src.ASM.instruction.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Section {
    public String sectionName;
    public List<String> globalList;
    public List<Instruction> instructionList;

    public Section(String sectionName_) {
        sectionName = sectionName_;
        globalList = new ArrayList<>();
        instructionList = new ArrayList<>();
    }

    public void pushGlobal(String name) {
        globalList.add(name);
    }
}
