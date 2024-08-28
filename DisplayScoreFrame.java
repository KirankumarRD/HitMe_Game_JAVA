import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayScoreFrame {

    private static final Color COLUMN1_COLOR = new Color(255, 200, 200); // Light red
    private static final Color COLUMN2_COLOR = new Color(200, 255, 200); // Light green
    private static final Color COLUMN3_COLOR = new Color(200, 200, 255); // Light blue
    private static final Color BUTTON_COLOR = new Color(40, 100, 250); // Color for Home and Reset buttons
    private static final Font FONT_SNAP_ITC_LARGE = new Font("Snap ITC", Font.BOLD, 24); // Large Snap ITC font
    private static final Font FONT_SNAP_ITC_SMALL = new Font("Snap ITC", Font.PLAIN, 14); // Small Snap ITC font

    public DisplayScoreFrame() {
        JFrame frame = new JFrame("Top Scores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(400, 160, 720, 500);
        frame.setLayout(new BorderLayout());

        // Header label with larger font
        JLabel headerLabel = new JLabel("Top 5 Best SCORES in each level", SwingConstants.CENTER);
        headerLabel.setFont(FONT_SNAP_ITC_LARGE);
        frame.add(headerLabel, BorderLayout.NORTH);

        // Create panels for each column with equal size
        JPanel easyPanel = createScorePanel("Easy Level", COLUMN1_COLOR);
        JPanel mediumPanel = createScorePanel("Medium Level", COLUMN2_COLOR);
        JPanel difficultPanel = createScorePanel("Difficult Level", COLUMN3_COLOR);

        // Create a container panel to manage the layout of score panels
        JPanel centerPanel = new JPanel(new GridLayout(1, 3));
        centerPanel.add(easyPanel);
        centerPanel.add(mediumPanel);
        centerPanel.add(difficultPanel);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Add Home and Reset buttons
        JPanel buttonPanel = new JPanel();
        JButton homeButton = new JButton("Home");
        homeButton.setFont(FONT_SNAP_ITC_LARGE);
        homeButton.setBackground(BUTTON_COLOR);
        homeButton.setForeground(Color.BLACK);
        homeButton.addActionListener(e -> {
            frame.dispose(); // Close the current frame
            new HomeFrame(); // Create a new instance of HomeFrame
        });

        JButton resetButton = new JButton("Reset");
        resetButton.setFont(FONT_SNAP_ITC_LARGE);
        resetButton.setBackground(BUTTON_COLOR);
        resetButton.setForeground(Color.BLACK);
        resetButton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete all scores?",
                    "Confirm Reset",
                    JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                resetDatabase();
                // Refresh panels after reset
                frame.dispose();
                new DisplayScoreFrame(); // Consider handling this to avoid potential issues
            }
        });

        buttonPanel.add(homeButton);
        buttonPanel.add(resetButton);
        buttonPanel.setPreferredSize(new Dimension(frame.getWidth(), 62)); // Set the height of the button panel

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set the same height for the header panel as the button panel
        headerLabel.setPreferredSize(new Dimension(frame.getWidth(), 58));
        headerLabel.setVerticalAlignment(SwingConstants.CENTER);

        frame.setVisible(true);
    }

    private JPanel createScorePanel(String title, Color backgroundColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.setPreferredSize(new Dimension(240, 400)); // Set preferred size for layout management

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SNAP_ITC_LARGE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        List<ScoreEntry> topScores = getTopScores(title);
        if (topScores.isEmpty()) {
            JLabel noScoreLabel = new JLabel("No scores available");
            noScoreLabel.setFont(FONT_SNAP_ITC_SMALL);
            noScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(noScoreLabel);
        } else {
            for (ScoreEntry entry : topScores) {
                JPanel scorePanel = new JPanel();
                scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
                scorePanel.setBackground(backgroundColor);
                scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add gap

                JLabel nameLabel = new JLabel("Player: " + entry.playerName);
                JLabel hitAndMissLabel = new JLabel("Hits: " + entry.hitCount + "   Misses: " + entry.missCount);

                nameLabel.setFont(FONT_SNAP_ITC_SMALL);
                hitAndMissLabel.setFont(FONT_SNAP_ITC_SMALL);

                scorePanel.add(nameLabel);
                scorePanel.add(hitAndMissLabel);

                scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the scorePanel

                panel.add(scorePanel);
            }
        }

        return panel;
    }

    private List<ScoreEntry> getTopScores(String level) {
        List<ScoreEntry> topScores = new ArrayList<>();
        String tableName = switch (level) {
            case "Easy Level" -> "easy";
            case "Medium Level" -> "medium";
            case "Difficult Level" -> "difficult";
            default -> throw new IllegalArgumentException("Invalid level: " + level);
        };

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hitme", "root", "kirankumar.9008");
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT name, hit_count, miss_count FROM " + tableName +
                             " ORDER BY hit_count DESC, miss_count ASC LIMIT 5");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String playerName = resultSet.getString("name");
                int hitCount = resultSet.getInt("hit_count");
                int missCount = resultSet.getInt("miss_count");
                topScores.add(new ScoreEntry(playerName, hitCount, missCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topScores;
    }

    private void resetDatabase() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hitme", "root", "kirankumar.9008");
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM easy");
            statement.executeUpdate("DELETE FROM medium");
            statement.executeUpdate("DELETE FROM difficult");

            JOptionPane.showMessageDialog(null, "All scores have been deleted.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error resetting database.");
        }
    }

    private static class ScoreEntry {
        String playerName;
        int hitCount;
        int missCount;

        ScoreEntry(String playerName, int hitCount, int missCount) {
            this.playerName = playerName;
            this.hitCount = hitCount;
            this.missCount = missCount;
        }
    }

    public static void main(String[] args) {
        new DisplayScoreFrame();
    }
}
