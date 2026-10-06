package com.rips7.sketchkit.canvas.config;

/**
 * Setting that controls how the position of an ellipse is interpreted.
 *
 * <p>Defaults to {@link EllipseMode#CENTER}.
 *
 * @see EllipseMode
 */
public final class EllipseConfig implements Config<EllipseMode> {

    private EllipseMode mode = EllipseMode.CENTER;

    /**
     * Returns the current ellipse mode.
     *
     * @return the current {@link EllipseMode}
     */
    @Override
    public EllipseMode value() {
        return mode;
    }

    /**
     * Sets the ellipse mode.
     *
     * @param value the new {@link EllipseMode}
     */
    @Override
    public void value(final EllipseMode value) {
        mode = value;
    }
}