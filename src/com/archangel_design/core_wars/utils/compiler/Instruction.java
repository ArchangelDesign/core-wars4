package com.archangel_design.core_wars.utils.compiler;

import java.util.HashMap;

public class Instruction {
    private String name;

    private HashMap<String, String> arguments;

    private ConditionArgument conditionArgument;

    private InstructionType type = InstructionType.METHOD_CALL;

    public Instruction(String name) {
        this.name = name;
    }

    public Instruction(String name, HashMap<String, String> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public Instruction(String name, HashMap<String, String> arguments, InstructionType instructionType) {
        this.name = name;
        this.arguments = arguments;
        this.type = instructionType;
    }

    public Instruction(ConditionArgument conditionArgument) {
        this.name = "";
        this.type = InstructionType.CONDITION_START;
        this.conditionArgument = conditionArgument;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getArguments() {
        return arguments;
    }

    public ConditionArgument getConditionArgument() {
        return conditionArgument;
    }

    public InstructionType getType() {
        return type;
    }
}
