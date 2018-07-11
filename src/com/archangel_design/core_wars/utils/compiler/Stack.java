package com.archangel_design.core_wars.utils.compiler;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private List<Instruction> instructions = new ArrayList<>();

    private int size = 0;
    private int currentInstruction = 0;

    public void addInstruction(Instruction inst) {
        instructions.add(size, inst);
        size++;
    }

    public void addInstruction(Instruction inst, int index) {
        instructions.add(index, inst);
        size = instructions.size();
    }

    public Instruction getCurrentInstruction() {
        return instructions.get(currentInstruction);
    }

    public Instruction getNext() {
        if (endOfStack())
            resetStack();
        Instruction i = instructions.get(currentInstruction);
        currentInstruction++;

        return i;
    }

    private boolean endOfStack() {
        return currentInstruction == size;
    }

    private void resetStack() {
        currentInstruction = 0;
    }

    /**
     * Conditional instruction has been evaluated to false
     * thus we move outside of the scope of this and any
     * nested conditional instructions
     */
    public void conditionNotMet() {
        int nestedConditions = 0;
        for (;;) {
            if (getCurrentInstruction().getType() == InstructionType.CONDITION_END)
                nestedConditions--;

            if (nestedConditions < 0)
                return;

            if (getCurrentInstruction().getType() == InstructionType.CONDITION_START)
                nestedConditions++;

            getNext();
        }
    }

    public void reset() {
        currentInstruction = 0;
    }
}
