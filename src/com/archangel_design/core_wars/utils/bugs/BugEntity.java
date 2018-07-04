package com.archangel_design.core_wars.utils.bugs;

public class BugEntity {

    private String name;

    private String path;

    private int x;

    private int y;

    private Direction direction;

    public String getName() {
        return name;
    }

    public BugEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public BugEntity setPath(String path) {
        this.path = path;
        return this;
    }

    public int getX() {
        return x;
    }

    public BugEntity setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public BugEntity setY(int y) {
        this.y = y;
        return this;
    }

    public Direction getDirection() {
        return direction;
    }

    public BugEntity setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }
}
