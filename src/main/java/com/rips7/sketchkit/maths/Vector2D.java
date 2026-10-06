package com.rips7.sketchkit.maths;

/**
 * An immutable two-dimensional vector or point.
 *
 * @param x the x component
 * @param y the y component
 */
public record Vector2D(double x, double y) {

    /**
     * Creates a vector from the given components.
     *
     * @param x the x component
     * @param y the y component
     * @return a new {@code Vector2D}
     */
    public static Vector2D of(double x, double y) {
        return new Vector2D(x, y);
    }

}