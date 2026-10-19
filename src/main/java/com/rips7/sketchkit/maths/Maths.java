package com.rips7.sketchkit.maths;

/**
 * Mathematical constants and helper functions for sketches.
 *
 * <p>This is a utility class and cannot be instantiated.
 */
public final class Maths {

    /**
     * Half a turn in radians, equal to {@link Math#PI}.
     */
    public static final double PI = Math.PI;

    /**
     * A quarter turn in radians, equal to {@code PI / 2}.
     */
    public static final double HALF_PI = 0.5 * PI;

    /**
     * A full turn in radians, equal to {@link Math#TAU} ({@code 2 * PI}).
     */
    public static final double TWO_PI = Math.TAU;

    private Maths() {
        // Do not instantiate
    }

    /**
     * Returns a random number between {@code lower} (inclusive) and {@code upper} (exclusive).
     *
     * @param lower the lower bound, inclusive
     * @param upper the upper bound, exclusive
     * @return a pseudo-random value in the range {@code [lower, upper)}
     */
    public static double random(final double lower, final double upper) {
        return Math.random() * (upper - lower) + lower;
    }

    /**
     * Returns a random number between 0 (inclusive) and {@code upper} (exclusive).
     *
     * @param upper the upper bound, exclusive
     * @return a pseudo-random value in the range {@code [0, upper)}
     */
    public static double random(final double upper) {
        return random(0, upper);
    }

    /**
     * Returns a random element from the given array.
     *
     * @param arr the array
     * @return a random element from the array
     * @param <T> the type of the elements in the array
     */
    public static <T> T random(final T[] arr) {
        final int randomIndex = (int) Math.floor(Math.random() * arr.length);
        return arr[randomIndex];
    }

    /**
     * Rescales a value from one range to another.
     *
     * @param value   the value to rescale
     * @param fromMin the lower bound of the source range
     * @param fromMax the upper bound of the source range
     * @param toMin   the lower bound of the target range
     * @param toMax   the upper bound of the target range
     * @return the rescaled value
     */
    public static double map(final double value, final double fromMin, final double fromMax, final double toMin,
            final double toMax) {
        return toMin + (value - fromMin) * (toMax - toMin) / (fromMax - fromMin);
    }

    /**
     * Linearly interpolates between two values.
     *
     * @param start the starting value
     * @param stop  the ending value
     * @param amount the interpolation amount, where {@code 0} returns {@code start}
     *               and {@code 1} returns {@code stop}
     * @return the interpolated value
     */
    public static double lerp(final double start, final double stop, final double amount) {
        return start + (stop - start) * amount;
    }

    /**
     * Constrains a value to lie between a minimum and maximum.
     *
     * @param value the value to constrain
     * @param min   the minimum allowed value
     * @param max   the maximum allowed value
     * @return {@code value} if it is within the range, otherwise the nearest bound
     */
    public static double constrain(final double value, final double min, final double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Normalises a value from a range to the range {@code [0, 1]}.
     *
     * @param value the value to normalise
     * @param start the lower bound of the source range
     * @param stop  the upper bound of the source range
     * @return the normalised value
     */
    public static double norm(final double value, final double start, final double stop) {
        return (value - start) / (stop - start);
    }

    /**
     * Calculates the distance between two points.
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the distance between the two points
     */
    public static double dist(final double x1, final double y1, final double x2, final double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    /**
     * Returns the square of a value.
     *
     * @param value the value to square
     * @return {@code value * value}
     */
    public static double sq(final double value) {
        return value * value;
    }

    /**
     * Calculates the magnitude of a two-dimensional vector.
     *
     * @param x the x-component
     * @param y the y-component
     * @return the magnitude of the vector
     */
    public static double mag(final double x, final double y) {
        return Math.hypot(x, y);
    }

    /**
     * Converts an angle from degrees to radians.
     *
     * @param degrees the angle in degrees
     * @return the angle in radians
     */
    public static double radians(final double degrees) {
        return Math.toRadians(degrees);
    }

    /**
     * Converts an angle from radians to degrees.
     *
     * @param radians the angle in radians
     * @return the angle in degrees
     */
    public static double degrees(final double radians) {
        return Math.toDegrees(radians);
    }
}