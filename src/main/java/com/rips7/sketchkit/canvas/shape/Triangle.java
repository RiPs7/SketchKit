package com.rips7.sketchkit.canvas.shape;

import com.rips7.sketchkit.canvas.context.ShapeContext;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

/**
 * An isosceles triangle defined by a centre point, a base width, a height and a heading angle.
 *
 * <p>The triangle points in the direction of {@code angle}, with its apex at the front and its
 * base at the rear. An angle of {@code 0} points right, and positive angles rotate clockwise on
 * screen, consistent with {@link com.rips7.sketchkit.maths.Vector2D#heading()}.
 *
 * <p>This makes it straightforward to draw a boid facing its velocity vector:
 * <pre>{@code
 * s.triangle(position.x(), position.y(), 12, 20, velocity.heading());
 * }</pre>
 */
public final class Triangle implements Shape {

    private final double x;
    private final double y;
    private final double base;
    private final double height;
    private final double angle;

    /**
     * Creates an isosceles triangle.
     *
     * @param x      the x-coordinate of the centre of the triangle
     * @param y      the y-coordinate of the centre of the triangle
     * @param base   the width of the base
     * @param height the distance from base to apex
     * @param angle  the heading angle in radians; {@code 0} points right, positive values rotate
     *               clockwise on screen
     */
    public Triangle(final double x, final double y, final double base,
                    final double height, final double angle) {
        this.x = x;
        this.y = y;
        this.base = base;
        this.height = height;
        this.angle = angle;
    }

    @Override
    public void fill(final Graphics2D graphics, final ShapeContext context) {
        graphics.fill(createTriangle());
    }

    @Override
    public void stroke(final Graphics2D graphics, final ShapeContext context) {
        graphics.draw(createTriangle());
    }

    /**
     * Builds the triangle path in local space (pointing right along the x-axis) and then
     * rotates it to the requested heading.
     *
     * <p>In local space the three vertices are:
     * <ul>
     *   <li>Apex: {@code (height * 2/3, 0)} — two thirds of the height in front of centre.</li>
     *   <li>Base left:  {@code (-height / 3,  base / 2)}</li>
     *   <li>Base right: {@code (-height / 3, -base / 2)}</li>
     * </ul>
     * The centroid of these three points is the origin, so the visual centre of the triangle
     * sits at {@code (x, y)}.
     *
     * @return the path ready to fill or draw
     */
    private Path2D.Double createTriangle() {
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);

        // Local-space vertices (centroid at origin, pointing along +x).
        final double apexLx    =  height * 2.0 / 3.0;
        final double apexLy    =  0;
        final double baseLLx   = -height / 3.0;
        final double baseLLy   =  base / 2.0;
        final double baseLRx   = -height / 3.0;
        final double baseLRy   = -base / 2.0;

        final Path2D.Double path = new Path2D.Double();
        path.moveTo(rotX(apexLx,  apexLy,  cos, sin), rotY(apexLx,  apexLy,  cos, sin));
        path.lineTo(rotX(baseLLx, baseLLy, cos, sin), rotY(baseLLx, baseLLy, cos, sin));
        path.lineTo(rotX(baseLRx, baseLRy, cos, sin), rotY(baseLRx, baseLRy, cos, sin));
        path.closePath();

        return path;
    }

    /**
     * Returns the world-space x-coordinate of a local-space point after rotation and translation.
     *
     * @param lx  local x
     * @param ly  local y
     * @param cos cosine of the heading angle
     * @param sin sine of the heading angle
     * @return world x
     */
    private double rotX(final double lx, final double ly, final double cos, final double sin) {
        return x + lx * cos - ly * sin;
    }

    /**
     * Returns the world-space y-coordinate of a local-space point after rotation and translation.
     *
     * @param lx  local x
     * @param ly  local y
     * @param cos cosine of the heading angle
     * @param sin sine of the heading angle
     * @return world y
     */
    private double rotY(final double lx, final double ly, final double cos, final double sin) {
        return y + lx * sin + ly * cos;
    }
}
