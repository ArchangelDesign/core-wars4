package com.archangel_design.core_wars.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.print.attribute.IntegerSyntax;
import java.util.HashMap;
import java.util.Iterator;

public class MapDrawer {

    public void drawMap(GraphicsContext context, Map map) {
        Iterator rowOperator = map.getRows().entrySet().iterator();

        while (rowOperator.hasNext()) {
            Map.Row.Entry rowEntry = (Map.Row.Entry) rowOperator.next();
            Integer positionY = (Integer) rowEntry.getKey();
            HashMap<Integer, Cell> cells = (HashMap<Integer, Cell>) rowEntry.getValue();
            Iterator columnIterator = cells.entrySet().iterator();
            while (columnIterator.hasNext()) {
                Map.Row.Entry colEntry = (Map.Row.Entry) columnIterator.next();
                Integer positionX = (Integer) colEntry.getKey();
                Cell cell = (Cell) colEntry.getValue();
                drawCell(cell, context);
            }
        }
    }

    private void drawCell(Cell cell, GraphicsContext context) {
        context.setStroke(Color.BLACK);
        context.setFill(Color.GREEN);
        context.setLineWidth(1);
        Integer x = cell.getRealPositionX(30);
        Integer y = cell.getRealPositionY(30);
        context.fillRect(cell.getRealPositionX(30), cell.getRealPositionY(30), 30, 30);
        context.strokeRect(cell.getRealPositionX(30), cell.getRealPositionY(30), 30, 30);
    }
}
