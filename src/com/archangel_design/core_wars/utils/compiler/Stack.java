package com.archangel_design.core_wars.utils.compiler;

import java.util.List;

public class Stack {
    private List<Instruction> instructions;

    public void addInstruction(Instruction inst) {
        instructions.add(inst);
    }
}
