import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class FinalFrame implements ActionListener {

    JButton homeButton, scoreButton;
    JLabel gameover;
    static JFrame resultframe;
    int hitCount, missCount, level;
    InsertHighScoresDB highScoresColumns;

    public FinalFrame(int hitCount, int missCount, int level) {
        this.hitCount = hitCount;
        this.missCount = missCount;
        this.level = level;

        resultframe = new JFrame("RESULTS");
        resultframe.setLayout(null); // Use null layout for absolute positioning
        resultframe.setBounds(400, 160, 720, 500);
        resultframe.setSize(720, 500);

        double sum = hitCount + missCount;
        double div = (sum > 0) ? (double) hitCount / sum : 0; // Avoid division by zero
        double per = div * 100;

        JLabel label = new JLabel("<html>YOUR SCORE :<br>HIT - " + hitCount + "&nbsp;&nbsp;&nbsp; MISS - " + missCount + "&nbsp;&nbsp;&nbsp; Ratio - " + (int) per + " % </html>");
        label.setFont(new Font("Snap ITC", Font.BOLD, 25));
        label.setForeground(new Color(41, 92, 255));
        label.setBounds(50, 100, 1000, 100); // Set bounds for the label
        resultframe.add(label);

        // Center the gameover label horizontally
        gameover = new JLabel(" GAME OVER! ");
        gameover.setFont(new Font("Snap ITC", Font.BOLD, 40));
        gameover.setForeground(Color.magenta);
        gameover.setBounds(170, 30, 400, 50);
        resultframe.add(gameover);

        scoreButton = new JButton("Show Score");
        scoreButton.setFont(new Font("Snap ITC", Font.BOLD, 25));
        scoreButton.setBackground(new Color(40, 100, 250));
        scoreButton.setForeground(Color.BLACK);
        scoreButton.setBounds(90, 360, 220, 50); // Position button at bottom center
        scoreButton.addActionListener(this);
        resultframe.add(scoreButton);

        // Center the home button at the bottom
        homeButton = new JButton("HOME");
        homeButton.setFont(new Font("Snap ITC", Font.BOLD, 32));
        homeButton.setBackground(new Color(40, 100, 250));
        homeButton.setForeground(Color.BLACK);
        homeButton.setBounds(390, 360, 220, 50); // Position button at bottom center
        homeButton.addActionListener(this);
        resultframe.add(homeButton);

        // Process High Scores and check if the score is in the top 5
        boolean isTop5 = false;
        try {
            highScoresColumns = new InsertHighScoresDB(hitCount, missCount, level);
            isTop5 = highScoresColumns.checkIfTop5();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Display congratulations message if in top 5
        if (isTop5) {
            JLabel congratsLabel = new JLabel("<html><h1>Congratulations! New high score made<br>Player name: " + NameFrame.globalName + "</h1></html>");
            congratsLabel.setFont(new Font("Snap ITC", Font.BOLD, 35));
            congratsLabel.setForeground(new Color(255, 153, 0, 255));
            congratsLabel.setBounds(50, 214, 1000, 100);
            resultframe.add(congratsLabel);
        }

        resultframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultframe.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resultframe.dispose();
        if (e.getSource() == homeButton) {
            new HomeFrame();
        }
        if (e.getSource() == scoreButton) {
            new DisplayScoreFrame();
        }
    }

    public static void main(String[] args) {
        new FinalFrame(0, 0, 0);
    }
}
