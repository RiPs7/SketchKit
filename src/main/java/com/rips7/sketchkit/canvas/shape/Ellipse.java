package com.rips7.sketchkit.canvas.shape;

import com.rips7.sketchkit.canvas.context.ShapeContext;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * An ellipse defined by a position, a width and a height.
 *
 * <p>The meaning of the position depends on the context's
 * {@link com.rips7.sketchkit.canvas.config.EllipseMode}: it is the centre in
 * {@code CENTER} mode and the top-left corner of the bounding box in {@code CORNER} mode.
 */
public final class Ellipse implements Shape {

    private final double x;
    private final double y;
    private final double width;
    private final double height;

    /**
     * Creates an ellipse.
     *
     * @param x      the x-coordinate (centre or corner, depending on the ellipse mode)
     * @param y      the y-coordinate (centre or corner, depending on the ellipse mode)
     * @param width  the width of the ellipse
     * @param height the height of the ellipse
     */
    public Ellipse(final double x, final double y, final double width, final double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void fill(final Graphics2D graphics, final ShapeContext context) {
        graphics.fill(createEllipse(context));
    }

    @Override
    public void stroke(final Graphics2D graphics, final ShapeContext context) {
        graphics.draw(createEllipse(context));
    }

    private Ellipse2D.Double createEllipse(final ShapeContext context) {
        final double ellipseX;
        final double ellipseY;

        switch (context.ellipseMode()) {
            case CENTER -> {
                ellipseX = x - width / 2;
                ellipseY = y - height / 2;
            }
            case CORNER -> {
                ellipseX = x;
                ellipseY = y;
            }
            default -> throw new IllegalStateException("Unsupported ellipse mode: " + context.ellipseMode());
        }

        return new Ellipse2D.Double(ellipseX, ellipseY, width, height);
    }
}