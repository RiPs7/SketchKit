package com.rips7.sketchkit.canvas.config;

/**
 * Setting that holds the stroke weight used to draw shape outlines and lines.
 *
 * <p>Defaults to {@code 1.0} (note that 1.0 is the lowest valid value).
 */
public final class StrokeWeightConfig implements Config<Double> {

    private Double weight = 1.0;

    /**
     * Returns the current stroke weight.
     *
     * @return the stroke weight
     */
    @Override
    public Double value() {
        return weight;
    }

    /**
     * Sets the stroke weight.
     *
     * <p>If a value less than 1.0 is given, the stroke weight is set to 1.0</p>
     *
     * @param value the new stroke weight,
     */
    @Override
    public void value(final Double value) {
        this.weight = Math.max(1.0, value);
    }
}