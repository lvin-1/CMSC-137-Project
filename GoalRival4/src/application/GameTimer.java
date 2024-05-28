package application;

import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
    private Player[] players;
    private Ball ball;
    private Goal leftGoal;
    private Goal rightGoal;

    public GameTimer(Player[] players, Ball ball, Goal leftGoal, Goal rightGoal) {
        this.players = players;
        this.ball = ball;
        this.leftGoal = leftGoal;
        this.rightGoal = rightGoal;
    }

    @Override
    public void handle(long now) {
      for(Player player : this.players) {
    	  if(player != null) {
    		  player.checkCollision(ball);
        	  ball.checkCollision(player);
    	  }

      }
      ball.updatePosition();
      leftGoal.checkCollision(ball);
      rightGoal.checkCollision(ball);
    }
}
