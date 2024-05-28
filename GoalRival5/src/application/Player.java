package application;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Rotate;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class Player extends Sprite {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 500;
    private static final int GOAL_WIDTH = 200;  // Define the width of the goal area
    private String playerID;
    private GameStage gameStage;
    private double direction;

    public Player(double x, double y, double radius, String playerID, DatagramSocket socket, InetAddress serverAddress, int serverPort) {
        super(x, y, radius, null);  // Pass null as color, we'll set image later
        this.socket = socket;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.playerID = playerID;
        this.gameStage = gameStage;
        this.direction = 0;

        String imagePath = "/images/player" + playerID + ".png";
        Image image = new Image(imagePath);
        circle.setFill(new ImagePattern(image));
    }

    public void move(KeyCode code) {
        double oldX = this.getCircle().getCenterX();
        double oldY = this.getCircle().getCenterY();

        switch (code) {
            case W:
                if (oldY - radius - 10 >= 0 && canMoveTo(oldX, oldY - 10)) {
                    this.getCircle().setCenterY(oldY - 10);
                    this.direction = 270; // Up
                }
                break;
            case S:
                if (oldY + radius + 10 <= WINDOW_HEIGHT && canMoveTo(oldX, oldY + 10)) {
                    this.getCircle().setCenterY(oldY + 10);
                    this.direction = 90; // Down
                }
                break;
            case A:
                if (oldX - radius - 10 >= 0 && canMoveTo(oldX - 10, oldY)) {
                    this.getCircle().setCenterX(oldX - 10);
                    this.direction = 180; // Left
                }
                break;
            case D:
                if (oldX + radius + 10 <= WINDOW_WIDTH && canMoveTo(oldX + 10, oldY)) {
                    this.getCircle().setCenterX(oldX + 10);
                    this.direction = 0; // Right
                }
                break;
            case ENTER:
                gameStage.showChatBox();
                break;
            case ESCAPE:
                gameStage.hideChatBox();
                break;
            case U:
                try {
                    sendMessage("Player " + this.playerID + " Showed Emote 1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case I:
                try {
                    sendMessage("Player " + this.playerID + " Showed Emote 2");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case O:
                try {
                    sendMessage("Player " + this.playerID + " Showed Emote 3");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        // Apply the rotation with adjustment based on playerID
        double adjustedDirection = getAdjustedDirection(this.playerID, this.direction);
        this.getCircle().getTransforms().clear();
        this.getCircle().getTransforms().add(new Rotate(adjustedDirection, this.getCircle().getCenterX(), this.getCircle().getCenterY()));

        if (oldX != this.getCircle().getCenterX() || oldY != this.getCircle().getCenterY()) {
            String xpos = this.getCenterX() + "";
            String ypos = this.getCenterY() + "";
            try {
                this.sendMessage("Move " + xpos + " " + ypos + " " + this.playerID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


 // Method to check if the player can move to the specified coordinates
    private boolean canMoveTo(double x, double y) {
        if ("2".equals(playerID)) {
            // Left goal area
        	return x >= 0 && x <= WINDOW_WIDTH/11 && y >= 100 && y <= WINDOW_HEIGHT - 100;
        } else if ("4".equals(playerID)) {
            // Right goal area
            return x >= WINDOW_WIDTH-WINDOW_WIDTH/11 && x <= WINDOW_WIDTH && y >= 100 && y <= WINDOW_HEIGHT - 100;
        } else {
            // Other players can move within the entire window
            return x >= 0 && x <= WINDOW_WIDTH && y >= 0 && y <= WINDOW_HEIGHT;
        }
    }


    // Method to adjust the direction based on player ID
    private double getAdjustedDirection(String playerID, double direction) {
        double adjustment = 0;
        switch (playerID) {
            case "3":
                adjustment = -180;  // adjustment for player3.png
                break;
            case "4":
                adjustment = +180; // adjustment for player4.png
                break;
        }
        return direction + adjustment;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    @Override
    public void checkCollision(Sprite other) {
        if (other instanceof Ball) {
            Ball ball = (Ball) other;
            double dx = ball.getCenterX() - this.getCenterX();
            double dy = ball.getCenterY() - this.getCenterY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < radius + ball.getRadius()) {
                double overlap = radius + ball.getRadius() - distance;
                double directionX = dx / distance;
                double directionY = dy / distance;

                ball.getImageView().setX(ball.getImageView().getX() + directionX * overlap);
                ball.getImageView().setY(ball.getImageView().getY() + directionY * overlap);
            }
        }
    }

    @Override
    public double getRadius() {
        return radius;
    }

    public void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        socket.send(packet);
    }

    public String getPlayerID() {
        return this.playerID;
    }
}
