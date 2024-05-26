package application;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class Player extends Sprite {
    private DatagramSocket socket; // Declare as class member
    private InetAddress serverAddress; // Declare as class member
    private int serverPort; // Declare as class member
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 500;
    private String playerID;

    public Player(double x, double y, double radius, Color color, String playerID, DatagramSocket socket, InetAddress serverAddress, int serverPort) {
        super(x, y, radius, color);
        this.socket = socket;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.playerID = playerID;
    }

    public void move(KeyCode code) {
        switch (code) {
            case W:
                if (this.getCircle().getCenterY() - radius - 10 >= 0) {
                    this.getCircle().setCenterY(this.getCircle().getCenterY() - 10);
                }
                break;
            case S:
                if (this.getCircle().getCenterY() + radius + 10 <= WINDOW_HEIGHT) {
                    this.getCircle().setCenterY(this.getCircle().getCenterY() + 10);
                }
                break;
            case A:
                if (this.getCircle().getCenterX() - radius - 10 >= 0) {
                    this.getCircle().setCenterX(this.getCircle().getCenterX() - 10);
                }
                break;
            case D:
                if (this.getCircle().getCenterX() + radius + 10 <= WINDOW_WIDTH) {
                    this.getCircle().setCenterX(this.getCircle().getCenterX() + 10);
                }
                break;
            case ENTER:
                try {
                    sendMessage("Player "+this.playerID +": Hello");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case U:
                try {
                    sendMessage("Player "+this.playerID +" Showed Emote 1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case I:
                try {
                    sendMessage("Player "+this.playerID +" Showed Emote 2");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case O:
                try {
                    sendMessage("Player "+this.playerID +" Showed Emote 3");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        String xpos = this.getCenterX()+"";
        String ypos = this.getCenterY()+"";
        try {
			this.sendMessage("Move " + xpos + " " + ypos + " " + this.playerID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    @Override
    public void checkCollision(Sprite other) {
        double dx = getCenterX() - other.getCenterX();
        double dy = getCenterY() - other.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < radius + ((Ball) other).getRadius()) {
            double directionX = dx / distance;
            double directionY = dy / distance;

            other.getCircle().setCenterX(other.getCenterX() - directionX * 20);
            other.getCircle().setCenterY(other.getCenterY() - directionY * 20);
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
