package com.archangel_design.core_wars.utils.compiler;

import java.util.HashMap;

public class Instruction {
    private String name;

    private HashMap<String, String> arguments;

    private InstructionType type;

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
}
