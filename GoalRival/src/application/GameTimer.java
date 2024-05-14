package application;

import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
    private Player player;
    private Ball ball;

    public GameTimer(Player player, Ball ball) {
        this.player = player;
        this.ball = ball;
    }

    @Override
    public void handle(long now) {
        player.checkCollision(ball);
        ball.checkCollision(player);
    }
}