package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Goal extends Sprite {
    private Rectangle rectangle; // The rectangle representing the goal area
    private ScoreBoard scoreBoard; // Reference to the scoreboard to update scores
    private boolean isLeftGoal; // Flag to determine if this is the left or right goal

    // Constructor to initialize the goal with position, dimensions, color, scoreboard, and goal side
    public Goal(double x, double y, double width, double height, Color color, ScoreBoard scoreBoard, boolean isLeftGoal) {
        super(x, y, 0, color); // Pass radius as 0 because we won't use the circle in Goal
        rectangle = new Rectangle(x, y, width, height); // Create a rectangle representing the goal
        rectangle.setFill(color); // Set the color of the rectangle
        this.scoreBoard = scoreBoard; // Assign the scoreboard reference
        this.isLeftGoal = isLeftGoal; // Set if this is the left goal or not
    }

    @Override
    public void checkCollision(Sprite other) {
        // Check if the ball collides with the goal
        if (rectangle.getBoundsInParent().intersects(((Ball) other).getImageView().getBoundsInParent())) {
            if (other instanceof Ball) {
                // Reset the ball position to the center if it collides with the goal
                ((Ball) other).getImageView().setX(500 - other.getRadius());
                ((Ball) other).getImageView().setY(250 - other.getRadius());
                // Update the score based on which goal was hit
                if (isLeftGoal) {
                    scoreBoard.incrementRightScore(); // Increment the right team's score
                } else {
                    scoreBoard.incrementLeftScore(); // Increment the left team's score
                }
            }
        }
    }

    @Override
    public double getRadius() {
        return 0; // Radius is not used for Goal
    }

    public Rectangle getRectangle() {
        return rectangle; // Getter for the goal's rectangle
    }
}
