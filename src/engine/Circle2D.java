package engine;

import javafx.scene.canvas.GraphicsContext;

/**
 * Simple class for representing a 2D circle, optionally textured.
 */
public class Circle2D extends GraphicsEntity {
    private double _radius;

    public Circle2D(double x, double y, double radius, double depth) {
        setLocationXYDepth(x, y, depth);
        _radius = radius;
        setWidthHeight(radius, radius);
    }

    public double getRadius() {
        return getWidth();
    }

    public void setRadius(double radius) {
        _radius = radius;
        setWidthHeight(radius, radius);
    }

    public void setWidthHeight(double width, double height) {
        super.setWidthHeight(_radius, _radius);
    }

    @Override
    public void render(GraphicsContext gc, double x, double y) {
        gc.fillOval(x, y, getWidth(), getHeight());
    }
}
