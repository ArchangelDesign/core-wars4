package com.archangel_design.core_wars.utils.compiler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void getMethodName() {
        String line = " methodName();";
        Assertions.assertEquals("methodName", Parser.getMethodName(line));
        line = "methodName ();";
        Assertions.assertEquals("methodName", Parser.getMethodName(line));
        line = "methodName ( ) ;";
        Assertions.assertEquals("methodName", Parser.getMethodName(line));
        line = " theMethod ($agrument, argument) ; ";
        Assertions.assertEquals("theMethod", Parser.getMethodName(line));
    }

    @Test
    public void getConditionArguments() {
        String line = "if_($name == CONSTANT)";
        ConditionArgument res = Parser.getConditionArguments(line);
        Assertions.assertEquals("$name", res.getArg1());
        Assertions.assertEquals("CONSTANT", res.getArg2());
        Assertions.assertEquals("==", res.getOperator());
    }

    @Test
    public void getMethodArguments() {
    }

    @Test
    public void isMethodCall() {
        String line = " theMethod ($agrument, argument) ; ";
        Assertions.assertEquals(true, Parser.isMethodCall(line));
    }
}