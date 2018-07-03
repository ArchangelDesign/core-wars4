package com.archangel_design.core_wars.utils;

public enum CellType {
    EMPTY,
    BARRIER,
    TELEPORT,
    MINE,
    TRAP;

    public static CellType fromByte(byte input) {
        switch (input) {
            case 0: return CellType.EMPTY;
            case 1: return CellType.BARRIER;
            case 2: return CellType.MINE;
            case 3: return CellType.TELEPORT;
            case 4: return CellType.TRAP;
        }

        throw new RuntimeException("Invalid byte given. Cannot convert to cell type.");
    }
}
