package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

class Ball extends Sprite {
    private static final int WINDOW_WIDTH = 1000; // Width of the game window
    private static final int WINDOW_HEIGHT = 500; // Height of the game window
    private ImageView imageView; // ImageView to represent the ball image
    private double velocityX = 0; // Initial velocity in X direction
    private double velocityY = 0; // Initial velocity in Y direction

    // Constructor to initialize the ball with its position, radius, and color
    public Ball(double x, double y, double radius, Color color) {
        super(x, y, radius, color);
        // Replace the circle with an ImageView
        Image ballImage = new Image(getClass().getResourceAsStream("/images/ball_soccer.png")); // Load ball image
        imageView = new ImageView(ballImage); // Create ImageView for the ball image
        imageView.setFitWidth(radius * 2); // Set width of the image view
        imageView.setFitHeight(radius * 2); // Set height of the image view
        imageView.setX(x - radius); // Set initial X position of the image view
        imageView.setY(y - radius); // Set initial Y position of the image view
    }

    // Method to update the position of the ball based on its velocity
    public void updatePosition() {
        double newX = imageView.getX() + velocityX; // Calculate new X position
        double newY = imageView.getY() + velocityY; // Calculate new Y position

        // Check for collision with the left and right edges of the window
        if (newX < 0 || newX + radius * 2 > WINDOW_WIDTH) {
            resetPosition(); // Reset ball position to center if it hits the edge
        } else {
            imageView.setX(newX); // Update the X position
        }

        // Check for collision with the top and bottom edges of the window
        if (newY < 0 || newY + radius * 2 > WINDOW_HEIGHT) {
            velocityY = -velocityY; // Reverse Y direction if it hits the edge
            newY = Math.max(0, Math.min(newY, WINDOW_HEIGHT - radius * 2)); // Prevent ball from going out of bounds
        }

        imageView.setY(newY); // Update the Y position
    }

    // Method to reset the ball position to the center
    private void resetPosition() {
        double centerX = (WINDOW_WIDTH - radius * 2) / 2; // Calculate center X position
        double centerY = (WINDOW_HEIGHT - radius * 2) / 2; // Calculate center Y position
        imageView.setX(centerX); // Set the X position to center
        imageView.setY(centerY); // Set the Y position to center
//        velocityX = 0; // Optionally reset velocity as well
//        velocityY = 0; // Optionally reset velocity as well
    }

    // Method to check collision with other sprites
    @Override
    public void checkCollision(Sprite other) {
        if (other instanceof Player) { // Check if the other sprite is a player
            Player player = (Player) other; // Cast the sprite to a player
            double dx = player.getCenterX() - this.getCenterX(); // Calculate the difference in X positions
            double dy = player.getCenterY() - this.getCenterY(); // Calculate the difference in Y positions
            double distance = Math.sqrt(dx * dx + dy * dy); // Calculate the distance between the ball and the player

            if (distance < radius + player.getRadius()) { // Check if the ball collides with the player
                double overlap = radius + player.getRadius() - distance; // Calculate the overlap distance
                double directionX = dx / distance; // Calculate the X direction of collision
                double directionY = dy / distance; // Calculate the Y direction of collision

                // Adjust the velocity of the ball based on the player it collides with
                if ("2".equals(player.getPlayerID()) || "4".equals(player.getPlayerID())) {
                    // Change the velocity to move in the opposite direction
                    velocityX = -velocityX;
                    velocityY = -velocityY;
                }

                // Adjust the ball's position to resolve the collision
                imageView.setX(imageView.getX() + directionX * overlap);
                imageView.setY(imageView.getY() + directionY * overlap);
            }
        }
    }

    // Getter for the radius of the ball
    @Override
    public double getRadius() {
        return radius;
    }

    // Getter for the X center position of the ball
    @Override
    public double getCenterX() {
        return imageView.getX() + radius;
    }

    // Getter for the Y center position of the ball
    @Override
    public double getCenterY() {
        return imageView.getY() + radius;
    }

    // Getter for the ImageView of the ball
    public ImageView getImageView() {
        return imageView;
    }
}
