package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

// Abstract class representing a sprite in a game
abstract class Sprite {
    protected Circle circle; // Circle representing the visual shape of the sprite
    protected double radius; // Radius of the sprite's circle

    // Constructor to initialize the sprite with position, radius, and color
    public Sprite(double x, double y, double radius, Color color) {
        this.radius = radius; // Set the radius
        circle = new Circle(x, y, radius, color); // Create a new Circle with specified position, radius, and color
    }

    // Abstract method to check collision with another sprite
    public abstract void checkCollision(Sprite other);

    // Abstract method to get the radius of the sprite
    public abstract double getRadius();

    // Method to get the X coordinate of the circle's center
    public double getCenterX() {
        return circle.getCenterX();
    }

    // Method to get the Y coordinate of the circle's center
    public double getCenterY() {
        return circle.getCenterY();
    }

    // Method to get the Circle object representing the sprite
    public Circle getCircle() {
        return circle;
    }
}
