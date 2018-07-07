package com.archangel_design.core_wars.utils.compiler;

import com.archangel_design.core_wars.utils.compiler.exception.MethodRedeclaredException;

import java.util.HashMap;

public class Compiler {
    private HashMap<String, Stack> functions;

    public void addMethod(String name, Stack functionStack) throws MethodRedeclaredException {
        if (functions.containsKey(name))
            throw new MethodRedeclaredException(String.format("Method %s already declared.", name));
        functions.put(name, functionStack);
    }
}
