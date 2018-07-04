package com.archangel_design.core_wars.utils;

import com.archangel_design.core_wars.utils.bugs.Direction;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;

public class MapRenderer {

    private static final Integer cellSize = 30;
    private static final Paint colorEmpty = Color.rgb(70, 70, 70, 0.7);
    private static final Paint colorBarrier = Color.rgb(20, 20, 20, 1);
    private static final Paint colorBomb = Color.rgb(150, 0, 10, 0.4);
    private static final Paint colorTeleport = Color.rgb(10, 0, 150, 0.4);
    private static final Paint colorTrap = Color.rgb(200, 150, 10, 0.4);

    public void drawMap(GraphicsContext context, Map map) {
        Iterator rowIterator = map.getRows().entrySet().iterator();

        while (rowIterator.hasNext()) {
            Map.Row.Entry rowEntry = (Map.Row.Entry) rowIterator.next();
            HashMap<Integer, Cell> cells = (HashMap<Integer, Cell>) rowEntry.getValue();
            Iterator columnIterator = cells.entrySet().iterator();
            while (columnIterator.hasNext()) {
                Map.Row.Entry colEntry = (Map.Row.Entry) columnIterator.next();
                Cell cell = (Cell) colEntry.getValue();
                drawCell(cell, context);
            }
        }
    }

    private void drawCell(Cell cell, GraphicsContext context) {
        context.setLineWidth(1);
        if (!cell.getType().equals(CellType.EMPTY)) {
            // if empty cells have background color
            // this call prevents white background
            // for other types. If we're drawing
            // empty however, we do not redraw
            // to avoid darkening since we're using RGBA
            //@todo: this should not be the case
            drawEmpty(
                    cell.getRealPositionX(cellSize),
                    cell.getRealPositionY(cellSize),
                    cellSize,
                    context
            );
        }
        switch (cell.getType()) {
            case EMPTY:
                drawEmpty(
                        cell.getRealPositionX(cellSize),
                        cell.getRealPositionY(cellSize),
                        cellSize,
                        context
                );
                break;
            case MINE:
                drawMine(
                        cell.getRealPositionX(cellSize),
                        cell.getRealPositionY(cellSize),
                        cellSize,
                        context
                );
                break;
            case TRAP:
                drawTrap(
                        cell.getRealPositionX(cellSize),
                        cell.getRealPositionY(cellSize),
                        cellSize,
                        context
                );
                break;
            case BARRIER:
                drawBarrier(
                        cell.getRealPositionX(cellSize),
                        cell.getRealPositionY(cellSize),
                        cellSize,
                        context
                );
                break;
            case PORTAL:
                drawTeleport(
                        cell.getRealPositionX(cellSize),
                        cell.getRealPositionY(cellSize),
                        cellSize,
                        context
                );
                break;
        }
    }

    private void drawRect(int x, int y, int size, GraphicsContext context) {
        context.fillRect(x, y, size, size);
        context.strokeRect(x, y, size, size);
    }

    private void drawEmpty(final int x, final int y, final int size, GraphicsContext context) {
        context.setStroke(Color.BLACK);
        context.setFill(colorEmpty);
        drawRect(x, y, size, context);
    }

    public void drawMine(final int x, final int y, final int size, GraphicsContext context) {
        Image image = Assets.getImage("mine.png");
        context.drawImage(image, x, y, size, size);
    }

    private void drawTrap(final int x, final int y, final int size, GraphicsContext context) {
        context.setStroke(Color.BLACK);
        context.setFill(colorTrap);
        drawRect(x, y, size, context);
    }

    private void drawBarrier(final int x, final int y, final int size, GraphicsContext context) {
        context.setStroke(Color.BLACK);
        context.setFill(colorBarrier);
        drawRect(x, y, size, context);
    }

    private void drawTeleport(final int x, final int y, final int size, GraphicsContext context) {
        context.setStroke(Color.BLACK);
        context.setFill(colorTeleport);
        drawRect(x, y, size, context);
    }

    public void drawBug(int x, int y, int number, int size, Direction direction, GraphicsContext context) {
        context.drawImage(Assets.getImage("bug1.png", direction), x, y, size, size);
    }
}
