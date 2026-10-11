package com.rips7.sketchkit.canvas.shape;

import com.rips7.sketchkit.canvas.context.RenderContext;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * An axis-aligned rectangle defined by its top-left corner, width and height.
 */
public final class Rectangle implements Shape {

    private final double x;
    private final double y;
    private final double width;
    private final double height;

    /**
     * Creates a rectangle.
     *
     * @param x      the x-coordinate of the top-left corner
     * @param y      the y-coordinate of the top-left corner
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public Rectangle(final double x, final double y, final double width, final double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void fill(final Graphics2D graphics, final RenderContext context) {
        final Rectangle2D.Double rectangle = new Rectangle2D.Double(x, y, width, height);
        graphics.fill(rectangle);
    }

    @Override
    public void stroke(final Graphics2D graphics, final RenderContext context) {
        final Rectangle2D.Double rectangle = new Rectangle2D.Double(x, y, width, height);
        graphics.draw(rectangle);
    }
}