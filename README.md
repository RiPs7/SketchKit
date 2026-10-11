# SketchKit

A lightweight Java 2D drawing library inspired by Processing.

SketchKit provides a simple, expressive drawing API on top of Java2D. The goal is to make creative coding in Java feel natural and straightforward, without hiding the underlying Java ecosystem.

---

## Quick Start

Extend `Sketch`, implement `setup()` and `draw()`, and use the inherited drawing API directly:

```java
public class MySketch extends Sketch {

    public MySketch() {
        super("My Sketch", 800, 600);
    }

    @Override
    public void setup() {
        // called once on startup
    }

    @Override
    public void draw() {
        background(Color.BLACK);
        fill(Color.WHITE);
        circle(width() / 2, height() / 2, 100);
    }

    public static void main(final String[] args) {
        new MySketch();
    }
}
```

---

## Architecture

SketchKit is built around a clear separation of three concerns.

```
Sketch          user-facing entry point; implements setup() and draw()
└── Canvas      high-level drawing API: shapes, colours, transforms
     └── Renderer   manages the Graphics2D surface and current style
          └── Shape  geometry; knows only how to fill and stroke itself
```

### Sketch

The main entry point. Users extend `Sketch` and implement `setup()` and `draw()`. The animation loop, window lifecycle and frame timing are handled automatically. All drawing operations are inherited from `Canvas`.

### Canvas

Provides the high-level drawing API: shapes, styling, transforms, background, colour utilities and more. Canvas methods delegate rendering to the `Renderer` and are available directly from `Sketch`.

### Renderer

Owns the `Graphics2D` surface and the current `RenderContext` (fill, stroke, stroke weight, drawing modes). When a shape is drawn, the renderer applies the current style and then delegates to the shape's own fill and stroke operations.

This keeps styling logic in one place. Adding a new shape does not require changing the renderer.

### Shape

Shapes contain geometry and implement two operations: `fill` and `stroke`. They are responsible for knowing how their geometry is drawn, but not how it is styled. The renderer has already configured the surface before invoking them.

---

## Design Principles

**Separation of concerns.** Geometry, styling and rendering are kept separate. Shapes know their geometry. The renderer owns the style. Canvas owns the API.

**New shapes are cheap.** Adding a shape means implementing two methods. The renderer, canvas and sketch do not need to change.

**Style is centralised.** Fill, stroke, and other settings live in the `RenderContext`. Every shape drawn after a style change uses the new style. This mirrors Processing's model and eliminates per-shape styling boilerplate.

**Java-native.** SketchKit sits on top of Java2D and Swing rather than replacing them. It is a convenience layer, not a separate runtime.

**Simple on the surface, clean underneath.** The API should feel as approachable as Processing. The implementation should feel like ordinary, well-structured Java.

---

## Requirements

- Java 21 or later
- A Java2D-compatible desktop environment (standard AWT/Swing; no external rendering engine required)

---

## Status

SketchKit is under active development. The API and architecture are still evolving and should be considered experimental.

---

## Inspiration

SketchKit is inspired by the philosophy and API of [Processing](https://processing.org/), particularly its approachable drawing primitives and mathematical utilities. It is not a Java port of Processing. It explores what a small, modern Java drawing library could look like while fitting naturally into an ordinary Java project.

---

## License

License information will be added as the project approaches its first release.