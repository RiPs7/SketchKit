package com.rips7.sketchkit.canvas.config;

import java.awt.Color;

/**
 * Setting that holds the colour used to draw shape outlines and lines.
 *
 * <p>Defaults to {@link Color#BLACK}. A {@code null} value means outlines are not drawn.
 */
public final class StrokeConfig implements Config<Color> {

    private Color color = Color.BLACK;

    /**
     * Returns the current stroke colour.
     *
     * @return the stroke colour, or {@code null} if outlines should not be drawn
     */
    @Override
    public Color value() {
        return color;
    }

    /**
     * Sets the stroke colour.
     *
     * @param value the new stroke colour, or {@code null} to disable outlines
     */
    @Override
    public void value(final Color value) {
        this.color = value;
    }
}