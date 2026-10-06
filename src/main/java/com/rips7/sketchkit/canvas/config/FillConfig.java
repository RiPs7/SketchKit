package com.rips7.sketchkit.canvas.config;

import java.awt.Color;

/**
 * Setting that holds the colour used to fill shapes.
 *
 * <p>Defaults to {@link Color#BLACK}. A {@code null} value means shapes are not filled.
 */
public final class FillConfig implements Config<Color> {

    private Color color = Color.BLACK;

    /**
     * Returns the current fill colour.
     *
     * @return the fill colour, or {@code null} if shapes should not be filled
     */
    @Override
    public Color value() {
        return color;
    }

    /**
     * Sets the fill colour.
     *
     * @param value the new fill colour, or {@code null} to disable filling
     */
    @Override
    public void value(final Color value) {
        this.color = value;
    }
}