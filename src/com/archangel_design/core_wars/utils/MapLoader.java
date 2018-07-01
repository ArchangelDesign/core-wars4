package com.archangel_design.core_wars.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    public static Map loadMap(String filename) throws IOException {
        byte[] bytes = readFile(filename);

        return readMap(bytes);
    }

    public static void saveMap(Map map, String filename) throws IOException {
        File f = new File(filename);

        FileOutputStream os = new FileOutputStream(f);
        os.write(convertToBytes(map));
    }

    private static boolean validFile(String filename) {
        return true;
    }

    private static byte[] readFile(String filename) throws IOException {
        Path path = Paths.get(filename);

        return Files.readAllBytes(path);
    }

    private static Map readMap(byte[] bytes) throws IOException {
        if (bytes.length < 2)
            throw new IOException("Invalid cw4 file.");

        Byte width = bytes[0];
        Byte height = bytes[1];

        Map map = new Map(width.intValue(), height.intValue());

        return map;
    }

    private static byte[] convertToBytes(Map map) {
        List<Byte> result = new ArrayList<>();

        result.add(map.getWitdth().byteValue());
        result.add(map.getHeight().byteValue());

        return convert(result);
    }

    private static Byte[] convert(byte[] bytes) {
        Byte[] result = new Byte[bytes.length];
        for (int i=0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }

        return result;
    }

    private static byte[] convert(List<Byte> bytes) {
        byte[] result = new byte[bytes.size()];
        for (int i=0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }

        return result;
    }
}
