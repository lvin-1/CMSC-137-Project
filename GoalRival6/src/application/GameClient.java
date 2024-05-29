package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class GameClient {
    private static final double PLAYER_RADIUS = 30;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private Player[] players;
    private GameStage stage;
    private boolean connectionConfirmed;
    private String playerID;

    public GameClient(String playerID, Player[] initialPlayers, InetAddress address, int port, GameStage gameStage) throws IOException {
        this.playerID = playerID;
        socket = new DatagramSocket();
        serverAddress = address;
        serverPort = port;
        players = initialPlayers;
        stage = gameStage;
        connectionConfirmed = false;
    }

    public void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        socket.send(packet);
    }

    public void receiveMessages() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) {
                try {
                    socket.receive(packet);
                    String receivedMessage = new String(packet.getData(), 0, packet.getLength());
//                    String[] messageParts = receivedMessage.split(" ", 4);
                    if (receivedMessage.equals("AlreadyConnected")) {
                        this.connectionConfirmed = true;
                    }
                    checkConnectionMessage(receivedMessage);
                    System.out.println("Server:");
                    System.out.println(receivedMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    private void checkConnectionMessage(String msg) {
    	String[] arr = msg.split(" ",4);
        if (arr[0].equals("Connect")) {
            String receivedPlayerID = arr[3];
            if (!receivedPlayerID.equals(this.playerID)) {
                spawnPlayer(receivedPlayerID, Double.parseDouble(arr[1]), Double.parseDouble(arr[2]));
            }
        }else if(arr[0].equals("Move")) {
        	String receivedPlayerID = arr[3];
        	movePlayer(receivedPlayerID,Double.parseDouble(arr[1]), Double.parseDouble(arr[2]));
        }else if(arr[0].equals("Chat")) {
        	String[] toSend = msg.split(" ",3);
        	String receivedPlayerID = arr[1];
        	if(!receivedPlayerID.equals(this.playerID)){
        		this.stage.appendMessage("Player " + receivedPlayerID + ": "+toSend[2]+"\n");
        	}
        }
    }

    private void spawnPlayer(String playerID, double x, double y) {
        Platform.runLater(() -> {
            int playerIndex = Integer.parseInt(playerID) - 1;
            if (players[playerIndex] == null) {
                Color color = Color.BLUE; // Default color, you can change this as needed
                switch (playerID) {
                    case "1":
                        color = Color.BLUE;
                        break;
                    case "2":
                        color = Color.GREEN;
                        break;
                    case "3":
                        color = Color.YELLOW;
                        break;
                    case "4":
                        color = Color.ORANGE;
                        break;
                }
                players[playerIndex] = new Player(x, y, PLAYER_RADIUS, playerID, socket, serverAddress, serverPort, this.stage);
                stage.getRoot().getChildren().add(players[playerIndex].getCircle());
                sendExistingPlayers(playerID);
            }
        });
    }

    private void movePlayer(String playerID, double x, double y) {
    	 Platform.runLater(() -> {
             int playerIndex = Integer.parseInt(playerID) - 1;
             this.players[playerIndex].getCircle().setCenterX(x);
             this.players[playerIndex].getCircle().setCenterY(y);
         });
    }

    public void sendExistingPlayers(String connectedPlayerID) {
        for (Player player : players) {
            if (player != null && !connectedPlayerID.equals(player.getPlayerID())) {
                String message = "Connect " + player.getCenterX() + " " + player.getCenterY() + " " + player.getPlayerID();
                try {
                    sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isConnectionConfirmed() {
        return this.connectionConfirmed;
    }

    public void sendMovementUpdate(String playerID, double x, double y) {
        String message = "Move " + x + " " + y + " " + playerID;
        try {
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void receivedChat() {
    	this.stage.showChatBox();
    }
}