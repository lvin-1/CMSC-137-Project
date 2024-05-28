package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

class Ball extends Sprite {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 500;
    private ImageView imageView;
    private double velocityX = 0; // Initial velocity in X direction
    private double velocityY = 0; // Initial velocity in Y direction

    public Ball(double x, double y, double radius, Color color) {
        super(x, y, radius, color);
        // Replace the circle with an ImageView
        Image ballImage = new Image(getClass().getResourceAsStream("/images/ball_soccer.png"));
        imageView = new ImageView(ballImage);
        imageView.setFitWidth(radius * 2);
        imageView.setFitHeight(radius * 2);
        imageView.setX(x - radius);
        imageView.setY(y - radius);
    }

    public void updatePosition() {
        double newX = imageView.getX() + velocityX;
        double newY = imageView.getY() + velocityY;

        // Check for collision with the left and right edges of the window
        if (newX < 0 || newX + radius * 2 > WINDOW_WIDTH) {
            resetPosition(); // Reset ball position to center
        } else {
            imageView.setX(newX);
        }

        // Check for collision with the top and bottom edges of the window
        if (newY < 0 || newY + radius * 2 > WINDOW_HEIGHT) {
            velocityY = -velocityY; // Reverse Y direction
            newY = Math.max(0, Math.min(newY, WINDOW_HEIGHT - radius * 2)); // Prevent ball from going out of bounds
        }

        imageView.setY(newY);
    }

    private void resetPosition() {
        double centerX = (WINDOW_WIDTH - radius * 2) / 2;
        double centerY = (WINDOW_HEIGHT - radius * 2) / 2;
        imageView.setX(centerX);
        imageView.setY(centerY);
        velocityX = 0; // Optionally reset velocity as well
        velocityY = 0; // Optionally reset velocity as well
    }

    @Override
    public void checkCollision(Sprite other) {
        // Collision detection with other sprites can be handled here
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public double getCenterX() {
        return imageView.getX() + radius;
    }

    @Override
    public double getCenterY() {
        return imageView.getY() + radius;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
