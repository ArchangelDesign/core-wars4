package com.archangel_design.core_wars.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    public static void loadMap(Map map, String filename) {

    }

    public static void saveMap(Map map, String filename) {
        Byte[] bytes = convertToBytes(map);
    }

    private static boolean validFile(String filename) {
        return true;
    }

    private static Byte[] readFile(String filename) throws IOException {
        Path path = Paths.get(filename);

        return convert(Files.readAllBytes(path));
    }

    private static Byte[] convertToBytes(Map map) {
        List<Byte> result = new ArrayList<>();

        result.add(map.getWitdth().byteValue());
        result.add(map.getHeight().byteValue());

        return (Byte[]) result.toArray();
    }

    private static Byte[] convert(byte[] bytes) {
        Byte[] result = new Byte[bytes.length];
        for (int i=0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }

        return result;
    }
}
