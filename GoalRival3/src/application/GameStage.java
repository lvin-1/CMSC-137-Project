package application;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameStage {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 500;

    private Pane root;
    private Stage stage;

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
    }

    public void show() {
        stage.show();
    }

    public Pane getRoot() {
        return root;
    }
}
