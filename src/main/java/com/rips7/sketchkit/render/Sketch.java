package com.rips7.sketchkit.render;

import com.rips7.sketchkit.canvas.Canvas;
import com.rips7.sketchkit.input.MouseButton;
import com.rips7.sketchkit.input.MouseState;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Base class for animated sketches.
 *
 * <p>Extend this class and implement {@link #setup()} and {@link #draw()}. Constructing a
 * {@code Sketch} opens a window, calls {@link #setup()} once, and then repaints about 60 times
 * per second, calling {@link #draw()} for each frame. All drawing methods inherited from
 * {@link Canvas} are available inside {@link #draw()}.
 *
 * <h2>Mouse input</h2>
 *
 * <p>The current mouse state is available through {@link #mouseX()}, {@link #mouseY()},
 * {@link #isMousePressed()} and {@link #mouseButton()} and can be read at any point inside
 * {@link #draw()}. The previous frame's position is available through {@link #pmouseX()} and
 * {@link #pmouseY()}.
 *
 * <p>Override any of the mouse callback methods to respond to specific events:
 * <ul>
 *   <li>{@link #mousePressed()}</li>
 *   <li>{@link #mouseReleased()}</li>
 *   <li>{@link #mouseClicked()}</li>
 *   <li>{@link #mouseMoved()}</li>
 *   <li>{@link #mouseDragged()}</li>
 *   <li>{@link #mouseWheel(int)}</li>
 * </ul>
 *
 * <p>All callbacks run on the Swing event thread and see the updated mouse state.
 *
 * <h2>Example</h2>
 *
 * <pre>{@code
 * public class MySketch extends Sketch {
 *
 *     public MySketch() {
 *         super("My Sketch", 800, 600);
 *     }
 *
 *     public void setup() { }
 *
 *     public void draw() {
 *         background(Color.BLACK);
 *         fill(Color.WHITE);
 *         circle(mouseX(), mouseY(), 50);
 *     }
 *
 *     public void mousePressed() {
 *         if (mouseButton() == MouseButton.LEFT) {
 *             // handle left click
 *         }
 *     }
 * }
 * }</pre>
 */
public abstract class Sketch extends Canvas {

    private static final int FPS = 60;
    private static final int FRAME_DELAY = 1000 / FPS;

    private final Timer drawTimer;
    private final MouseState mouseState = new MouseState();

    private double deltaTime;
    private long lastFrameTime;

    private volatile boolean setupCompleted = false;

    private boolean drawLooping = true;
    private boolean redrawRequested;

    /**
     * Creates the sketch window, runs {@link #setup()} and starts the animation loop.
     *
     * <p>The window is centred on screen, and closing it exits the application.
     *
     * <p><b>Note:</b> {@link #setup()} is invoked from this constructor, so it runs before any
     * field initialisers or constructor body of a subclass have executed. Avoid relying on
     * subclass state inside {@code setup()}.
     *
     * @param title  the window title
     * @param width  the initial window width in pixels
     * @param height the initial window height in pixels
     */
    protected Sketch(final String title, final int width, final int height) {
        final JFrame frame = new JFrame(title);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(width, height));
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        registerMouseListeners();

        drawTimer = new Timer(FRAME_DELAY, event -> repaint());

        setup();
        setupCompleted = true;

        lastFrameTime = System.nanoTime();

        if (drawLooping) {
            drawTimer.start();
        }
    }

    // ------------------------------------------------------------------ timing

    /**
     * Returns the time elapsed between the previous frame and the current one.
     *
     * <p>The value is measured in seconds and updated immediately before each call to
     * {@link #draw()}. When drawing is paused the value remains unchanged until the next frame.
     *
     * @return the elapsed time in seconds
     */
    public final double deltaTime() {
        return deltaTime;
    }

    // ------------------------------------------------------------------ loop control

    /**
     * Stops the continuous drawing loop.
     *
     * <p>After this method is called, {@link #draw()} is not called unless a single frame is
     * requested with {@link #redraw()} or continuous drawing is resumed with {@link #loop()}.
     */
    public final void noLoop() {
        drawLooping = false;
        drawTimer.stop();
    }

    /**
     * Resumes continuous drawing after {@link #noLoop()}.
     *
     * <p>The elapsed time reported by {@link #deltaTime()} is reset to avoid including the
     * paused duration in the next frame.
     */
    public final void loop() {
        drawLooping = true;
        redrawRequested = false;
        lastFrameTime = System.nanoTime();
        drawTimer.start();
        repaint();
    }

    /**
     * Requests a single frame to be drawn while continuous drawing is paused.
     *
     * <p>Has no effect if continuous drawing is active. Multiple requests before the repaint is
     * processed may be coalesced into a single frame by Swing.
     */
    public final void redraw() {
        if (!drawLooping) {
            redrawRequested = true;
            repaint();
        }
    }

    // ------------------------------------------------------------------ mouse state

    /**
     * Returns the current mouse x-coordinate, relative to the canvas.
     *
     * @return the x-coordinate in pixels
     */
    public final int mouseX() {
        return mouseState.mouseX();
    }

    /**
     * Returns the current mouse y-coordinate, relative to the canvas.
     *
     * @return the y-coordinate in pixels
     */
    public final int mouseY() {
        return mouseState.mouseY();
    }

    /**
     * Returns the mouse x-coordinate at the end of the previous frame.
     *
     * @return the x-coordinate in pixels
     */
    public final int pmouseX() {
        return mouseState.pmouseX();
    }

    /**
     * Returns the mouse y-coordinate at the end of the previous frame.
     *
     * @return the y-coordinate in pixels
     */
    public final int pmouseY() {
        return mouseState.pmouseY();
    }

    /**
     * Returns whether any mouse button is currently held down.
     *
     * @return {@code true} if a button is pressed
     */
    public final boolean isMousePressed() {
        return mouseState.isMousePressed();
    }

    /**
     * Returns the mouse button currently held down.
     *
     * @return the button, or {@link MouseButton#NONE} if no button is pressed
     */
    public final MouseButton mouseButton() {
        return mouseState.mouseButton();
    }

    // ------------------------------------------------------------------ mouse callbacks

    /**
     * Called when a mouse button is pressed.
     *
     * <p>Override to handle mouse press events. The current button is available via
     * {@link #mouseButton()}.
     */
    public void mousePressed() { }

    /**
     * Called when a mouse button is released.
     *
     * <p>Override to handle mouse release events.
     */
    public void mouseReleased() { }

    /**
     * Called after a mouse button is pressed and released without the pointer moving.
     *
     * <p>Override to handle click events.
     */
    public void mouseClicked() { }

    /**
     * Called when the mouse moves with no button held.
     *
     * <p>Override to handle mouse movement. The current position is available via
     * {@link #mouseX()} and {@link #mouseY()}.
     */
    public void mouseMoved() { }

    /**
     * Called when the mouse moves with a button held down.
     *
     * <p>Override to handle drag events. The current position is available via
     * {@link #mouseX()} and {@link #mouseY()}, and the previous position via
     * {@link #pmouseX()} and {@link #pmouseY()}.
     */
    public void mouseDragged() { }

    /**
     * Called when the mouse wheel is rotated.
     *
     * <p>Override to handle scroll events.
     *
     * @param delta the number of notches rotated; negative when scrolled up (away from the
     *              user), positive when scrolled down (towards the user)
     */
    public void mouseWheel(final int delta) { }

    // ------------------------------------------------------------------ lifecycle

    /**
     * Called once, after the window is created and before the first frame is drawn.
     * Use it to initialise state.
     */
    public abstract void setup();

    /**
     * Called once per frame to draw the sketch.
     *
     * <p>Drawing methods inherited from {@link Canvas} should be called from here.
     */
    public abstract void draw();

    /**
     * Renders a frame if setup has completed and either continuous drawing is active or a single
     * frame has been requested.
     *
     * <p>Updates {@link #deltaTime()}, begins a drawing pass, calls {@link #draw()}, ends the
     * pass and updates the previous mouse position.
     *
     * @param graphics the graphics object to paint on
     */
    @Override
    protected final void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);

        if (!setupCompleted || (!drawLooping && !redrawRequested)) {
            return;
        }

        if (!drawLooping) {
            redrawRequested = false;
        }

        final long now = System.nanoTime();
        deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
        lastFrameTime = now;

        begin(graphics);
        draw();
        end();

        mouseState.endFrame();
    }

    // ------------------------------------------------------------------ private

    /**
     * Registers mouse listeners on this panel to update the mouse state and invoke callbacks.
     */
    private void registerMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                mouseState.onPressed(e);
                Sketch.this.mousePressed();
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                mouseState.onReleased(e);
                Sketch.this.mouseReleased();
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                mouseState.updatePosition(e);
                Sketch.this.mouseClicked();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                mouseState.updatePosition(e);
                Sketch.this.mouseMoved();
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                mouseState.updatePosition(e);
                Sketch.this.mouseDragged();
            }
        });

        addMouseWheelListener(e -> {
            mouseState.onWheel(e);
            Sketch.this.mouseWheel(mouseState.wheelDelta());
        });
    }
}
