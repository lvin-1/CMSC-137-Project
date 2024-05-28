package application;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class ScoreBoard {
    private int leftScore;
    private int rightScore;
    private Text leftScoreText;
    private Text rightScoreText;

    public ScoreBoard(Pane root) {
        leftScore = 0;
        rightScore = 0;

        leftScoreText = new Text(20, 20, "Score: 0");
        leftScoreText.setFont(new Font(20));
        leftScoreText.setFill(Color.WHITE);

        rightScoreText = new Text(800, 20, "Score: 0");
        rightScoreText.setFont(new Font(20));
        rightScoreText.setFill(Color.WHITE);

        root.getChildren().addAll(leftScoreText, rightScoreText);
    }

    public void incrementLeftScore() {
        leftScore++;
        updateScores();
    }

    public void incrementRightScore() {
        rightScore++;
        updateScores();
    }

    private void updateScores() {
        leftScoreText.setText("Left Team: " + leftScore);
        rightScoreText.setText("Right Team: " + rightScore);
    }
}