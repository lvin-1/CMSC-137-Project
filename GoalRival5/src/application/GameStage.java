package application;

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
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 500;

    private Pane root;
    private Stage stage;
    private VBox gameInterface;
    private VBox chatBox;

    public GameStage(Stage primaryStage) {
        stage = primaryStage;
        root = new Pane();

        // Load the background image
        Image backgroundImage = new Image(getClass().getResource("/images/background.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(background));

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
        chatBox.setVisible(false);

        // Text area to display chat messages
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(100);

        // Text field for typing messages
        TextField messageField = new TextField();
        messageField.setPromptText("Type your message here");
        messageField.setPrefWidth(200); // Reduce the width of the text field

        // Listen for Enter key press event on the message text field
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    chatArea.appendText("You: " + message + "\n");
                    messageField.clear();
                    gameInterface.setVisible(false);
                    chatBox.setVisible(true);
                }
            }
        });

        // Add components to the chat VBox
        chatBox.getChildren().addAll(chatArea, messageField);

        chatBox.setLayoutX(10); 
        chatBox.setLayoutY(WINDOW_HEIGHT - 150); 

        root.getChildren().addAll(gameInterface, chatBox);
    }

    public void show() {
        stage.show();
    }

    public Pane getRoot() {
        return root;
    }

    public void showChatBox() {
        chatBox.setVisible(true);
    }

    public void hideChatBox() {
        chatBox.setVisible(false);
    }
}
