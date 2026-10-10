package com.rips7.sketchkit.maths;

import java.util.List;
import java.util.Objects;

/**
 * An immutable two-dimensional vector or point.
 *
 * <p>Every arithmetic operation returns a new {@code Vector2D} and leaves the original unchanged.
 * The idiomatic update pattern is:
 * <pre>{@code
 * position = position.add(velocity.mult(deltaTime));
 * }</pre>
 *
 * <p>Static methods are factories or operations that combine two equal-standing vectors (such as
 * {@link #min} and {@link #max}). Instance methods operate on {@code this} and treat any vector
 * argument as secondary.
 *
 * @param x the x component
 * @param y the y component
 */
@SuppressWarnings("unused")
public record Vector2D(double x, double y) {

    // ------------------------------------------------------------------ constants

    /** The zero vector {@code (0, 0)}. */
    public static final Vector2D ZERO = new Vector2D(0, 0);

    /** The unit vector along the positive x-axis {@code (1, 0)}. */
    public static final Vector2D UNIT_X = new Vector2D(1, 0);

    /** The unit vector along the positive y-axis {@code (0, 1)}. */
    public static final Vector2D UNIT_Y = new Vector2D(0, 1);

    // ------------------------------------------------------------------ factories

    /**
     * Creates a vector from the given components.
     *
     * @param x the x component
     * @param y the y component
     * @return a new {@code Vector2D}
     */
    public static Vector2D of(final double x, final double y) {
        return new Vector2D(x, y);
    }

