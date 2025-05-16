package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dames_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public void connect() {
        try {
            // Étape 1: Charger le pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Étape 2: Établir la connexion
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            
            // Étape 3: Vérifier et créer les tables si nécessaire
            createTablesIfNotExist();
            
            System.out.println("Connected to database");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    private void createTablesIfNotExist() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Table des joueurs
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                "username VARCHAR(50) PRIMARY KEY, " +
                "password VARCHAR(50) NOT NULL, " +
                "first_name VARCHAR(50) NOT NULL, " +
                "last_name VARCHAR(50) NOT NULL, " +
                "avatar VARCHAR(100) NOT NULL)");
            
            // Table des parties sauvegardées
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS saved_games (" +
                "game_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "player_id VARCHAR(50) NOT NULL, " +
                "player_pawn VARCHAR(10) NOT NULL, " +
                "board_state TEXT NOT NULL, " +
                "current_player VARCHAR(10) NOT NULL, " +
                "save_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (player_id) REFERENCES players(username) ON DELETE CASCADE)");
            
            // Table des statistiques
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS stats (" +
                "player_id VARCHAR(50) PRIMARY KEY, " +
                "games_played INT DEFAULT 0, " +
                "wins INT DEFAULT 0, " +
                "losses INT DEFAULT 0, " +
                "draws INT DEFAULT 0, " +
                "last_played TIMESTAMP, " +
                "FOREIGN KEY (player_id) REFERENCES players(username) ON DELETE CASCADE)");
            
            System.out.println("Tables created/verified");
        }
    }

    public Player authenticatePlayer(String username, String password) {
        String sql = "SELECT * FROM players WHERE username = ? AND password = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Player(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("avatar")
                );
            }
        } catch (SQLException e) {
            System.err.println("Authentication failed: " + e.getMessage());
        }
        
        return null;
    }

    public boolean registerPlayer(String username, String password, String firstName, String lastName, String avatar) {
        String sql = "INSERT INTO players (username, password, first_name, last_name, avatar) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, avatar);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Créer une entrée dans la table stats pour ce joueur
                String statsSql = "INSERT INTO stats (player_id) VALUES (?)";
                try (PreparedStatement statsStmt = connection.prepareStatement(statsSql)) {
                    statsStmt.setString(1, username);
                    statsStmt.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
        }
        
        return false;
    }

    public boolean updatePlayerProfile(String username, String newPassword, String newAvatar) {
        String sql = "UPDATE players SET password = ?, avatar = ? WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, newAvatar);
            pstmt.setString(3, username);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Profile update failed: " + e.getMessage());
        }
        
        return false;
    }

    public boolean saveGame(GameState gameState) {
        String sql = "INSERT INTO saved_games (player_id, player_pawn, board_state, current_player) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, gameState.getPlayerId());
            pstmt.setString(2, gameState.getPlayerPawn());
            
            // Convertir le boardState en JSON
            ObjectMapper mapper = new ObjectMapper();
            String boardStateJson = mapper.writeValueAsString(gameState.getBoardState());
            
            pstmt.setString(3, boardStateJson);
            pstmt.setString(4, gameState.getCurrentPlayer());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Game save failed: " + e.getMessage());
        }
        
        return false;
    }

    public List<GameState> loadSavedGames(String playerId) throws SQLException{
        List<GameState> games = new ArrayList<>();
        String sql = "SELECT * FROM saved_games WHERE player_id = ? ORDER BY save_date DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerId);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ObjectMapper mapper = new ObjectMapper();
                String[][] boardState = mapper.readValue(rs.getString("board_state"), String[][].class);
                
                games.add(new GameState(
                    rs.getInt("game_id"),
                    rs.getString("player_id"),
                    rs.getString("player_pawn"),
                    boardState,
                    rs.getString("current_player"),
                    rs.getTimestamp("save_date").toString()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Loading games failed: " + e.getMessage());
        } 
        
        return games;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}