package com.rips7.sketchkit.canvas.renderer;

import com.rips7.sketchkit.canvas.config.EllipseMode;
import com.rips7.sketchkit.canvas.context.RenderContext;
import com.rips7.sketchkit.canvas.shape.Ellipse;
import com.rips7.sketchkit.canvas.shape.Line;
import com.rips7.sketchkit.canvas.shape.Path;
import com.rips7.sketchkit.canvas.shape.Rectangle;
import com.rips7.sketchkit.canvas.shape.Shape;
import com.rips7.sketchkit.canvas.shape.Triangle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Draws onto a {@link Graphics2D} surface using the styling stored in a
 * {@link RenderContext}.
 *
 * <p>The renderer owns its own context. Style methods ({@link #fill}, {@link #stroke},
 * {@link #ellipseMode}) update that context, and shape methods ({@link #line},
 * {@link #rectangle}, {@link #ellipse}) draw immediately using the current style.
 */
@SuppressWarnings("unused")
public final class Renderer {

    private final Graphics2D g2d;
    private final RenderContext context;
    private final Deque<AffineTransform> matrixStack = new ArrayDeque<>();

    /**
     * Creates a renderer that draws onto the given graphics object, with a fresh
     * {@link RenderContext} holding the default style.
     *
     * @param g2d the graphics surface to draw on
     */
    public Renderer(final Graphics2D g2d) {
        this.g2d = g2d;
        this.context = new RenderContext();
    }

    // Context

    /**
     * Sets the fill colour used for subsequent drawing.
     *
     * @param color the fill colour, or {@code null} to disable filling
     */
    public void fill(final Color color) {
        context.fill(color);
    }

    /**
     * Sets the stroke colour used for subsequent drawing.
     *
     * @param color the stroke colour, or {@code null} to disable outlines
     */
    public void stroke(final Color color) {
        context.stroke(color);
    }

    /**
     * Sets the stroke weight used for subsequent drawing.
     *
     * @param weight the stroke weight
     */
    public void strokeWeight(final double weight) {
        context.strokeWeight(weight);
    }

    /**
     * Sets how the position of subsequently drawn ellipses is interpreted.
     *
     * @param ellipseMode the {@link EllipseMode} to use
     */
    public void ellipseMode(final EllipseMode ellipseMode) {
        context.ellipseMode(ellipseMode);
    }

    // Background

    /**
     * Redraws the entire drawing surface using the given colour.
     *
     * <p>This does not modify the current fill or stroke style.
     *
     * @param color the colour to use for the background
     */
    public void background(final Color color) {
        final Color previousColor = g2d.getColor();
        final java.awt.Rectangle clip = g2d.getClipBounds();
        try {
            g2d.setColor(color);
            final int x = clip != null ? clip.x : 0;
            final int y = clip != null ? clip.y : 0;
            final int width = clip != null ? clip.width : g2d.getDeviceConfiguration().getBounds().width;
            final int height = clip != null ? clip.height : g2d.getDeviceConfiguration().getBounds().height;
            g2d.fillRect(x, y, width, height);
        } finally {
            g2d.setColor(previousColor);
        }
    }

    // Shapes

    /**
     * Draws a line between two points using the current stroke colour.
     *
     * @param x1 the x-coordinate of the start point
     * @param y1 the y-coordinate of the start point
     * @param x2 the x-coordinate of the end point
     * @param y2 the y-coordinate of the end point
     */
    public void line(final double x1, final double y1, final double x2, final double y2) {
        draw(new Line(x1, y1, x2, y2));
    }

    /**
     * Draws a rectangle using the current fill and stroke colours.
     *
     * @param x      the x-coordinate of the top-left corner
     * @param y      the y-coordinate of the top-left corner
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public void rectangle(final double x, final double y, final double width, final double height) {
        draw(new Rectangle(x, y, width, height));
    }

    /**
     * Draws an isosceles triangle pointing in the given direction.
     *
     * <p>The triangle is centred on {@code (x, y)} with its apex pointing towards {@code angle}.
     *
     * @param x      the x-coordinate of the centre of the triangle
     * @param y      the y-coordinate of the centre of the triangle
     * @param base   the width of the base
     * @param height the distance from base to apex
     * @param angle  the heading angle in radians
     */
    public void triangle(final double x, final double y, final double base, final double height, final double angle) {
        draw(new Triangle(x, y, base, height, angle));
    }

    /**
     * Draws an ellipse using the current fill colour, stroke colour and ellipse mode.
     *
     * @param x      the x-coordinate of the ellipse (centre or corner, depending on the mode)
     * @param y      the y-coordinate of the ellipse (centre or corner, depending on the mode)
     * @param width  the width of the ellipse
     * @param height the height of the ellipse
     */
    public void ellipse(final double x, final double y, final double width, final double height) {
        draw(new Ellipse(x, y, width, height));
    }

    /**
     * Starts defining a custom shape.
     *
     * <p>The returned builder is used to add vertices and complete the shape.
     *
     * @return a builder for defining the shape
     */
    public ShapeBuilder shape() {
        return new ShapeBuilder();
    }

    private void draw(final Shape shape) {
        if (context.fill() != null) {
            g2d.setColor(context.fill());
            shape.fill(g2d, context);
        }

        if (context.stroke() != null) {
            g2d.setColor(context.stroke());
            g2d.setStroke(new BasicStroke((float) context.strokeWeight()));
            shape.stroke(g2d, context);
        }
    }

    /**
     * Builds a custom shape from a sequence of vertices.
     */
    public final class ShapeBuilder {

        private final Path2D path = new Path2D.Double();

        private ShapeBuilder() { }

        /**
         * Adds a vertex to the shape.
         *
         * <p>The first vertex starts the shape; subsequent vertices are connected
         * to the previous vertex with a straight line.
         *
         * @param x the x-coordinate of the vertex
         * @param y the y-coordinate of the vertex
         * @return this builder
         */
        public ShapeBuilder vertex(final double x, final double y) {
            if (path.getCurrentPoint() == null) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            return this;
        }

        /**
         * Completes and draws the shape.
         *
         * <p>The shape is closed before being drawn, connecting the final vertex
         * back to the first vertex. The current fill and stroke styles are used.
         */
        public void endShape() {
            path.closePath();
            draw(new Path(path));
        }
    }

    // Transforms

    /**
     * Saves the current transformation matrix onto a stack so it can be restored later with
     * {@link #popMatrix()}.
     *
     * <p>Calls to {@code pushMatrix()} and {@code popMatrix()} must be balanced. A typical
     * usage is to isolate a transform to one object:
     * <pre>{@code
     * pushMatrix();
     * translate(position.x(), position.y());
     * rotate(velocity.heading());
     * triangle(0, 0, 10, 15, 0);
     * popMatrix();
     * }</pre>
     */
    public void pushMatrix() {
        matrixStack.push(g2d.getTransform());
    }

    /**
     * Restores the transformation matrix saved by the most recent {@link #pushMatrix()}.
     *
     * @throws IllegalStateException if there is no matching {@link #pushMatrix()}
     */
    public void popMatrix() {
        if (matrixStack.isEmpty()) {
            throw new IllegalStateException("popMatrix() called without a matching pushMatrix()");
        }
        g2d.setTransform(matrixStack.pop());
    }

    /**
     * Moves the origin of the coordinate system by the given amounts.
     *
     * <p>The translation accumulates with any existing transform. Use {@link #pushMatrix()} and
     * {@link #popMatrix()} to scope it to a single object.
     *
     * @param dx the distance to move along the x-axis
     * @param dy the distance to move along the y-axis
     */
    public void translate(final double dx, final double dy) {
        g2d.translate(dx, dy);
    }

    /**
     * Rotates the coordinate system about the current origin.
     *
     * <p>Positive angles rotate clockwise on screen, consistent with
     * {@link com.rips7.sketchkit.maths.Vector2D#heading()}.
     *
     * @param angle the rotation angle in radians
     */
    public void rotate(final double angle) {
        g2d.rotate(angle);
    }

    /**
     * Scales the coordinate system uniformly about the current origin.
     *
     * <p>Values greater than {@code 1} enlarge; values between {@code 0} and {@code 1} shrink.
     * Negative values also mirror the coordinate system.
     *
     * @param factor the scale factor
     */
    public void scale(final double factor) {
        g2d.scale(factor, factor);
    }

}