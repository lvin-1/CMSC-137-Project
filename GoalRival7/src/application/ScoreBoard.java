package application;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ScoreBoard {
    private static final int WINNING_SCORE = 5;  // The score required to win the game
    private int leftScore;  // Score for the left team
    private int rightScore; // Score for the right team
    private Text leftScoreText;  // Text element to display the left team's score
    private Text rightScoreText; // Text element to display the right team's score
    private Text winnerText;     // Text element to display the winning team

    // Constructor to initialize the scoreboard and add it to the root pane
    public ScoreBoard(Pane root) {
        leftScore = 0;
        rightScore = 0;

        // Initialize the text elements for displaying scores
        leftScoreText = new Text(150, 25, "Score: 0");
        leftScoreText.setFont(new Font(20));
        leftScoreText.setFill(Color.WHITE);

        rightScoreText = new Text(750, 25, "Score: 0");
        rightScoreText.setFont(new Font(20));
        rightScoreText.setFill(Color.WHITE);

        // Initialize the text element for displaying the winner
        winnerText = new Text(350, 50, "");
        winnerText.setFont(new Font(40));
        winnerText.setFill(Color.BLACK);

        // Add the text elements to the root pane
        root.getChildren().addAll(leftScoreText, rightScoreText, winnerText);
    }

    // Method to increment the left team's score
    public void incrementLeftScore() {
        // Increment score only if neither team has won yet
        if (leftScore < WINNING_SCORE && rightScore < WINNING_SCORE) {
            leftScore++;
            updateScores();
        }
    }

    // Method to increment the right team's score
    public void incrementRightScore() {
        // Increment score only if neither team has won yet
        if (leftScore < WINNING_SCORE && rightScore < WINNING_SCORE) {
            rightScore++;
            updateScores();
        }
    }

    // Method to update the score display and check for a winner
    private void updateScores() {
        // Update the score text elements
        leftScoreText.setText("Left Team: " + leftScore);
        rightScoreText.setText("Right Team: " + rightScore);
        
        // Check if either team has reached the winning score
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
