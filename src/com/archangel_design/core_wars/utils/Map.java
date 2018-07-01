package com.archangel_design.core_wars.utils;

import java.util.HashMap;

public class Map {

    private HashMap<Integer, Row> rows;
    private Integer witdth;
    private Integer height;
    private Integer cellSize = 30;

    public int getIndex(double realPosition) {
        return (int) Math.ceil(realPosition / cellSize);
    }

    class Row extends HashMap<Integer, Cell> {}

    public Map(Integer width, Integer height, Integer cellSize) {
        this.witdth = width;
        this.height = height;
        this.cellSize = cellSize;

        initializeMap(width, height);
    }

    public Map(Integer width, Integer height) {
        this.witdth = width;
        this.height = height;

        initializeMap(width, height);
    }

    private void initializeMap(Integer width, Integer height) {
        rows = new HashMap<>();

        for (int row = 1; row <= height; row++) {
            Row theRow = new Row();
            rows.put(row, theRow);
            for (int col = 1; col <= width; col++) {
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

    public Integer getCellSize() {
        return cellSize;
    }
}
