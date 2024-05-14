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

    public Player(double x, double y, double radius, Color color, DatagramSocket socket, InetAddress serverAddress, int serverPort) {
        super(x, y, radius, color);
        this.socket = socket;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void move(KeyCode code) {
        switch (code) {
            case W:
                this.getCircle().setCenterY(this.getCircle().getCenterY() - 10);
                break;
            case S:
            	this.getCircle().setCenterY(this.getCircle().getCenterY() + 10);
                break;
            case A:
            	this.getCircle().setCenterX(circle.getCenterX() - 10);
                break;
            case D:
            	this.getCircle().setCenterX(circle.getCenterX() + 10);
                break;
            case ENTER:
            	 try {
	                   sendMessage("Hello from player");
	               } catch (IOException e) {
	                   e.printStackTrace();
	               }
            	 break;
            case U:
           	 try {
	                   sendMessage("Show Emote 1");
	               } catch (IOException e) {
	                   e.printStackTrace();
	               }
           	 break;
            case I:
              	 try {
   	                   sendMessage("Show Emote 2");
   	               } catch (IOException e) {
   	                   e.printStackTrace();
   	               }
              	 break;
            case O:
              	 try {
   	                   sendMessage("Show Emote 3");
   	               } catch (IOException e) {
   	                   e.printStackTrace();
   	               }
              	 break;
            	 
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
    
//    public void handleKeyPress(KeyCode code) {
//        if (code == KeyCode.ENTER) {
//            try {
//                sendMessage("Hello from player");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}