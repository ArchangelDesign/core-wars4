package com.archangel_design.core_wars.utils;

import com.archangel_design.core_wars.utils.bugs.BugEntity;
import com.archangel_design.core_wars.utils.bugs.Direction;

public class Shell {
    private int x;
    private int y;
    private Direction direction;
    private BugEntity owner;

    public Shell(int x, int y, Direction direction, BugEntity owner) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.owner = owner;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public BugEntity getOwner() {
        return owner;
    }
}
