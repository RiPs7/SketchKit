package com.rips7.sketchkit.canvas.context;

import com.rips7.sketchkit.canvas.config.Config;
import com.rips7.sketchkit.canvas.config.EllipseConfig;
import com.rips7.sketchkit.canvas.config.EllipseMode;
import com.rips7.sketchkit.canvas.config.FillConfig;
import com.rips7.sketchkit.canvas.config.StrokeConfig;
import com.rips7.sketchkit.canvas.config.StrokeWeightConfig;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the drawing state that is shared by all shapes drawn through a
 * {@link com.rips7.sketchkit.canvas.renderer.ShapeRenderer}.
 *
 * <p>The context aggregates a {@link FillConfig}, a {@link StrokeConfig} and an
 * {@link EllipseConfig}. Shapes read these settings when they are drawn, so changing a setting
 * affects every shape drawn afterwards.
 *
 * <p>Defaults: black fill, black stroke and {@link EllipseMode#CENTER}.
 */
public final class ShapeContext {

    private final List<Config<?>> configs = new ArrayList<>();

    /**
     * Creates a context populated with the default fill, stroke and ellipse settings.
     */
    public ShapeContext() {
        configs.add(new FillConfig());
        configs.add(new StrokeConfig());
        configs.add(new StrokeWeightConfig());
        configs.add(new EllipseConfig());
    }

    /**
     * Returns the current fill colour.
     *
     * @return the fill colour, or {@code null} if filling is disabled
     */
    public Color fill() {
        return get(FillConfig.class);
    }

    /**
     * Sets the fill colour.
     *
     * @param color the new fill colour, or {@code null} to disable filling
     */
    public void fill(final Color color) {
        set(FillConfig.class, color);
    }

    /**
     * Returns the current stroke colour.
     *
     * @return the stroke colour, or {@code null} if outlines are disabled
     */
    public Color stroke() {
        return get(StrokeConfig.class);
    }

    /**
     * Sets the stroke colour.
     *
     * @param color the new stroke colour, or {@code null} to disable outlines
     */
    public void stroke(final Color color) {
        set(StrokeConfig.class, color);
    }

    /**
     * Returns the current stroke weight.
     *
     * @return the stroke weight
     */
    public double strokeWeight() {
        return get(StrokeWeightConfig.class);
    }

    /**
     * Sets the stroke weight.
     *
     * @param weight the new stroke weight
     */
    public void strokeWeight(final double weight) {
        set(StrokeWeightConfig.class, weight);
    }

    /**
     * Returns the current ellipse mode.
     *
     * @return the current {@link EllipseMode}
     */
    public EllipseMode ellipseMode() {
        return get(EllipseConfig.class);
    }

    /**
     * Sets the ellipse mode.
     *
     * @param ellipseMode the new {@link EllipseMode}
     */
    public void ellipseMode(final EllipseMode ellipseMode) {
        set(EllipseConfig.class, ellipseMode);
    }

    /**
     * Sets the value of the configuration of the given type.
     *
     * @param type  the concrete configuration class to update
     * @param value the new value
     * @param <V>   the type of value held by the configuration
     * @param <T>   the configuration type
     * @throws IllegalStateException if no configuration of the given type is registered
     */
    private <V, T extends Config<V>> void set(final Class<T> type, final V value) {
        getConfig(type).value(value);
    }

    /**
     * Gets the value of the configuration of the given type.
     *
     * @param type the concrete configuration class to read
     * @param <V>  the type of value held by the configuration
     * @param <T>  the configuration type
     * @return the current value of the configuration
     * @throws IllegalStateException if no configuration of the given type is registered
     */
    private <V, T extends Config<V>> V get(final Class<T> type) {
        return getConfig(type).value();
    }

    /**
     * Finds the registered configuration of the given type.
     *
     * @param type the concrete configuration class to look up
     * @param <V>  the type of value held by the configuration
     * @param <T>  the configuration type
     * @return the first registered configuration that is an instance of {@code type}
     * @throws IllegalStateException if no configuration of the given type is registered
     */
    private <V, T extends Config<V>> T getConfig(final Class<T> type) {
        return configs.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No config found for " + type.getSimpleName()));
    }
}