package com.archangel_design.core_wars.utils;

import java.util.HashMap;

public class Map {

    private HashMap<Integer, Row> rows;
    private Integer witdth;
    private Integer height;

    class Row extends HashMap<Integer, Cell> {}

    public Map(Integer width, Integer height) {
        this.witdth = width;
        this.height = height;

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

    public void setCellType(int x, int y, CellType type) {
        rows.get(y).get(x).setType(type);
    }

    public Integer getWitdth() {
        return witdth;
    }

    public Integer getHeight() {
        return height;
    }
}
