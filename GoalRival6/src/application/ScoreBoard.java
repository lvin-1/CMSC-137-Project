package application;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ScoreBoard {
    private static final int WINNING_SCORE = 1;
    private int leftScore;
    private int rightScore;
    private Text leftScoreText;
    private Text rightScoreText;
    private Text winnerText;

    public ScoreBoard(Pane root) {
        leftScore = 0;
        rightScore = 0;

        leftScoreText = new Text(150, 25, "Score: 0");
        leftScoreText.setFont(new Font(20));
        leftScoreText.setFill(Color.WHITE);

        rightScoreText = new Text(750, 25, "Score: 0");
        rightScoreText.setFont(new Font(20));
        rightScoreText.setFill(Color.WHITE);

        winnerText = new Text(350, 50, "");
        winnerText.setFont(new Font(40));
        winnerText.setFill(Color.BLACK);

        root.getChildren().addAll(leftScoreText, rightScoreText, winnerText);
    }

    public void incrementLeftScore() {
        if (leftScore < WINNING_SCORE && rightScore < WINNING_SCORE) {
            leftScore++;
            updateScores();
        }
    }

    public void incrementRightScore() {
        if (leftScore < WINNING_SCORE && rightScore < WINNING_SCORE) {
            rightScore++;
            updateScores();
        }
    }

    private void updateScores() {
        leftScoreText.setText("Left Team: " + leftScore);
        rightScoreText.setText("Right Team: " + rightScore);
        
        if (leftScore >= WINNING_SCORE) {
            winnerText.setText("Left Team Wins!");
        } else if (rightScore >= WINNING_SCORE) {
            winnerText.setText("Right Team Wins!");
        }
    }
    
    // Getter methods for scores
    public int getLeftScore() {
        return leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }
}
