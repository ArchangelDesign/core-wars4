package com.archangel_design.core_wars.utils;

import java.util.HashMap;

public class Map {

    private HashMap<Integer, Row> rows;

    class Row extends HashMap<Integer, Cell> {}

    public Map(Integer width, Integer height) {
        rows = new HashMap<>();

        for (int row = 1; row <= height; row++) {
            Row theRow = new Row();
            rows.put(row, theRow);
            for (int col = 1; col <= height; col++) {
                theRow.put(col, new Cell(col, row));
            }
        }
    }

    public HashMap<Integer, Row> getRows() {
        return rows;
    }

}
