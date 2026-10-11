package com.rips7.sketchkit.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Tracks the current and previous mouse position, the pressed state, and which button is held.
 *
 * <p>State is updated by the listener methods before the corresponding callback on
 * {@link com.rips7.sketchkit.render.Sketch} is invoked, so callbacks always see the new state.
 * The previous position ({@link #pmouseX()}, {@link #pmouseY()}) is updated once per frame, at
 * the end of {@code draw()}, so it reflects where the mouse was at the start of the current frame.
 */
public final class MouseState {

    private int mouseX;
    private int mouseY;
    private int pmouseX;
    private int pmouseY;
    private boolean pressed;
    private MouseButton button = MouseButton.NONE;
    private int wheelDelta;

    /**
     * Returns the current mouse x-coordinate.
     *
     * @return the x-coordinate in pixels, relative to the canvas
     */
    public int mouseX() {
        return mouseX;
    }

    /**
     * Returns the current mouse y-coordinate.
     *
     * @return the y-coordinate in pixels, relative to the canvas
     */
    public int mouseY() {
        return mouseY;
    }

    /**
     * Returns the mouse x-coordinate at the end of the previous frame.
     *
     * @return the x-coordinate in pixels, relative to the canvas
     */
    public int pmouseX() {
        return pmouseX;
    }

    /**
     * Returns the mouse y-coordinate at the end of the previous frame.
     *
     * @return the y-coordinate in pixels, relative to the canvas
     */
    public int pmouseY() {
        return pmouseY;
    }

    /**
     * Returns whether any mouse button is currently held down.
     *
     * @return {@code true} if a button is pressed
     */
    public boolean isMousePressed() {
        return pressed;
    }

    /**
     * Returns the mouse button currently held down.
     *
     * @return the button, or {@link MouseButton#NONE} if no button is pressed
     */
    public MouseButton mouseButton() {
        return button;
    }

    /**
     * Returns the scroll wheel delta from the most recent wheel event.
     *
     * <p>Negative values indicate scrolling up (away from the user); positive values indicate
     * scrolling down (towards the user). The value is {@code 0} if no wheel event has occurred.
     *
     * @return the wheel rotation in notches
     */
    public int wheelDelta() {
        return wheelDelta;
    }

    /**
     * Updates the current position from a mouse event.
     *
     * @param event the mouse event
     */
    public void updatePosition(final MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
    }

    /**
     * Records a button press.
     *
     * @param event the mouse event
     */
    public void onPressed(final MouseEvent event) {
        updatePosition(event);
        pressed = true;
        button = toButton(event.getButton());
    }

    /**
     * Records a button release.
     *
     * @param event the mouse event
     */
    public void onReleased(final MouseEvent event) {
        updatePosition(event);
        pressed = false;
        button = MouseButton.NONE;
    }

    /**
     * Records a wheel movement.
     *
     * @param event the mouse wheel event
     */
    public void onWheel(final MouseWheelEvent event) {
        updatePosition(event);
        wheelDelta = event.getWheelRotation();
    }

    /**
     * Snapshots the current position as the previous position.
     *
     * <p>Called at the end of each frame, after {@code draw()} returns.
     */
    public void endFrame() {
        pmouseX = mouseX;
        pmouseY = mouseY;
        wheelDelta = 0;
    }

    private static MouseButton toButton(final int awtButton) {
        return switch (awtButton) {
            case MouseEvent.BUTTON1 -> MouseButton.LEFT;
            case MouseEvent.BUTTON2 -> MouseButton.CENTER;
            case MouseEvent.BUTTON3 -> MouseButton.RIGHT;
            default -> MouseButton.NONE;
        };
    }
}
