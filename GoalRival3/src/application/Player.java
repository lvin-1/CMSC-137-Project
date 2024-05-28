package application;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
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
    private String playerID;

    public Player(double x, double y, double radius, String playerID, DatagramSocket socket, InetAddress serverAddress, int serverPort) {
        super(x, y, radius, null);  // Pass null as color, we'll set image later
        this.socket = socket;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.playerID = playerID;

        String imagePath = "/images/player" + playerID + ".png";
        Image image = new Image(imagePath);
        circle.setFill(new ImagePattern(image));
    }

    public void move(KeyCode code) {
        double oldX = this.getCircle().getCenterX();
        double oldY = this.getCircle().getCenterY();

        switch (code) {
            case W:
                if (oldY - radius - 10 >= 0) {
                    this.getCircle().setCenterY(oldY - 10);
                }
                break;
            case S:
                if (oldY + radius + 10 <= WINDOW_HEIGHT) {
                    this.getCircle().setCenterY(oldY + 10);
                }
                break;
            case A:
                if (oldX - radius - 10 >= 0) {
                    this.getCircle().setCenterX(oldX - 10);
                }
                break;
            case D:
                if (oldX + radius + 10 <= WINDOW_WIDTH) {
                    this.getCircle().setCenterX(oldX + 10);
                }
                break;
            case ENTER:
                try {
                    sendMessage("Player " + this.playerID + ": Hello");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

//                ball.setVelocityX(directionX * 5);  // Adjust the multiplier as needed
//                ball.setVelocityY(directionY * 5);

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