package application;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameStage {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 500;

    private Pane root;
    private Stage stage;

    public GameStage(Stage primaryStage) {
        stage = primaryStage;
        root = new Pane();
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