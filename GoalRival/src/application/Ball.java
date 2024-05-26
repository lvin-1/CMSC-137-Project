package application;

import javafx.scene.paint.Color;

class Ball extends Sprite {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 500;

    public Ball(double x, double y, double radius, Color color) {
        super(x, y, radius, color);
    }

    @Override
    public void checkCollision(Sprite other) {
        // Check collision with left and right screen edges
        if (circle.getCenterX() > WINDOW_WIDTH || circle.getCenterX() < 0) {
            circle.setCenterX(WINDOW_WIDTH / 2);
            circle.setCenterY(WINDOW_HEIGHT / 2);
        }

        // Check collision with top and bottom screen edges
        if (circle.getCenterY() - radius < 0 || circle.getCenterY() + radius > WINDOW_HEIGHT) {
            circle.setCenterY(Math.max(radius, Math.min(circle.getCenterY(), WINDOW_HEIGHT - radius)));
        }

//        if (circle.getCenterY() > WINDOW_HEIGHT - radius || circle.getCenterY() < radius) {
//            circle.setCenterY(WINDOW_HEIGHT / 2);
//        }
    }




    @Override
    public double getRadius() {
        return radius;
    }
}