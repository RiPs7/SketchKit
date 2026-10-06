package com.rips7.sketchkit.canvas.config;

/**
 * Determines how the {@code x} and {@code y} arguments of an ellipse are interpreted.
 */
public enum EllipseMode {

    /**
     * {@code x} and {@code y} specify the centre of the ellipse.
     */
    CENTER,

    /**
     * {@code x} and {@code y} specify the top-left corner of the ellipse's bounding box.
     */
    CORNER
}