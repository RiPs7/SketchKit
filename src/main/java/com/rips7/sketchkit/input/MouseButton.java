package com.rips7.sketchkit.input;

/**
 * A mouse button.
 *
 * <p>The value {@link #NONE} is used when no button is currently pressed, for example in
 * {@link com.rips7.sketchkit.render.Sketch#mouseButton()} while the mouse is moving freely.
 */
public enum MouseButton {

    /**
     * No button is pressed.
     */
    NONE,

    /**
     * The primary (usually left) button.
     */
    LEFT,

    /**
     * The middle button, often the scroll wheel click.
     */
    CENTER,

    /**
     * The secondary (usually right) button.
     */
    RIGHT
}