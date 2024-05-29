package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class GameStage {
    private static final int WINDOW_WIDTH = 1000; // Width of the game window
    private static final int WINDOW_HEIGHT = 500; // Height of the game window
    private DatagramSocket socket; // Datagram socket for communication
    private InetAddress serverAddress; // Server IP address
    private int serverPort; // Server port number
    private String playerID; // Player ID
    private Pane root; // Root pane for the game stage
    private Stage stage; // Stage for the game window
    private VBox gameInterface; // VBox for game interface components
    private VBox chatBox; // VBox for chat interface components
    private TextArea chatArea; // TextArea for displaying chat messages

    // Constructor to initialize the game stage
    public GameStage(Stage primaryStage, InetAddress serverAddress, int serverPort, String playerID) throws SocketException {
        stage = primaryStage;
        root = new Pane();
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.playerID = playerID;
        this.socket = new DatagramSocket();

        // Load the background image
        Image backgroundImage = new Image(getClass().getResource("/images/background.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(background));

        // Set up the scene
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Goal Rivals");

        // Create VBox to hold game interface components
        gameInterface = new VBox();
        gameInterface.setPadding(new Insets(10));
        gameInterface.setSpacing(10);
        gameInterface.setAlignment(Pos.CENTER);

        // Create VBox for chat interface
        chatBox = new VBox();
        chatBox.setPadding(new Insets(5));
        chatBox.setSpacing(5);
        chatBox.setAlignment(Pos.BOTTOM_LEFT);
        chatBox.setVisible(false); // Initially hide the chat box

        // Text area to display chat messages
        this.chatArea = new TextArea();
        this.chatArea.setEditable(false); // Make the chat area read-only
        this.chatArea.setWrapText(true); // Wrap text in the chat area
        this.chatArea.setPrefHeight(100); // Set preferred height for the chat area

        // Text field for typing messages
        TextField messageField = new TextField();
        messageField.setPromptText("Type your message here"); // Set placeholder text
        messageField.setPrefWidth(200); // Set preferred width for the text field

        // Listen for Enter key press event on the message text field
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = messageField.getText().trim(); // Get the typed message
                if (!message.isEmpty()) {
                    this.chatArea.appendText("You: " + message + "\n"); // Append the message to the chat area
                    try {
                        this.sendMessage("Chat " + this.playerID + " " + message); // Send the message to the server
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    messageField.clear(); // Clear the text field
                    gameInterface.setVisible(false); // Hide the game interface
                    chatBox.setVisible(true); // Show the chat box
                }
            }
        });

        // Add components to the chat VBox
        chatBox.getChildren().addAll(chatArea, messageField);

        // Set the position of the chat box
        chatBox.setLayoutX(10);
        chatBox.setLayoutY(WINDOW_HEIGHT - 150);

        // Add the game interface and chat box to the root pane
        root.getChildren().addAll(gameInterface, chatBox);
    }

    // Method to show the game stage
    public void show() {
        stage.show();
    }

    // Getter for the root pane
    public Pane getRoot() {
        return root;
    }

    // Method to show the chat box
    public void showChatBox() {
        chatBox.setVisible(true);
    }

    // Method to hide the chat box
    public void hideChatBox() {
        chatBox.setVisible(false);
    }

    // Method to append a message to the chat area
    public void appendMessage(String msg) {
        this.chatArea.appendText(msg);
    }

    // Method to send a message to the server
    public void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes(); // Convert the message to bytes
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        socket.send(packet); // Send the packet
    }
}
