package com.rips7.sketchkit.input;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tracks the current keyboard state: which keys are held down, and the most recent key character
 * and key code.
 *
 * <p>State is updated by the listener methods before the corresponding callback on
 * {@link com.rips7.sketchkit.render.Sketch} is invoked, so callbacks always see the new state.
 *
 * <p>Multiple keys can be held simultaneously. Use {@link #isKeyDown(int)} with a
 * {@link KeyEvent} virtual key code constant to test a specific key:
 * <pre>{@code
 * if (isKeyDown(KeyEvent.VK_LEFT)) { ... }
 * }</pre>
 */
public final class KeyState {

    private final Set<Integer> keysDown = new HashSet<>();

    private char key = KeyEvent.CHAR_UNDEFINED;
    private int keyCode = KeyEvent.VK_UNDEFINED;

    /**
     * Returns the character produced by the most recent key event.
     *
     * <p>For keys that do not produce a character (such as arrow keys or modifier keys) this
     * returns {@link KeyEvent#CHAR_UNDEFINED}. In that case, use {@link #keyCode()} instead.
     *
     * @return the most recent key character, or {@link KeyEvent#CHAR_UNDEFINED}
     */
    public char key() {
        return key;
    }

    /**
     * Returns the virtual key code of the most recent key press or release.
     *
     * <p>Key codes are the {@code VK_} constants defined on {@link KeyEvent}, for example
     * {@link KeyEvent#VK_LEFT}, {@link KeyEvent#VK_SHIFT} or {@link KeyEvent#VK_SPACE}.
     *
     * @return the most recent virtual key code
     */
    public int keyCode() {
        return keyCode;
    }

    /**
     * Returns whether any key is currently held down.
     *
     * @return {@code true} if at least one key is pressed
     */
    public boolean isKeyPressed() {
        return !keysDown.isEmpty();
    }

    /**
     * Returns whether a specific key is currently held down.
     *
     * <p>This allows multiple keys to be tested independently in the same frame:
     * <pre>{@code
     * if (isKeyDown(KeyEvent.VK_LEFT) && isKeyDown(KeyEvent.VK_SHIFT)) { ... }
     * }</pre>
     *
     * @param code the {@link KeyEvent} virtual key code to test
     * @return {@code true} if that key is currently held down
     */
    public boolean isKeyDown(final int code) {
        return keysDown.contains(code);
    }

    /**
     * Returns an unmodifiable view of all virtual key codes currently held down.
     *
     * @return the set of pressed key codes
     */
    public Set<Integer> keysDown() {
        return Collections.unmodifiableSet(keysDown);
    }

    /**
     * Records a key press.
     *
     * @param event the key event
     */
    public void onPressed(final KeyEvent event) {
        key = event.getKeyChar();
        keyCode = event.getKeyCode();
        keysDown.add(event.getKeyCode());
    }

    /**
     * Records a key release.
     *
     * @param event the key event
     */
    public void onReleased(final KeyEvent event) {
        key = event.getKeyChar();
        keyCode = event.getKeyCode();
        keysDown.remove(event.getKeyCode());
    }

    /**
     * Records a key typed event, updating only the character.
     *
     * <p>Key typed events fire only for keys that produce a character, so the key code is not
     * updated here.
     *
     * @param event the key event
     */
    public void onTyped(final KeyEvent event) {
        key = event.getKeyChar();
    }
}