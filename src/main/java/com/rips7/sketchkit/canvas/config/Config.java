package com.rips7.sketchkit.canvas.config;

/**
 * A single drawing setting that holds a value of type {@code T}.
 *
 * <p>Implementations (for example {@link FillConfig} or {@link EllipseConfig}) are stored in a
 * {@link com.rips7.sketchkit.canvas.context.ShapeContext}, which looks them up by their concrete
 * type.
 *
 * @param <T> the type of value held by this configuration
 */
public interface Config<T> {

    /**
     * Returns the current value of this setting.
     *
     * @return the current value, which may be {@code null} for settings that allow it
     */
    T value();

    /**
     * Replaces the current value of this setting.
     *
     * @param value the new value
     */
    void value(final T value);

}