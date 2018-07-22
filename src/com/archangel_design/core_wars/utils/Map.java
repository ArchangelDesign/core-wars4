package com.archangel_design.core_wars.utils;

import javafx.geometry.Dimension2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map {

    private HashMap<Integer, Row> rows;
    private Integer width;
    private Integer height;
    private Integer cellSize = 30;
    private int portalCount = 0;
    private List<Shell> shells;

    public int getIndex(double realPosition) {
        return (int) Math.ceil(realPosition / cellSize);
    }

    public Integer getPosition(int realPosition) {
        return ((realPosition + 15) / cellSize) + 1;
    }

    class Row extends HashMap<Integer, Cell> {
    }

    public Map(Integer width, Integer height, Integer cellSize) {
        this.cellSize = cellSize;

        initializeMap(width, height);
    }

    public Map(Integer width, Integer height) {
        this.width = width;
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

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getCellSize() {
        return cellSize;
    }

    public Cell getCell(int x, int y) {
        if (!rows.containsKey(y))
            return null;
        if (!rows.get(y).containsKey(x))
            return null;
        return rows.get(y).get(x);
    }

    public int getPortalCount() {
        portalCount = 0;
        rows.forEach((y, row) -> row.forEach(
                (x, cell) -> {
                    if (cell.getType().equals(CellType.PORTAL)) portalCount++;
                }
        ));

        return portalCount;
    }

    public List<Dimension2D> getPortals() {
        List<Dimension2D> result = new ArrayList<>();
        rows.forEach((y, row) -> row.forEach(
                (x, cell) -> {
                    if (cell.getType().equals(CellType.PORTAL))
                        result.add(new Dimension2D(cell.getX(), cell.getY()));
                }
        ));

        return result;
    }
}
