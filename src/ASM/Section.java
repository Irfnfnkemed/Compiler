package src.ASM;

import src.ASM.instruction.ASMInstr;

import java.util.ArrayList;
import java.util.List;

public class Section {
    public String sectionName;
    public List<String> globalList;
    public List<ASMInstr> ASMInstrList;

    public Section(String sectionName_) {
        sectionName = sectionName_;
        globalList = new ArrayList<>();
        ASMInstrList = new ArrayList<>();
    }

    public void pushGlobal(String name) {
        globalList.add(name);
    }

    public void pushInstr(ASMInstr asmInstr) {
        ASMInstrList.add(asmInstr);
    }
}