    /**
     * Creates a unit vector pointing in the given direction.
     *
     * @param angle the angle in radians, measured clockwise from the positive x-axis
     * @return a vector of length {@code 1}
     */
    public static Vector2D fromAngle(final double angle) {
        return new Vector2D(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Creates a unit vector pointing in a uniformly random direction.
     *
     * @return a vector of length {@code 1}
     */
    public static Vector2D random2D() {
        return fromAngle(Math.random() * Math.TAU);
    }

    // ------------------------------------------------------------------ static combiners

    /**
     * Returns the component-wise minimum of two vectors.
     *
     * <p>Useful for computing the top-left corner of a bounding box:
     * <pre>{@code
     * Vector2D topLeft = Vector2D.min(a, b);
     * }</pre>
     *
     * @param a the first vector
     * @param b the second vector
     * @return {@code (min(a.x, b.x), min(a.y, b.y))}
     */
    public static Vector2D min(final Vector2D a, final Vector2D b) {
        return new Vector2D(Math.min(a.x, b.x), Math.min(a.y, b.y));
    }

    /**
     * Returns the component-wise maximum of two vectors.
     *
     * <p>Useful for computing the bottom-right corner of a bounding box:
     * <pre>{@code
     * Vector2D bottomRight = Vector2D.max(a, b);
     * }</pre>
     *
     * @param a the first vector
     * @param b the second vector
     * @return {@code (max(a.x, b.x), max(a.y, b.y))}
     */
    public static Vector2D max(final Vector2D a, final Vector2D b) {
        return new Vector2D(Math.max(a.x, b.x), Math.max(a.y, b.y));
    }

    /**
     * Returns the midpoint of two points.
     *
     * @param a the first point
     * @param b the second point
     * @return the point halfway between {@code a} and {@code b}
     */
    public static Vector2D midpoint(final Vector2D a, final Vector2D b) {
        return new Vector2D((a.x + b.x) / 2, (a.y + b.y) / 2);
    }

    /**
     * Returns the angle in radians from {@code a} to {@code b}, measured clockwise from the
     * positive x-axis.
     *
     * <p>This is equivalent to {@code b.sub(a).heading()}, but reads more naturally at call
     * sites that have two points:
     * <pre>{@code
     * double angle = Vector2D.angleTo(mouse, target);
     * }</pre>
     *
     * @param a the origin point
     * @param b the target point
     * @return the angle in radians, in {@code (-PI, PI]}
     */
    public static double angleTo(final Vector2D a, final Vector2D b) {
        return b.sub(a).heading();
    }

    /**
     * Returns the distance between two points.
     *
     * <p>When only two points are available this reads more naturally than
     * {@code a.dist(b)}:
     * <pre>{@code
     * double d = Vector2D.distance(mouse, target);
     * }</pre>
     *
     * @param a the first point
     * @param b the second point
     * @return the Euclidean distance
     */
    public static double distance(final Vector2D a, final Vector2D b) {
        return a.dist(b);
    }

    /**
     * Returns the distance squared between two points.
     *
     * <p>When only two points are available this reads more naturally than
     * {@code a.distSq(b)}:
     * <pre>{@code
     * double d = Vector2D.distanceSq(mouse, target);
     * }</pre>
     *
     * @param a the first point
     * @param b the second point
     * @return the Euclidean distance squared
     */
    public static double distanceSq(final Vector2D a, final Vector2D b) {
        return a.distSq(b);
    }

    /**
     * Returns the arithmetic mean of a list of vectors, computed component-wise.
     *
     * <p>Useful for the flocking behaviours that need an average over a neighbourhood:
     * <pre>{@code
     * Vector2D avgVelocity = Vector2D.average(neighbours.stream()
     *         .map(b -> b.velocity)
     *         .toList());
     * }</pre>
     *
     * @param vectors the vectors to average; must not be {@code null}
     * @return the component-wise mean, or {@link #ZERO} if {@code vectors} is empty
     */
    public static Vector2D average(final List<Vector2D> vectors) {
        if (vectors.isEmpty()) return ZERO;
        return vectors.stream()
                .reduce(ZERO, Vector2D::add)
                .div(vectors.size());
    }

    // ------------------------------------------------------------------ arithmetic

    /**
     * Adds another vector to this one.
     *
     * @param other the vector to add
     * @return the sum {@code this + other}
     */
    public Vector2D add(final Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    /**
     * Adds scalar offsets to each component.
     *
     * @param dx the amount to add to the x component
     * @param dy the amount to add to the y component
     * @return the translated vector
     */
    public Vector2D add(final double dx, final double dy) {
        return new Vector2D(x + dx, y + dy);
    }

    /**
     * Subtracts another vector from this one.
     *
     * @param other the vector to subtract
     * @return the difference {@code this - other}
     */
    public Vector2D sub(final Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    /**
     * Multiplies both components by a scalar.
     *
     * @param factor the scalar
     * @return {@code this * factor}
     */
    public Vector2D mult(final double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    /**
     * Multiplies each component by the corresponding component of another vector
     * (Hadamard product).
     *
     * <p>This is not the dot product. It is useful for applying non-uniform scales.
     *
     * @param other the vector to multiply component-wise
     * @return {@code (this.x * other.x, this.y * other.y)}
     */
    public Vector2D mult(final Vector2D other) {
        return new Vector2D(x * other.x, y * other.y);
    }

    /**
     * Divides both components by a scalar.
     *
     * @param divisor the scalar; must not be zero
     * @return {@code this / divisor}
     */
    public Vector2D div(final double divisor) {
        return new Vector2D(x / divisor, y / divisor);
    }

    /**
     * Returns the opposite of this vector.
     *
     * @return {@code (-x, -y)}
     */
    public Vector2D negate() {
        return new Vector2D(-x, -y);
    }

    /**
     * Returns the absolute value of each component.
     *
     * @return {@code (|x|, |y|)}
     */
    public Vector2D abs() {
        return new Vector2D(Math.abs(x), Math.abs(y));
    }

    /**
     * Floors each component to the largest integer value not greater than it.
     *
     * @return the floored vector
     */
    public Vector2D floor() {
        return new Vector2D(Math.floor(x), Math.floor(y));
    }

    /**
     * Ceils each component to the smallest integer value not less than it.
     *
     * @return the ceiled vector
     */
    public Vector2D ceil() {
        return new Vector2D(Math.ceil(x), Math.ceil(y));
    }

    /**
     * Rounds each component to the nearest integer.
     *
     * @return the rounded vector
     */
    public Vector2D round() {
        return new Vector2D(Math.round(x), Math.round(y));
    }

    /**
     * Clamps each component to the given range.
     *
     * @param min the minimum value for each component
     * @param max the maximum value for each component
     * @return the clamped vector
     */
    public Vector2D clamp(final double min, final double max) {
        return new Vector2D(
                Math.min(Math.max(x, min), max),
                Math.min(Math.max(y, min), max));
    }

    /**
     * Clamps each component to the corresponding component of the given bounds.
     *
     * @param min the component-wise lower bound
     * @param max the component-wise upper bound
     * @return the clamped vector
     */
    public Vector2D clamp(final Vector2D min, final Vector2D max) {
        return new Vector2D(
                Math.min(Math.max(x, min.x), max.x),
                Math.min(Math.max(y, min.y), max.y));
    }

    // ------------------------------------------------------------------ magnitude and direction

    /**
     * Returns the length (magnitude) of this vector.
     *
     * @return {@code sqrt(x² + y²)}
     */
    public double mag() {
        return Math.hypot(x, y);
    }

    /**
     * Returns the squared length of this vector.
     *
     * <p>Cheaper than {@link #mag()} because it avoids a square root. Prefer it when comparing
     * lengths:
     * <pre>{@code
     * if (v.magSq() < radius * radius) { ... }
     * }</pre>
     *
     * @return {@code x² + y²}
     */
    public double magSq() {
        return x * x + y * y;
    }

    /**
     * Returns this vector scaled to length {@code 1}, keeping its direction.
     *
     * @return the unit vector, or {@link #ZERO} if this vector has zero length
     */
    public Vector2D normalize() {
        final double m = mag();
        return m == 0 ? ZERO : div(m);
    }

    /**
     * Returns this vector with its length set to the given value, keeping its direction.
     *
     * @param length the desired length
     * @return the rescaled vector, or {@link #ZERO} if this vector has zero length
     */
    public Vector2D setMag(final double length) {
        return normalize().mult(length);
    }

    /**
     * Returns this vector with its length capped at {@code max}, keeping its direction.
     *
     * @param max the maximum length; must not be negative
     * @return this vector if its length does not exceed {@code max}, otherwise a vector of
     *         length {@code max} in the same direction
     */
    public Vector2D limit(final double max) {
        return magSq() > max * max ? setMag(max) : this;
    }

    /**
     * Returns the direction of this vector, measured clockwise from the positive x-axis.
     *
     * @return the angle in radians, in {@code (-PI, PI]}
     */
    public double heading() {
        return Math.atan2(y, x);
    }

    /**
     * Returns a copy of this vector rotated by the given angle.
     *
     * @param angle the rotation angle in radians; positive values rotate clockwise on screen
     * @return the rotated vector
     */
    public Vector2D rotate(final double angle) {
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        return new Vector2D(x * cos - y * sin, x * sin + y * cos);
    }

    /**
     * Returns a copy of this vector rotated 90 degrees clockwise.
     *
     * <p>Equivalent to {@code rotate(HALF_PI)} but faster.
     *
     * @return the rotated vector {@code (y, -x)}
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public Vector2D perpendicular() {
        return new Vector2D(y, -x);
    }

    /**
     * Returns a copy of this vector with the x component flipped.
     *
     * @return {@code (-x, y)}
     */
    public Vector2D flipX() {
        return new Vector2D(-x, y);
    }

    /**
     * Returns a copy of this vector with the y component flipped.
     *
     * @return {@code (x, -y)}
     */
    public Vector2D flipY() {
        return new Vector2D(x, -y);
    }

    // ------------------------------------------------------------------ products and projections

    /**
     * Returns the dot product of this vector and another.
     *
     * <p>The sign tells you the general relationship between the directions:
     * positive means they point in broadly the same direction, zero means they are perpendicular,
     * and negative means they point in broadly opposite directions.
     *
     * @param other the other vector
     * @return {@code this.x * other.x + this.y * other.y}
     */
    public double dot(final Vector2D other) {
        return x * other.x + y * other.y;
    }

    /**
     * Returns the z-component of the three-dimensional cross product of this vector and another.
     *
     * <p>In 2D this is a scalar: positive if {@code other} is clockwise from {@code this},
     * negative if counter-clockwise, and zero if they are parallel. Its absolute value equals
     * the area of the parallelogram spanned by the two vectors.
     *
     * @param other the other vector
     * @return {@code this.x * other.y - this.y * other.x}
     */
    public double cross(final Vector2D other) {
        return x * other.y - y * other.x;
    }

    /**
     * Returns the scalar projection of this vector onto {@code axis}.
     *
     * <p>This is the signed length of the shadow that {@code this} casts onto {@code axis}:
     * positive if they point in the same general direction, negative otherwise.
     *
     * @param axis the axis to project onto; need not be normalised
     * @return the signed projected length, or {@code 0} if {@code axis} has zero length
     */
    public double scalarProject(final Vector2D axis) {
        final double m = axis.mag();
        return m == 0 ? 0 : dot(axis) / m;
    }

    /**
     * Returns the vector projection of this vector onto {@code axis}.
     *
     * <p>The result is the component of {@code this} that lies along {@code axis}.
     *
     * @param axis the axis to project onto; need not be normalised
     * @return the projected vector, or {@link #ZERO} if {@code axis} has zero length
     */
    public Vector2D project(final Vector2D axis) {
        final double mSq = axis.magSq();
        return mSq == 0 ? ZERO : axis.mult(dot(axis) / mSq);
    }

    /**
     * Returns the component of this vector perpendicular to {@code axis} (vector rejection).
     *
     * <p>{@code this.project(axis).add(this.reject(axis))} equals {@code this}.
     *
     * @param axis the axis to reject from; need not be normalised
     * @return the perpendicular component, or {@code this} if {@code axis} has zero length
     */
    public Vector2D reject(final Vector2D axis) {
        return sub(project(axis));
    }

    /**
     * Returns this vector reflected about a surface whose normal is {@code normal}.
     *
     * <p>The normal need not be normalised, but zero-length normals return {@code this}.
     *
     * @param normal the surface normal
     * @return the reflected vector
     */
    public Vector2D reflect(final Vector2D normal) {
        // r = this - 2 * (this · n̂) * n̂
        final double mSq = normal.magSq();
        if (mSq == 0) return this;
        return sub(normal.mult(2 * dot(normal) / mSq));
    }

    // ------------------------------------------------------------------ distance and interpolation

    /**
     * Returns the Euclidean distance from this point to another.
     *
     * @param other the other point
     * @return the distance
     */
    public double dist(final Vector2D other) {
        return Math.hypot(other.x - x, other.y - y);
    }

    /**
     * Returns the squared Euclidean distance from this point to another.
     *
     * <p>Cheaper than {@link #dist} when only relative distances matter.
     *
     * @param other the other point
     * @return the squared distance
     */
    public double distSq(final Vector2D other) {
        final double dx = other.x - x;
        final double dy = other.y - y;
        return dx * dx + dy * dy;
    }

    /**
     * Returns the Manhattan (taxicab) distance from this point to another.
     *
     * @param other the other point
     * @return {@code |other.x - x| + |other.y - y|}
     */
    public double distManhattan(final Vector2D other) {
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }

    /**
     * Returns the Chebyshev distance from this point to another, equal to the larger of the
     * absolute differences of the components.
     *
     * <p>This is the distance a king moves in chess.
     *
     * @param other the other point
     * @return {@code max(|other.x - x|, |other.y - y|)}
     */
    public double distChebyshev(final Vector2D other) {
        return Math.max(Math.abs(other.x - x), Math.abs(other.y - y));
    }

    /**
     * Returns the angle between this vector and another.
     *
     * @param other the other vector
     * @return the angle in radians, in {@code [0, PI]}, or {@code 0} if either vector has zero
     *         length
     */
    public double angleBetween(final Vector2D other) {
        final double lengths = mag() * other.mag();
        if (lengths == 0) return 0;
        return Math.acos(Math.max(-1.0, Math.min(1.0, dot(other) / lengths)));
    }

    /**
     * Linearly interpolates from this vector towards {@code target}.
     *
     * @param target the vector to interpolate towards
     * @param amount the interpolation amount; {@code 0} returns {@code this}, {@code 1} returns
     *               {@code target}, values outside {@code [0, 1]} extrapolate
     * @return the interpolated vector
     */
    public Vector2D lerp(final Vector2D target, final double amount) {
        return new Vector2D(
                x + (target.x - x) * amount,
                y + (target.y - y) * amount);
    }

    /**
     * Returns a copy of this vector moved towards {@code target} by at most {@code step} units.
     *
     * <p>If the distance is less than or equal to {@code step}, {@code target} is returned.
     *
     * @param target the destination point
     * @param step   the maximum distance to move; must not be negative
     * @return the moved vector
     */
    public Vector2D moveTowards(final Vector2D target, final double step) {
        final double d = dist(target);
        return d <= step ? target : lerp(target, step / d);
    }

    // ------------------------------------------------------------------ equality

    /**
     * Returns whether this vector is equal to another within a given tolerance.
     *
     * <p>Prefer this over {@link #equals} when the vectors were computed with floating-point
     * arithmetic.
     *
     * @param other     the vector to compare to
     * @param tolerance the maximum permitted difference for each component; must not be negative
     * @return {@code true} if both components differ by at most {@code tolerance}
     */
    public boolean epsilonEquals(final Vector2D other, final double tolerance) {
        Objects.requireNonNull(other, "other");
        return Math.abs(x - other.x) <= tolerance && Math.abs(y - other.y) <= tolerance;
    }

    /**
     * Returns whether this vector is the zero vector within a given tolerance.
     *
     * @param tolerance the maximum permitted magnitude; must not be negative
     * @return {@code true} if {@link #magSq()} is at most {@code tolerance * tolerance}
     */
    public boolean isZero(final double tolerance) {
        return magSq() <= tolerance * tolerance;
    }

    // ------------------------------------------------------------------ conversion

    /**
     * Returns the components as a two-element array {@code {x, y}}.
     *
     * @return a new array containing {@code x} and {@code y}
     */
    public double[] toArray() {
        return new double[]{x, y};
    }

    /**
     * Returns a human-readable representation of this vector.
     *
     * @return a string of the form {@code Vector2D(x, y)}
     */
    @Override
    public String toString() {
        return "Vector2D(" + x + ", " + y + ")";
    }
}