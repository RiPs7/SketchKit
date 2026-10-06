package com.rips7.sketchkit.canvas.shape;

import com.rips7.sketchkit.canvas.context.ShapeContext;

import java.awt.Graphics2D;

/**
 * A line segment between two points.
 *
 * <p>Lines do not have a fillable area and are therefore only affected by
 * the current stroke settings.
 */
public final class Line implements Shape {

    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;

    /**
     * Creates a line.
     *
     * @param x1 the x-coordinate of the start point
     * @param y1 the y-coordinate of the start point
     * @param x2 the x-coordinate of the end point
     * @param y2 the y-coordinate of the end point
     */
    public Line(final double x1, final double y1, final double x2, final double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Does nothing because a line has no fillable area.
     *
     * @param graphics the graphics surface to draw on
     * @param context  the current shape context
     */
    @Override
    public void fill(final Graphics2D graphics, final ShapeContext context) {
        // Lines cannot be filled.
    }

    /**
     * Strokes the line using the current graphics settings.
     *
     * <p>Coordinates are rounded to the nearest integer pixel.
     *
     * @param graphics the graphics surface to draw on
     * @param context  the current shape context
     */
    @Override
    public void stroke(final Graphics2D graphics, final ShapeContext context) {
        graphics.drawLine(
                (int) Math.round(x1),
                (int) Math.round(y1),
                (int) Math.round(x2),
                (int) Math.round(y2));
    }
}