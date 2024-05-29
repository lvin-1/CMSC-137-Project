package application;

import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
    private Player[] players; // Array of players in the game
    private Ball ball; // The ball used in the game
    private Goal leftGoal; // The left goal
    private Goal rightGoal; // The right goal
    private ScoreBoard scoreBoard; // The scoreboard tracking the scores

    // Constructor to initialize the game timer with players, ball, goals, and scoreboard
    public GameTimer(Player[] players, Ball ball, Goal leftGoal, Goal rightGoal, ScoreBoard scoreBoard) {
        this.players = players;
        this.ball = ball;
        this.leftGoal = leftGoal;
        this.rightGoal = rightGoal;
        this.scoreBoard = scoreBoard;
    }

    @Override
    public void handle(long now) {
        // Check for collisions between players and the ball
        for (Player player : this.players) {
            if (player != null) {
                player.checkCollision(ball); // Check if player collides with the ball
                ball.checkCollision(player); // Check if ball collides with the player
            }
        }

        // Update the ball's position
        ball.updatePosition();

        // Check for collisions between the ball and the goals
        leftGoal.checkCollision(ball); // Check if ball collides with the left goal
        rightGoal.checkCollision(ball); // Check if ball collides with the right goal

        // Check if the game should be stopped based on the scores
        if (scoreBoard.getLeftScore() >= 5 || scoreBoard.getRightScore() >= 5) {
            this.stop(); // Stop the game if either team reaches 5 points
        }
    }
}
