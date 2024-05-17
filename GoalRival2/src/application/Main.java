package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 500;
    private static final double PLAYER_RADIUS = 30;
    private static final double BALL_RADIUS = 20;
    private static final double GOAL_WIDTH = 10;
    private static final double GOAL_HEIGHT = 200;

    private Player player;
    private Ball ball;
    private GameStage gameStage;
    private GameTimer gameTimer;
    private Goal leftGoal;
    private Goal rightGoal;
    private ScoreBoard scoreBoard;

    @Override
    public void start(Stage primaryStage) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getLocalHost();
            int serverPort = 5000;

            gameStage = new GameStage(primaryStage);

            scoreBoard = new ScoreBoard(gameStage.getRoot());

            player = new Player(WINDOW_WIDTH / 4, WINDOW_HEIGHT / 2, PLAYER_RADIUS, Color.BLUE, socket, serverAddress, serverPort);
            gameStage.getRoot().getChildren().add(player.getCircle());

            ball = new Ball(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, BALL_RADIUS, Color.RED);
            gameStage.getRoot().getChildren().add(ball.getCircle());

            leftGoal = new Goal(0, (WINDOW_HEIGHT - GOAL_HEIGHT) / 2, GOAL_WIDTH, GOAL_HEIGHT, Color.GREEN, scoreBoard, true);
            rightGoal = new Goal(WINDOW_WIDTH - GOAL_WIDTH, (WINDOW_HEIGHT - GOAL_HEIGHT) / 2, GOAL_WIDTH, GOAL_HEIGHT, Color.GREEN, scoreBoard, false);

            gameStage.getRoot().getChildren().addAll(leftGoal.getRectangle(), rightGoal.getRectangle());

            gameStage.getRoot().getScene().setOnKeyPressed(event -> player.move(event.getCode()));

            gameStage.show();

            gameTimer = new GameTimer(player, ball, leftGoal, rightGoal);
            gameTimer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
