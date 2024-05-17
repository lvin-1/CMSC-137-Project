package application;

import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
    private Player player;
    private Ball ball;
    private Goal leftGoal;
    private Goal rightGoal;

    public GameTimer(Player player, Ball ball, Goal leftGoal, Goal rightGoal) {
        this.player = player;
        this.ball = ball;
        this.leftGoal = leftGoal;
        this.rightGoal = rightGoal;
    }

    @Override
    public void handle(long now) {
        player.checkCollision(ball);
        ball.checkCollision(player);
        leftGoal.checkCollision(ball);
        rightGoal.checkCollision(ball);
    }
}
