package com.rips7.sketchkit.canvas.config;

import java.awt.Font;

/**
 * Holds the font used for text rendering.
 *
 * <p>Defaults to 12 pt plain SansSerif, which is always available in the JDK as a
 * logical font name and maps to an appropriate platform sans-serif at runtime.
 */
public final class TextFontConfig implements Config<Font> {

    private static final Font DEFAULT = new Font("SansSerif", Font.PLAIN, 12);

    private Font value = DEFAULT;

    /**
     * Returns the current font.
     *
     * @return the current {@link Font}; never {@code null}
     */
    @Override
    public Font value() {
        return value;
    }

    /**
     * Sets the current font.
     *
     * @param value the new {@link Font}; must not be {@code null}
     */
    @Override
    public void value(final Font value) {
        this.value = value;
    }
}