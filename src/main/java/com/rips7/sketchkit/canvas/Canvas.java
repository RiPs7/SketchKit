package com.rips7.sketchkit.canvas;

import com.rips7.sketchkit.canvas.config.EllipseMode;
import com.rips7.sketchkit.canvas.renderer.ShapeRenderer;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A Swing panel offering a simple, Processing-style drawing API.
 *
 * <p>Drawing is only valid between a call to {@link #begin(Graphics)} and a call to
 * {@link #end()}. Calling any style or shape method outside that window throws an
 * {@link IllegalStateException}. Subclasses such as {@link com.rips7.sketchkit.render.Sketch}
 * handle this lifecycle automatically on each repaint.
 *
 * <p>Note that this class is unrelated to {@link java.awt.Canvas}.
 */
@SuppressWarnings("unused")
public class Canvas extends JPanel {

    private ShapeRenderer shapeRenderer;

    /**
     * Starts a drawing pass on the given graphics object.
     *
     * <p>This creates a new {@link ShapeRenderer}, so style settings from a previous pass are
     * reset to their defaults.
     *
     * @param graphics the graphics object to draw on; must be a {@link Graphics2D}
     * @throws ClassCastException if {@code graphics} is not a {@link Graphics2D}
     */
    public void begin(final Graphics graphics) {
        shapeRenderer = new ShapeRenderer((Graphics2D) graphics);
    }

    /**
     * Ends the current drawing pass and discards the renderer.
     */
    public void end() {
        shapeRenderer = null;
    }

    /**
     * Fills the entire canvas with the given background colour.
     *
     * <p>This does not modify the current fill or stroke colour.
     *
     * @param color the background colour
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void background(final Color color) {
        ensureInitialised();
        shapeRenderer.background(color);
    }

    /**
     * Sets the fill colour for subsequently drawn shapes.
     *
     * @param color the fill colour
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void fill(final Color color) {
        ensureInitialised();
        shapeRenderer.fill(color);
    }

    /**
     * Disables filling for subsequently drawn shapes.
     *
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void noFill() {
        ensureInitialised();
        shapeRenderer.fill(null);
    }

    /**
     * Sets the stroke colour for subsequently drawn shapes and lines.
     *
     * @param color the stroke colour
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void stroke(final Color color) {
        ensureInitialised();
        shapeRenderer.stroke(color);
    }

    /**
     * Disables outlines for subsequently drawn shapes. Lines are not drawn while the stroke is
     * disabled.
     *
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void noStroke() {
        ensureInitialised();
        shapeRenderer.stroke(null);
    }

    /**
     * Sets the stroke weight for subsequently drawn shapes and lines.
     *
     * @param weight the stroke weight
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void strokeWeight(final double weight) {
        ensureInitialised();
        shapeRenderer.strokeWeight(weight);
    }

    /**
     * Sets how the position of subsequently drawn ellipses and circles is interpreted.
     *
     * @param mode the {@link EllipseMode} to use
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void ellipseMode(final EllipseMode mode) {
        ensureInitialised();
        shapeRenderer.ellipseMode(mode);
    }

    /**
     * Draws a line between two points.
     *
     * @param x1 the x-coordinate of the start point
     * @param y1 the y-coordinate of the start point
     * @param x2 the x-coordinate of the end point
     * @param y2 the y-coordinate of the end point
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void line(final double x1, final double y1, final double x2, final double y2) {
        ensureInitialised();
        shapeRenderer.line(x1, y1, x2, y2);
    }

    /**
     * Draws a rectangle.
     *
     * @param x      the x-coordinate of the top-left corner
     * @param y      the y-coordinate of the top-left corner
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void rect(final double x, final double y, final double width, final double height) {
        ensureInitialised();
        shapeRenderer.rectangle(x, y, width, height);
    }

    /**
     * Draws an ellipse.
     *
     * @param x      the x-coordinate (centre or corner, depending on the ellipse mode)
     * @param y      the y-coordinate (centre or corner, depending on the ellipse mode)
     * @param width  the width of the ellipse
     * @param height the height of the ellipse
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void ellipse(final double x, final double y, final double width, final double height) {
        ensureInitialised();
        shapeRenderer.ellipse(x, y, width, height);
    }

    /**
     * Draws a circle. This is an ellipse whose width and height are both {@code diameter}.
     *
     * @param x        the x-coordinate (centre or corner, depending on the ellipse mode)
     * @param y        the y-coordinate (centre or corner, depending on the ellipse mode)
     * @param diameter the diameter of the circle
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void circle(final double x, final double y, final double diameter) {
        ensureInitialised();
        ellipse(x, y, diameter, diameter);
    }

    /**
     * Converts a colour from HSB (hue, saturation, brightness) to an AWT {@link Color}.
     *
     * <p>This is equivalent to {@link java.awt.Color#getHSBColor(float, float, float)} but returns
     * a {@link Color} with full opacity and keeps the conversion explicit and readable.
     *
     * <p>The six branches of the conversion correspond to the six 60-degree sectors of the colour
     * wheel:
     * <ol>
     *   <li>Red → Yellow</li>
     *   <li>Yellow → Green</li>
     *   <li>Green → Cyan</li>
     *   <li>Cyan → Blue</li>
     *   <li>Blue → Magenta</li>
     *   <li>Magenta → Red</li>
     * </ol>
     *
     * @param hue        the hue angle, in {@code [0, 1)}, where {@code 0} and {@code 1} both
     *                   represent red. Values outside this range are wrapped.
     * @param saturation the saturation, in {@code [0, 1]}: {@code 0} is grey, {@code 1} is fully
     *                   saturated
     * @param brightness the brightness, in {@code [0, 1]}: {@code 0} is black, {@code 1} is fully
     *                   bright
     * @return the equivalent fully opaque {@link Color}
     */
    public Color hsbToRgb(final float hue, final float saturation, final float brightness) {
        final float h = (hue % 1.0f + 1.0f) % 1.0f * 6.0f; // wrap to [0, 6)
        final float c = brightness * saturation;           // chroma
        final float x = c * (1 - Math.abs(h % 2 - 1));     // secondary component
        final float m = brightness - c;                    // brightness adjustment

        final float r, g, b;
        if      (h < 1) { r = c; g = x; b = 0; }
        else if (h < 2) { r = x; g = c; b = 0; }
        else if (h < 3) { r = 0; g = c; b = x; }
        else if (h < 4) { r = 0; g = x; b = c; }
        else if (h < 5) { r = x; g = 0; b = c; }
        else            { r = c; g = 0; b = x; }

        return new Color(r + m, g + m, b + m);
    }

    /**
     * Draws an isosceles triangle pointing in the given direction.
     *
     * <p>The triangle is centred on {@code (x, y)} with its apex pointing towards {@code angle}.
     * An angle of {@code 0} points right; positive angles rotate clockwise on screen, consistent
     * with {@link com.rips7.sketchkit.maths.Vector2D#heading()}. This makes it straightforward
     * to draw a boid facing its velocity vector:
     * <pre>{@code
     * triangle(position.x(), position.y(), 12, 20, velocity.heading());
     * }</pre>
     *
     * @param x      the x-coordinate of the centre of the triangle
     * @param y      the y-coordinate of the centre of the triangle
     * @param base   the width of the base
     * @param height the distance from base to apex
     * @param angle  the heading angle in radians
     * @throws IllegalStateException if called outside a drawing pass
     */
    public void triangle(final double x, final double y, final double base, final double height, final double angle) {
        ensureInitialised();
        shapeRenderer.triangle(x, y, base, height, angle);
    }

    /**
     * Begins defining a custom shape.
     *
     * <p>The returned builder is used to add vertices to the shape and complete
     * it with {@code endShape()}.
     *
     * @return a builder for defining the custom shape
     * @throws IllegalStateException if called outside a drawing pass
     */
    public ShapeRenderer.ShapeBuilder shape() {
        ensureInitialised();
        return shapeRenderer.shape();
    }

    /**
     * Returns the current width of this canvas.
     *
     * @return the width in pixels
     */
    public double width() {
        return getWidth();
    }

    /**
     * Returns the current height of this canvas.
     *
     * @return the height in pixels
     */
    public double height() {
        return getHeight();
    }

    /**
     * Verifies that a drawing pass is in progress.
     *
     * @throws IllegalStateException if {@link #begin(Graphics)} has not been called
     */
    private void ensureInitialised() {
        if (shapeRenderer == null) {
            throw new IllegalStateException("Shape Renderer has not been initialised");
        }
    }
}