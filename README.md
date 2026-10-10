## SketchKit

A lightweight Java 2D drawing library inspired by Processing.

SketchKit provides a simple, expressive drawing API on top of Java2D, with support for shapes, colours, fills, strokes, custom paths, drawing modes, and common mathematical utilities.

The goal is to make creative coding in Java feel straightforward without hiding the underlying Java ecosystem.

### Features
- Simple, Processing-inspired drawing API
- Java2D-based rendering
- Basic geometric shapes
- Lines
- Rectangles
- Ellipses
- Circles
- Fill and stroke styling
- Configurable stroke weight
- Ellipse positioning modes
- Custom shapes using a fluent vertex builder
- Background colours
- Mathematical utilities commonly useful in creative coding
- Separation between geometry, styling, and rendering
- Designed to be used as a reusable Java library

### Design

SketchKit is built around the following architecture:
```
   User
     │
     ▼
   Sketch
     │
     │ setup() / draw()
     │
     ▼
   Canvas
     │
     │ drawing API
     │
     ▼
   ShapeRenderer
     │
     │ applies current style and renders shapes
     │
     ▼
   Shape
┌────┼──────────┬───────┐
▼    ▼          ▼       ▼
Line Rectangle Ellipse Path
```

#### Sketch

Sketch is the main user-facing entry point.

Users extend `Sketch` and implement `setup()` and `draw()`. The `setup()` method is called once when the sketch starts, while `draw()` is called once per frame.

All drawing operations are inherited from `Canvas` and can be used directly inside `draw()`.

For example:

```java
public class MySketch extends Sketch {

  public MySketch() {
    super("My Sketch", 800, 600);
  }

  @Override
  public void setup() {
  }

  @Override
  public void draw() {
    background(Color.BLACK);

    fill(Color.WHITE);
    circle(100, 100, 50);

    noFill();
    stroke(Color.RED);
    rect(200, 100, 100, 100);
  }
}
```

#### Canvas

Canvas provides the high-level drawing API inherited by Sketch.

It exposes operations such as background(), fill(), stroke(), line(), rect(), ellipse(), circle(), and custom shape construction.

Canvas delegates the actual rendering to a ShapeRenderer.

#### ShapeRenderer

ShapeRenderer is responsible for rendering shapes onto a Java2D Graphics2D surface.

It owns the current ShapeContext and centralises styling.

In particular, the renderer is responsible for:

- Deciding whether a shape should be filled
- Applying the current fill colour
- Applying the current stroke colour
- Applying the current stroke weight
- Invoking the appropriate shape operation

This keeps styling out of individual shape implementations.

Conceptually:

```
ShapeRenderer
     │
     ├── fill colour
     ├── stroke colour
     ├── stroke weight
     └── ShapeContext
              │
              ▼
              Shape
            ┌───────┐
            │ fill  │
            │ stroke│
            └───────┘
```


#### Shape

Shape represents drawable geometry.

Shapes provide two operations:

```java
void fill(Graphics2D graphics, ShapeContext context);

void stroke(Graphics2D graphics, ShapeContext context);
```

A shape is responsible for knowing how its geometry is filled or stroked, but not how it is styled.

For example, Rectangle can simply do:

```java
graphics.fill(rectangle);
```

and:

```java
graphics.draw(rectangle);
```

The renderer has already configured the appropriate colour and stroke.

This separation makes it possible to add new shapes without duplicating rendering and styling logic.

### Built-in Shapes

SketchKit currently provides several basic shapes.

#### Line

```java
line(50, 50, 200, 150);
```

Lines have no fillable area and are affected only by the current stroke settings.

#### Rectangle

```java
rect(100, 100, 200, 100);
```

#### Ellipse

```java
ellipse(200, 150, 100, 60);
```

Ellipses support different positioning modes.

For example:

```java
ellipseMode(EllipseMode.CENTER);
```

allows the supplied coordinates to represent the centre of the ellipse rather than its top-left corner.

Circle

```java
circle(200, 150, 100);
```

A circle is a convenience operation for drawing an ellipse with equal width and height.

#### Path

Path represents custom geometry built from a Java2D Path2D.

It supports both filling and stroking.

Custom paths are exposed through the higher-level shape builder API.

#### Custom Shapes

Custom shapes can be constructed using vertices:

```java
beginShape()
    .vertex(100, 100)
    .vertex(200, 100)
    .vertex(250, 200)
    .vertex(150, 250)
    .endShape();
```

