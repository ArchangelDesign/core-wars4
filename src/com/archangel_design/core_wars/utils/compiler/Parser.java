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

public class Parser {

    public static HashMap<String, String> loadMethods(String absolutePath) {
        String contents = readFile(absolutePath);
        HashMap<String, String> result = new HashMap<>();
        List<String> matches = getAllMatches("function [a-zA-Z]*\\(\\) ?\\{[^\\{.]*\\}", contents);

        matches.forEach(s -> {
            String name = getAllMatches("(?<=function) [a-zA-Z]*(?=\\()", s).stream().findFirst().orElse("");
            String body = getAllMatches("(?<=\\{).*(?=\\})", s).stream().findFirst().orElse("");
            result.put(name, body);
        });
        return result;
    }

    private static List<String> getAllMatches(String pattern, String content) {
        List<String> result = new ArrayList<>();
        Matcher m = Pattern.compile(pattern, Pattern.DOTALL)
                .matcher(content);
        while (m.find()) {
            result.add(m.group());
        }

        return result;
    }

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
