package com.archangel_design.core_wars.utils.compiler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Core Wars 4 Source Code Parser
 * Class is responsible for transforming
 * bug's source code into CW4 data structures
 */
public class Parser {

    private static final String REGEX_METHOD_CALL = "[a-zA-Z]*[ ]*\\([^\\).]*\\)[ ]*;";
    private static final String REGEX_CONDITION_CALL = "if_\\([^\\).]*\\)";
    private static final String REGEX_CONDITION_END_CALL = "endif;";
    private static final String REGEX_ASSIGNMENT = "\\$[a-zA-Z0-9_]*[ ]*=[ ]*[\\$a-zA-Z0-9_\\(\\)][ ]*;";
    private static final String REGEX_COMMENT = "\\/\\/.*?\n";

    /**
     * Parses given source file and returns a map where
     * key is a method's name and a value is a full body
     * of the method. If the method is not correctly formatted
     * it is not loaded. If the method already exists,
     * RuntimeException is thrown.
     *
     * @param absolutePath path to a source file
     * @return map of strings representing method's
     * body under a key representing method's name
     */
    public static HashMap<String, String> loadMethods(String absolutePath) {
        String contents = readFile(absolutePath);
        HashMap<String, String> result = new HashMap<>();
        List<String> matches = getAllMatches(
                "function [a-zA-Z]*\\(\\) ?\\{[^\\{.]*\\}",
                contents
        );

        matches.forEach(s -> {
            String name = getAllMatches("(?<=function )[a-zA-Z]*(?=\\()", s).stream().findFirst().orElse("");
            String body = getAllMatches("(?<=\\{).*(?=\\})", s).stream().findFirst().orElse("");
            if (result.containsKey(name))
                throw new RuntimeException(String.format("Method %s already exists. Cannot redeclare.", name));
            result.put(name, body);
        });
        return result;
    }

    /**
     * Returns stack for the parsed method from the source code
     *
     * @param body parsed method string
     * @return method's stack
     */
    public static Stack readStack(String body) {
        Stack result = new Stack();
        List<String> lines = getAllMatches("[^\\n.]*\\n", body);

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);
            if (isComment(line))
                continue;
            if (isMethodCall(line))
                addMethod(line, result);
            else if (isConditionalStartCall(line))
                addConditionStart(line, result);
            else if (isConditionalEndCall(line))
                addConditionStop(line, result);
            else if (isAssignment(line))
                addAssignment(line, result);
        }

        return result;
    }

    private static void addConditionStart(String rawLine, Stack stack) {
        stack.addInstruction(new Instruction(getConditionArguments(rawLine)));
    }

    private static void addConditionStop(String rawLine, Stack stack) {
        stack.addInstruction(new Instruction("", new HashMap<>(), InstructionType.CONDITION_END));
    }

    private static void addAssignment(String rawLine, Stack stack) {
        String variableName = getAllMatches("\\$[a-zA-Z0-9_]*", rawLine).stream().findFirst().orElse(null);
        if (variableName == null)
            return;
        String variableValue = getAllMatches("(?<=(\\$[a-zA-Z0-9_]{1,50}[ ]{0,5}=[ ]{0,5}))[\\$a-zA-Z0-9_]*(?=;)", rawLine).stream().findFirst().orElse(null);
        HashMap<String, String> args = new HashMap<>();
        args.put(variableValue, variableValue);
        stack.addInstruction(new Instruction(variableName, args, InstructionType.ASSIGNMENT));
    }

    private static void addMethod(String rawLine, Stack stack) {
        stack.addInstruction(
                new Instruction(
                        getMethodName(rawLine),
                        getMethodArguments(rawLine))
        );
    }

    public static boolean isMethodCall(String line) {
        return getAllMatches(REGEX_METHOD_CALL, line).size() > 0;
    }

    public static boolean isConditionalStartCall(String line) {
        return getAllMatches(REGEX_CONDITION_CALL, line).size() > 0;
    }

    public static boolean isConditionalEndCall(String line) {
        return getAllMatches(REGEX_CONDITION_END_CALL, line).size() > 0;
    }

    public static boolean isAssignment(String line) {
        return getAllMatches(REGEX_ASSIGNMENT, line).size() > 0;
    }

    public static boolean isComment(String line) {
        return getAllMatches(REGEX_COMMENT, line).size() > 0;
    }

    /**
     * Returns method name extracted from the method call
     *
     * @param parsedCall method call string
     * @return method name
     */
    public static String getMethodName(String parsedCall) {
        return getAllMatches("[a-zA-Z]*(?=([ ]{0,10}\\())", parsedCall)
                .stream().findFirst().orElse("");
    }

    /**
     * Returns argument of the conditional statement.
     * Currently only one is supported.
     *
     * @param rawLine conditional statement
     * @return arguments
     */
    public static ConditionArgument getConditionArguments(String rawLine) {
        String innerPart = getAllMatches("(?<=\\()[^\\).]*(?=\\))", rawLine).stream().findFirst().orElse("");

        if (innerPart.isEmpty())
            return null;

        String firstArg = getAllMatches("[a-zA-Z0-9\\$]*(?=[ \\=\\>\\<])", innerPart).stream().findFirst().orElse("");
        String secondArg = getAllMatches("(?<=[\\= \\<\\>]{1,100})[a-zA-Z0-9\\$]{1,100}", innerPart).stream().findFirst().orElse("");
        String operator = getAllMatches("(?<=[a-zA-Z0-9 \\$]{1,100})[\\=\\>\\<!]{1,}(?=[a-zA-Z0-9 \\$]{1,100})", innerPart).stream().findFirst().orElse("");

        return new ConditionArgument(firstArg, secondArg, operator);
    }

    /**
     * Returns arguments where key is a name
     * and value is always null (to be evaluated later by compiler)
     *
     * @param parsedCall method call
     * @return arguments
     */
    public static HashMap<String, String> getMethodArguments(String parsedCall) {
        HashMap<String, String> result = new HashMap<>();
        getAllMatches("(?<=([a-zA-Z]{2,100}[ ]{0,100}\\())[^\\).]*(?=\\))", parsedCall)
        .forEach(s -> {
            if (!s.isEmpty())
                result.put(s, null);
        });

        return result;
    }

    /**
     * Generic method for searching patterns in a given content
     *
     * @param pattern pattern to base on
     * @param content content to search in
     * @return list of matches
     */
    private static List<String> getAllMatches(String pattern, String content) {
        List<String> result = new ArrayList<>();
        Matcher m = Pattern.compile(pattern, Pattern.DOTALL)
                .matcher(content);
        while (m.find()) {
            result.add(m.group());
        }

        return result;
    }

    /**
     * Reads the entire file into the string
     *
     * @param path full path
     * @return file content
     */
    static String readFile(String path) {
        Charset encoding = Charset.defaultCharset();
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, encoding);
    }
}
