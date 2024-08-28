import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsertHighScoresDB {

    private Connection connection;
    private int hitCount;
    private int missCount;
    private int level;

    // Constructor to initialize hitCount, missCount, and level
    public InsertHighScoresDB(int hitCount, int missCount, int level) throws SQLException {
        this.hitCount = hitCount;
        this.missCount = missCount;
        this.level = level;
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hitme", "root", "kirankumar.9008");
        this.connection.setAutoCommit(false); // Start transaction management
    }

    // Method to check if the score is in the top 5
    public boolean checkIfTop5() throws SQLException {
        boolean isTop5 = false;

        // Determine table name based on level
        String tableName = switch (level) {
            case 1 -> "easy";
            case 2 -> "medium";
            default -> "difficult";
        };

        // Get the top scores
        List<ScoreEntry> topScores = getTopScores(tableName);

        // Check if the new score should be added
        if (topScores.size() < 5 || hitCount > topScores.get(topScores.size() - 1).hitCount ||
                (hitCount == topScores.get(topScores.size() - 1).hitCount && missCount < topScores.get(topScores.size() - 1).missCount)) {

            // Insert the score
            String truncatedName = NameFrame.globalName.length() > 20 ? NameFrame.globalName.substring(0, 20) : NameFrame.globalName;
            insertScore(tableName, truncatedName, hitCount, missCount);

            // Set isTop5 to true
            isTop5 = true;

            // Re-fetch top scores after insertion to check if we have more than 5 entries
            topScores = getTopScores(tableName);

            // Remove the lowest score if there are more than 5 scores
            if (topScores.size() > 5) {
                removeLowestScore(tableName);
            }

            connection.commit(); // Commit transaction
        }

        return isTop5; // Return whether the score is in the top 5
    }

    // Method to retrieve the top scores
    private List<ScoreEntry> getTopScores(String tableName) throws SQLException {
        String query = "SELECT name, hit_count, miss_count FROM " + tableName + " ORDER BY hit_count DESC, miss_count ASC";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        List<ScoreEntry> topScores = new ArrayList<>();
        while (resultSet.next()) {
            String playerName = resultSet.getString("name");
            int hitCount = resultSet.getInt("hit_count");
            int missCount = resultSet.getInt("miss_count");
            topScores.add(new ScoreEntry(playerName, hitCount, missCount));
        }

        resultSet.close();
        statement.close();

        return topScores;
    }

    // Method to insert a new score
    private void insertScore(String tableName, String playerName, int hitCount, int missCount) throws SQLException {
        String insertQuery = "INSERT INTO " + tableName + " (name, hit_count, miss_count) VALUES (?, ?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString(1, playerName);
        insertStatement.setInt(2, hitCount);
        insertStatement.setInt(3, missCount);
        insertStatement.executeUpdate();
        insertStatement.close();
    }

    // Method to remove the lowest score
    private void removeLowestScore(String tableName) throws SQLException {
        // Get the name of the player with the lowest score
        String selectQuery = "SELECT name FROM " + tableName + " ORDER BY hit_count ASC, miss_count DESC LIMIT 1";
        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
        ResultSet resultSet = selectStatement.executeQuery();

        if (resultSet.next()) {
            String nameToRemove = resultSet.getString("name");
            // Delete the lowest score
            String deleteQuery = "DELETE FROM " + tableName + " WHERE name = ? LIMIT 1";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, nameToRemove);
            deleteStatement.executeUpdate();
            deleteStatement.close();
        }

        resultSet.close();
        selectStatement.close();
    }

    // Helper class to store score entries
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

    // Close the connection when done
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
