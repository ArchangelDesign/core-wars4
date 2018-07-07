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
            String name = getAllMatches("(?<=function) [a-zA-Z]*(?=\\()", s).stream().findFirst().orElse("");
            String body = getAllMatches("(?<=\\{).*(?=\\})", s).stream().findFirst().orElse("");
            if (result.containsKey(name))
                throw new RuntimeException(String.format("Method %s already exists. Cannot redeclare.", name));
            result.put(name, body);
        });
        return result;
    }

    public static Stack readStack(String body) {
        Stack result = new Stack();
        List<String> methodCalls = getAllMatches("[a-zA-Z]*[ ]*\\([^\\).]*\\)[ ]*;", body);

        methodCalls.forEach(s -> {
            Instruction i = new Instruction(getMethodName(s));
            result.addInstruction(i);
        });
        return result;
    }

    public static String getMethodName(String parsedCall) {
        return getAllMatches("[a-zA-Z]*(?= *?)", parsedCall).stream().findFirst().orElse("");
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
