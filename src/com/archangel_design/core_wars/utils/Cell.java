package com.archangel_design.core_wars.utils;

public class Cell {

    private Integer positionX;

    private Integer positionY;

    private CellType type;

    public Cell(int x, int y) {
        positionX = x;
        positionY = y;
        type = CellType.EMPTY;
    }

    public Cell(int x, int y, CellType cellType) {
        positionX = x;
        positionY = y;
        type = cellType;
    }

    public Integer getX() {
        return positionX;
    }

    public Integer getY() {
        return positionY;
    }

    public Integer getRealPositionX(Integer size) {
        return (positionX * size) - size;
    }

    public Integer getRealPositionY(Integer size) {
        return (positionY * size) - size;
    }
}
