package com.rips7.sketchkit.canvas.config;

/**
 * Holds the horizontal and vertical text alignment used for text rendering as a
 * single record, since the two are always set and read together.
 *
 * <p>Defaults to {@link TextAlignH#LEFT} and {@link TextAlignV#BASELINE}, which matches
 * Java2D's native coordinate conventions and Processing's default behaviour.
 */
public final class TextAlignConfig implements Config<TextAlignConfig.TextAlign> {

    /**
     * A pair of horizontal and vertical alignment values.
     *
     * @param h the horizontal alignment
     * @param v the vertical alignment
     */
    public record TextAlign(TextAlignH h, TextAlignV v) { }

    private TextAlign value = new TextAlign(TextAlignH.LEFT, TextAlignV.BASELINE);

    /**
     * Returns the current text alignment pair.
     *
     * @return the current {@link TextAlign}; never {@code null}
     */
    @Override
    public TextAlign value() {
        return value;
    }

    /**
     * Sets the current text alignment pair.
     *
     * @param value the new {@link TextAlign}; must not be {@code null}
     */
    @Override
    public void value(final TextAlign value) {
        this.value = value;
    }
}