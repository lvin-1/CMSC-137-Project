package application;

import javafx.scene.paint.Color;

class Ball extends Sprite {
    public Ball(double x, double y, double radius, Color color) {
        super(x, y, radius, color);
    }

    @Override
    public void checkCollision(Sprite other) {
        if (circle.getCenterX() > 1000 || circle.getCenterX() < 0) {
            circle.setCenterX(500);
            circle.setCenterY(250);
        }
    }
    
    @Override
    public double getRadius() {
        return radius;
    }
}