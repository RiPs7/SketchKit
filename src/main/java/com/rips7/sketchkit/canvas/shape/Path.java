package com.rips7.sketchkit.canvas.shape;

import com.rips7.sketchkit.canvas.context.RenderContext;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

/**
 * A custom path consisting of connected line segments.
 *
 * <p>The path geometry is defined by the supplied {@link Path2D}. Styling such
 * as fill colour, stroke colour and stroke weight is applied by the renderer.
 */
public final class Path implements Shape {

    private final Path2D path;

    /**
     * Creates a path from the given Java2D path.
     *
     * @param path the geometry of the path
     */
    public Path(final Path2D path) {
        this.path = path;
    }

    /**
     * Fills this path.
     *
     * @param graphics the graphics surface
     * @param context  the current shape context
     */
    @Override
    public void fill(final Graphics2D graphics, final RenderContext context) {
        graphics.fill(path);
    }

    /**
     * Strokes this path.
     *
     * @param graphics the graphics surface
     * @param context  the current shape context
     */
    @Override
    public void stroke(final Graphics2D graphics, final RenderContext context) {
        graphics.draw(path);
    }
}