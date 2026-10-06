package com.rips7.sketchkit.render;

import com.rips7.sketchkit.canvas.Canvas;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Base class for animated sketches.
 *
 * <p>Extend this class and implement {@link #setup()} and {@link #draw()}. Constructing a
 * {@code Sketch} opens a window, calls {@link #setup()} once, and then repaints about 60 times
 * per second, calling {@link #draw()} for each frame. All drawing methods inherited from
 * {@link Canvas} are available inside {@link #draw()}.
 *
 * <p>Example:
 * <pre>{@code
 * public class MySketch extends Sketch {
 *     MySketch() { super("My Sketch", 800, 600); }
 *
 *     public void setup() { }
 *
 *     public void draw() {
 *         fill(Color.RED);
 *         circle(width() / 2, height() / 2, 100);
 *     }
 * }
 * }</pre>
 */
public abstract class Sketch extends Canvas {

    private static final int FPS = 60;
    private static final int FRAME_DELAY = 1000 / FPS;

    private final Timer drawTimer;

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
     * <p><b>Note:</b> {@link #setup()} is invoked from this constructor, so it runs before any field
     * initialisers or constructor body of a subclass have executed. Avoid relying on subclass
     * state inside {@code setup()}.
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

        drawTimer = new Timer(FRAME_DELAY, event -> repaint());

        setup();
        setupCompleted = true;

        lastFrameTime = System.nanoTime();

        if (drawLooping) {
            drawTimer.start();
        }
    }

    /**
     * Returns the time elapsed between the previous frame and the current one.
     *
     * <p>The value is measured in seconds and is updated immediately before each call to
     * {@link #draw()}. When drawing is paused, the value remains unchanged until another frame
     * is rendered.
     *
     * @return the elapsed time in seconds
     */
    public final double deltaTime() {
        return deltaTime;
    }

    /**
     * Stops the continuous drawing loop.
     *
     * <p>After this method is called, timer-driven repaint requests stop and {@link #draw()}
     * is not called unless a single frame is requested using {@link #redraw()} or continuous
     * drawing is resumed using {@link #loop()}.
     *
     * <p>Any repaint requested by Swing while drawing is paused will not call {@link #draw()}.
     */
    public final void noLoop() {
        drawLooping = false;
        drawTimer.stop();
    }

    /**
     * Resumes continuous drawing.
     *
     * <p>The timer is started and a repaint is requested. The elapsed time reported by
     * {@link #deltaTime()} is reset relative to the time this method is called, avoiding
     * including the paused duration in the next frame's elapsed time.
     *
     * <p>Any pending single-frame redraw request is cleared.
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
     * <p>If continuous drawing is active, this method has no effect. If drawing is paused,
     * a repaint is requested and {@link #draw()} is called when that repaint is processed.
     *
     * <p>Multiple requests made before the repaint is processed may be coalesced into a single
     * frame by Swing.
     */
    public final void redraw() {
        if (!drawLooping) {
            redrawRequested = true;
            repaint();
        }
    }

    /**
     * Called once, after the window is created and before the first frame is drawn.
     * Use it to initialise state.
     */
    public abstract void setup();

    /**
     * Called to draw a frame when continuous drawing is active or a single frame has been
     * requested using {@link #redraw()}.
     *
     * <p>Drawing methods inherited from {@link Canvas} should generally be called from this
     * method.
     */
    public abstract void draw();

    /**
     * Renders a frame if setup has completed and either continuous drawing is active or a single
     * frame has been requested.
     *
     * <p>Updates {@link #deltaTime()}, begins a drawing pass, calls {@link #draw()} and ends the
     * pass. Repaint requests received while drawing is paused and no single frame is pending are
     * ignored.
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
    }
}