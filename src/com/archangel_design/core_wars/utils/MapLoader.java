package com.archangel_design.core_wars.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapLoader {

    public static final String MAP_FOLDER = "maps";

    public static Map loadMap(String filename) throws IOException {
        byte[] bytes = readFile(filename);

        return readMap(bytes);
    }

    public static List<String> getMapList() {
        List<String> result = new ArrayList<>();
        File folder = new File(MAP_FOLDER);
        List<File> fileList = Arrays.asList(folder.listFiles());
        fileList.forEach(f -> result.add(f.getName()));

        return result;
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

        if ((bytes.length - 2) < (width * height))
            throw new IOException("Invalid map file.");

        for (int index = 2; index < bytes.length; index+=3) {
            map.setCellType(bytes[index], bytes[index+1], CellType.fromByte(bytes[index+2]));
        }


        return map;
    }

    private static byte[] convertToBytes(Map map) {
        List<Byte> result = new ArrayList<>();

        result.add(map.getWidth().byteValue());
        result.add(map.getHeight().byteValue());

        for (int x = 1; x <= map.getWidth(); x++) {
            for (int y = 1; y <= map.getHeight(); y++) {
                result.add((byte) x);
                result.add((byte) y);
                result.add(map.getCell(x, y).toByte());
            }
        }

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
