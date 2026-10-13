package com.rips7.sketchkit.canvas.config;

/**
 * Controls how the x-coordinate passed to
 * {@link com.rips7.sketchkit.canvas.Canvas#text(String, double, double)} is interpreted.
 *
 * <p>This mirrors the horizontal component of Processing's {@code textAlign()} function.
 */
public enum TextAlignH {

    /**
     * The x-coordinate is the left edge of the text.
     *
     * <p>This is the default.
     */
    LEFT,

    /**
     * The x-coordinate is the horizontal centre of the text.
     */
    CENTER,

    /**
     * The x-coordinate is the right edge of the text.
     */
    RIGHT
}