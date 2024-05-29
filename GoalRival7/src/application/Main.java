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
    private static final double GOAL_HEIGHT = 135;
    private static final int SERVER_PORT = 5000;

    private Player[] players;
    private Ball ball;
    private GameStage gameStage;
    private GameTimer gameTimer;
    private Goal leftGoal;
    private Goal rightGoal;
    private ScoreBoard scoreBoard;
    private static String playerID;

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        try {
            // Retrieve command line arguments
            String[] args = getParameters().getRaw().toArray(new String[0]);
            String serverIP = args[1];
            playerID = getPlayerID(args);
            
            // Start the server only if this is the server instance (Player 1)
            if (playerID.equals("1")) {
                new Thread(() -> {
                    try {
                        GameServer server = new GameServer(SERVER_PORT);
                        server.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            
            // Setup server address
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            players = new Player[4];
            
            // Initialize the game stage
            gameStage = new GameStage(primaryStage, serverAddress, SERVER_PORT, this.playerID);
            
            // Create a client instance to communicate with the server
            GameClient client = new GameClient(playerID, players, serverAddress, SERVER_PORT, this.gameStage);
            client.receiveMessages();

            // Initialize the game components
            scoreBoard = new ScoreBoard(gameStage.getRoot());
            ball = new Ball(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, BALL_RADIUS, Color.RED);
            gameStage.getRoot().getChildren().add(ball.getImageView());

            leftGoal = new Goal(0, (WINDOW_HEIGHT - GOAL_HEIGHT) / 2, GOAL_WIDTH, GOAL_HEIGHT, Color.GREEN, scoreBoard, true);
            rightGoal = new Goal(WINDOW_WIDTH - GOAL_WIDTH, (WINDOW_HEIGHT - GOAL_HEIGHT) / 2, GOAL_WIDTH, GOAL_HEIGHT, Color.GREEN, scoreBoard, false);

            // Determining start positions and initializing players
            double startX;
            double startY;
            switch (playerID) {
                case "1":
                    startX = WINDOW_WIDTH / 4;
                    startY = WINDOW_HEIGHT / 2;
                    players[0] = new Player(startX, startY, PLAYER_RADIUS, playerID, client.getSocket(), serverAddress, SERVER_PORT, this.gameStage);
                    break;
                case "2":
                    startX = WINDOW_WIDTH / 11;
                    startY = WINDOW_HEIGHT / 2;
                    players[1] = new Player(startX, startY, PLAYER_RADIUS, playerID, client.getSocket(), serverAddress, SERVER_PORT, this.gameStage);
                    break;
                case "3":
                    startX = WINDOW_WIDTH - WINDOW_WIDTH / 4;
                    startY = WINDOW_HEIGHT / 2;
                    players[2] = new Player(startX, startY, PLAYER_RADIUS, playerID, client.getSocket(), serverAddress, SERVER_PORT, this.gameStage);
                    break;
                case "4":
                    startX = WINDOW_WIDTH - WINDOW_WIDTH / 11;
                    startY = WINDOW_HEIGHT / 2;
                    players[3] = new Player(startX, startY, PLAYER_RADIUS, playerID, client.getSocket(), serverAddress, SERVER_PORT, this.gameStage);
                    break;
                default:
                    throw new IllegalStateException("Unexpected playerID: " + playerID);
            }

            // Add current player to the game stage and setup key event handler for movement
            Player currentPlayer = players[Integer.parseInt(playerID) - 1];
            gameStage.getRoot().getChildren().add(currentPlayer.getCircle());
            gameStage.getRoot().getScene().setOnKeyPressed(event -> currentPlayer.move(event.getCode()));

            // Initialize and start the game timer
            gameTimer = new GameTimer(this.players, ball, leftGoal, rightGoal, scoreBoard);
            client.sendMessage("Connect " + currentPlayer.getCenterX() + " " + currentPlayer.getCenterY() + " " + playerID);

            // Add goals to the game stage
            gameStage.getRoot().getChildren().addAll(leftGoal.getRectangle(), rightGoal.getRectangle());
            gameStage.show();
            gameTimer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get the player ID from the command line arguments
    private String getPlayerID(String[] args) {
        if (args.length > 0) {
            return args[0];
        }
        throw new IllegalArgumentException("Player ID must be provided as an argument");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
