package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

abstract class Sprite {
    protected Circle circle;
    protected double radius;

    public Sprite(double x, double y, double radius, Color color) {
        this.radius = radius;
        circle = new Circle(x, y, radius, color);
    }

    public abstract void checkCollision(Sprite other);

    public abstract double getRadius();

    public double getCenterX() {
        return circle.getCenterX();
    }

    public double getCenterY() {
        return circle.getCenterY();
    }

    public Circle getCircle() {
        return circle;
    }
}

