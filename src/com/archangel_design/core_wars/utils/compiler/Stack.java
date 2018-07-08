package com.archangel_design.core_wars.utils.compiler;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private List<Instruction> instructions = new ArrayList<>();

    private int size = 0;

    public void addInstruction(Instruction inst) {
        instructions.add(size, inst);
        size++;
    }

    public void addInstruction(Instruction inst, int index) {
        instructions.add(index, inst);
        size = instructions.size();
    }
}
