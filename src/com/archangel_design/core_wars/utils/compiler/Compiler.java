package com.archangel_design.core_wars.utils.compiler;

import com.archangel_design.core_wars.utils.compiler.exception.MethodRedeclaredException;

import java.util.HashMap;

public class Compiler {

    private HashMap<String, Stack> functions = new HashMap<>();

    private HashMap<String, String> variables = new HashMap<>();

    private String currentMethod = null;

    public void addMethod(String name, Stack functionStack) throws MethodRedeclaredException {
        if (functions.containsKey(name))
            throw new MethodRedeclaredException(String.format("Method %s already declared.", name));
        functions.put(name, functionStack);
    }

    public HashMap<String, Stack> getMethods() {
        return functions;
    }

    public boolean hasVariable(String variable) {
        return variables.containsKey(variable);
    }

    public String getCurrentMethod() {
        if (currentMethod == null)
            // default method (entry point)
            return "loop";
        return currentMethod;
    }

    public Compiler setCurrentMethod(String currentMethod) {
        this.currentMethod = currentMethod;
        return this;
    }

    public String getVariableValue(String variableName) {
        return variables.get(variableName);
    }

    public void declareVariable(String name, String value) {
        variables.put(name, value);
    }

    public Stack getCurrentStack() {
        return functions.get(getCurrentMethod());
    }
}