The builder creates the underlying path and closes it when endShape() is called.

This allows shapes that are not provided as built-in primitives without requiring the user to work directly with Java2D.

### Styling

SketchKit uses a Processing-inspired styling model.

#### Fill

```java
fill(Color.RED);
```

Disable filling:

```java
noFill();
```

#### Stroke

```java
stroke(Color.BLACK);
```

Disable the stroke:

```java
noStroke();
```

#### Stroke Weight

```java
strokeWeight(4);
```

Stroke configuration is applied centrally by the renderer rather than by individual shapes.


#### Background

The entire drawing surface can be cleared with:

```java
background(Color.WHITE);
```

Changing the background does not modify the current fill or stroke settings.

For example:

```java
fill(Color.RED);

background(Color.WHITE);

// Fill is still red
circle(100, 100, 50);
```

### Mathematical Utilities

SketchKit also provides common mathematical functions useful for drawing and animation.

The Maths utility class includes functions such as:

```java
Maths.map(value, fromMin, fromMax, toMin, toMax);

Maths.lerp(start, stop, amount);

Maths.constrain(value, min, max);

Maths.norm(value, start, stop);

Maths.dist(x1, y1, x2, y2);

Maths.sq(value);

Maths.mag(x, y);

Maths.radians(degrees);

Maths.degrees(radians);
```

It also provides common constants:

```java
Maths.PI
Maths.HALF_PI
Maths.TWO_PI
```
### Architecture

SketchKit deliberately separates three concerns.

1. User API

`Sketch` is the main user-facing entry point.

Users extend `Sketch` and implement `setup()` and `draw()`. The drawing API is inherited from `Canvas` and is available directly inside `draw()`.

2. Drawing API

`Canvas` provides the high-level drawing operations, such as shapes, colours, stroke settings, background drawing, and custom shape construction.

3. Rendering and Geometry

`ShapeRenderer` manages the Java2D graphics context and current drawing style.

Shapes contain their own geometry and know how to perform their fill and stroke operations.

The overall relationship is:

```
Sketch
└── Canvas
     └── ShapeRenderer
          └── Shape
               ├── Line
               ├── Rectangle
               ├── Ellipse
               └── Path
```

This separation means that adding a new shape should generally not require changes to the renderer.

For example, a future `Triangle` could implement:

```java
public final class Triangle implements Shape {

    @Override
    public void fill(
            final Graphics2D graphics,
            final ShapeContext context) {

        // Fill triangle geometry
    }

    @Override
    public void stroke(
            final Graphics2D graphics,
            final ShapeContext context) {

        // Stroke triangle geometry
    }
}
```

The renderer would not need to know that Triangle exists. It only needs to know how to apply the current drawing style and invoke the shape's fill and stroke operations.

### Requirements
- Java 21 or later
- A Java2D-compatible desktop environment

SketchKit currently uses standard Java AWT/Swing and does not require an external rendering engine.

### Status

SketchKit is currently under active development.

The API and internal architecture are still evolving as the library grows. The current focus is on establishing a clean foundation for:

- Primitive shapes
- Custom shapes
- Drawing styles
- Rendering
- Transformations
- Animation
- Input handling
- Creative-coding utilities

The API should therefore be considered experimental for now.

### Philosophy

SketchKit is intended to sit somewhere between raw Java2D and larger creative-coding frameworks.

Java2D is powerful, but drawing directly with it often involves dealing with implementation details:

```java
Graphics2D g2d = ...;

g2d.setColor(Color.RED);
g2d.setStroke(new BasicStroke(3));
g2d.fill(...);

g2d.setColor(Color.BLACK);
g2d.draw(...);
```

SketchKit aims to make the common case much simpler:

```java
fill(Color.RED);
stroke(Color.BLACK);
strokeWeight(3);

circle(200, 200, 100);
```

At the same time, the library is intentionally built on top of Java's existing graphics APIs rather than attempting to replace them.

The goal is simple on the surface, clean underneath.

### Inspiration

SketchKit is inspired by the philosophy and API design of Processing, particularly its approachable drawing primitives and mathematical utilities.

It is not intended to be a Java port of Processing.

Instead, it explores what a small, modern Java drawing library could look like while retaining access to Java2D and fitting naturally into a normal Java project.

### License

License information will be added as the project approaches its first release.