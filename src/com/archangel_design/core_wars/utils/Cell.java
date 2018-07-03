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

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public Byte toByte() {
        switch (type) {
            case EMPTY: return 0;
            case BARRIER: return 1;
            case MINE: return 2;
            case PORTAL: return 3;
            case TRAP: return 4;
        }
        throw new RuntimeException("Missing cell type.");
    }
}
