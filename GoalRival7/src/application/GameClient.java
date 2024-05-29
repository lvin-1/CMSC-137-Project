package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class GameClient {
    private static final double PLAYER_RADIUS = 30;
    private DatagramSocket socket; // Datagram socket for sending and receiving packets
    private InetAddress serverAddress; // Address of the game server
    private int serverPort; // Port number of the game server
    private Player[] players; // Array to store players
    private GameStage stage; // GameStage instance to manage game UI
    private boolean connectionConfirmed; // Flag to check if the connection is confirmed
    private String playerID; // ID of the player

    // Constructor to initialize the GameClient
    public GameClient(String playerID, Player[] initialPlayers, InetAddress address, int port, GameStage gameStage) throws IOException {
        this.playerID = playerID;
        socket = new DatagramSocket(); // Create a new datagram socket
        serverAddress = address;
        serverPort = port;
        players = initialPlayers;
        stage = gameStage;
        connectionConfirmed = false; // Initially, the connection is not confirmed
    }

    // Method to send a message to the server
    public void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        socket.send(packet); // Send the packet to the server
    }

    // Method to receive messages from the server
    public void receiveMessages() {
        new Thread(() -> {
            byte[] buffer = new byte[1024]; // Buffer to store received data
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) {
                try {
                    socket.receive(packet); // Receive a packet from the server
                    String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                    if (receivedMessage.equals("AlreadyConnected")) {
                        this.connectionConfirmed = true; // Set the connection confirmed flag
                    }
                    checkConnectionMessage(receivedMessage); // Process the received message
                    System.out.println("Server:");
                    System.out.println(receivedMessage); // Print the received message
                } catch (IOException e) {
                    e.printStackTrace(); // Print the stack trace if an error occurs
                }
            }
        }).start(); // Start the thread
    }

    // Getter for the socket
    public DatagramSocket getSocket() {
        return socket;
    }

    // Method to check and process connection-related messages
    private void checkConnectionMessage(String msg) {
        String[] arr = msg.split(" ", 4);
        if (arr[0].equals("Connect")) {
            String receivedPlayerID = arr[3];
            if (!receivedPlayerID.equals(this.playerID)) {
                spawnPlayer(receivedPlayerID, Double.parseDouble(arr[1]), Double.parseDouble(arr[2])); // Spawn a new player
            }
        } else if (arr[0].equals("Move")) {
            String receivedPlayerID = arr[3];
            movePlayer(receivedPlayerID, Double.parseDouble(arr[1]), Double.parseDouble(arr[2])); // Move the player
        } else if (arr[0].equals("Chat")) {
            String[] toSend = msg.split(" ", 3);
            String receivedPlayerID = arr[1];
            if (!receivedPlayerID.equals(this.playerID)) {
                this.stage.appendMessage("Player " + receivedPlayerID + ": " + toSend[2] + "\n"); // Append chat message
            }
        }
    }

    // Method to spawn a new player
    private void spawnPlayer(String playerID, double x, double y) {
        Platform.runLater(() -> {
            int playerIndex = Integer.parseInt(playerID) - 1;
            if (players[playerIndex] == null) {
           
                players[playerIndex] = new Player(x, y, PLAYER_RADIUS, playerID, socket, serverAddress, serverPort, this.stage); // Create a new player
                stage.getRoot().getChildren().add(players[playerIndex].getCircle()); // Add player to the game stage
                sendExistingPlayers(playerID); // Send existing players to the new player
            }
        });
    }

    // Method to move a player to a new position
    private void movePlayer(String playerID, double x, double y) {
        Platform.runLater(() -> {
            int playerIndex = Integer.parseInt(playerID) - 1;
            this.players[playerIndex].getCircle().setCenterX(x); // Set the X position of the player
            this.players[playerIndex].getCircle().setCenterY(y); // Set the Y position of the player
        });
    }

    // Method to send information about existing players to a new player
    public void sendExistingPlayers(String connectedPlayerID) {
        for (Player player : players) {
            if (player != null && !connectedPlayerID.equals(player.getPlayerID())) {
                String message = "Connect " + player.getCenterX() + " " + player.getCenterY() + " " + player.getPlayerID();
                try {
                    sendMessage(message); // Send the player information to the new player
                } catch (IOException e) {
                    e.printStackTrace(); // Print the stack trace if an error occurs
                }
            }
        }
    }

    // Getter for the connection confirmed flag
    public boolean isConnectionConfirmed() {
        return this.connectionConfirmed;
    }

    // Method to send a movement update to the server
    public void sendMovementUpdate(String playerID, double x, double y) {
        String message = "Move " + x + " " + y + " " + playerID;
        try {
            sendMessage(message); // Send the movement update to the server
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if an error occurs
        }
    }
    
    // Method to show the chat box
    public void receivedChat() {
        this.stage.showChatBox();
    }
}
