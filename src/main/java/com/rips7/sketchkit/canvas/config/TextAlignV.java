package com.rips7.sketchkit.canvas.config;

/**
 * Controls how the y-coordinate passed to
 * {@link com.rips7.sketchkit.canvas.Canvas#text(String, double, double)} is interpreted.
 *
 * <p>This mirrors the vertical component of Processing's {@code textAlign()} function.
 */
public enum TextAlignV {

    /**
     * The y-coordinate is the typographic baseline of the text.
     *
     * <p>This is Java2D's native coordinate and the default, so no adjustment is applied.
     */
    BASELINE,

    /**
     * The y-coordinate is the top of the ascender line (the tallest glyph in the font).
     */
    TOP,

    /**
     * The y-coordinate is the visual centre between the ascent and descent.
     */
    CENTER,

    /**
     * The y-coordinate is the bottom of the descender line.
     */
    BOTTOM
}