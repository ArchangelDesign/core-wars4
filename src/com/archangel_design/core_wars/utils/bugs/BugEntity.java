package com.archangel_design.core_wars.utils.bugs;

import com.archangel_design.core_wars.utils.compiler.Compiler;
import com.archangel_design.core_wars.utils.compiler.Stack;
import com.archangel_design.core_wars.utils.compiler.exception.MethodRedeclaredException;

public class BugEntity {

    private String name;

    private String path;

    private int x;

    private int y;

    private Direction direction = Direction.UP;

    private Compiler compiler = new Compiler();

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

    public int getRealX(int size) {
        return (x - 1) * size;
    }

    public int getRealY(int size) {
        return (y - 1) * size;
    }

    public void addMethod(String name, Stack stack) {
        try {
            compiler.addMethod(name, stack);
        } catch (MethodRedeclaredException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getMethodCount() {
        return compiler.getMethods().size();
    }
}
