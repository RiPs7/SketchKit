package com.rips7.sketchkit.canvas.shape;

import com.rips7.sketchkit.canvas.context.ShapeContext;

import java.awt.Graphics2D;

/**
 * A drawable geometric shape.
 *
 * <p>Implementations hold their own geometry and provide operations for filling
 * and stroking that geometry. Styling such as fill colour, stroke colour and
 * stroke weight is applied by the renderer.
 */
public interface Shape {

    /**
     * Fills this shape.
     *
     * @param graphics the graphics surface to draw on
     * @param context  the context providing the current style and any
     *                 geometry-specific settings
     */
    void fill(final Graphics2D graphics, final ShapeContext context);

    /**
     * Strokes the outline of this shape.
     *
     * @param graphics the graphics surface to draw on
     * @param context  the context providing the current style and any
     *                 geometry-specific settings
     */
    void stroke(final Graphics2D graphics, final ShapeContext context);

}