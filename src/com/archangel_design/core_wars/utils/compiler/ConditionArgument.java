package com.archangel_design.core_wars.utils.compiler;

public class ConditionArgument {
    private String arg1;
    private String arg2;
    private String operator;

    public ConditionArgument(String arg1, String arg2, String operator) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.operator = operator;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getOperator() {
        return operator;
    }
}
