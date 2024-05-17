package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Goal extends Sprite {
    private Rectangle rectangle;
    private ScoreBoard scoreBoard;
    private boolean isLeftGoal;

    public Goal(double x, double y, double width, double height, Color color, ScoreBoard scoreBoard, boolean isLeftGoal) {
        super(x, y, 0, color); // Pass radius as 0 because we won't use the circle in Goal
        rectangle = new Rectangle(x, y, width, height);
        rectangle.setFill(color);
        this.scoreBoard = scoreBoard;
        this.isLeftGoal = isLeftGoal;
    }

    @Override
    public void checkCollision(Sprite other) {
        if (rectangle.getBoundsInParent().intersects(other.getCircle().getBoundsInParent())) {
            if (other instanceof Ball) {
                // Reset the ball position if it collides with the goal
                other.getCircle().setCenterX(500);
                other.getCircle().setCenterY(250);
                if (isLeftGoal) {
                    scoreBoard.incrementRightScore();
                } else {
                    scoreBoard.incrementLeftScore();
                }
            }
        }
    }

    @Override
    public double getRadius() {
        return 0; // Not used for Goal
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
