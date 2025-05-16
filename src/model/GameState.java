package model;
import java.io.IOException;

public class GameState {
    private final int gameId;
    private final String playerId;
    private final String playerPawn;
    private final String[][] boardState;
    private final String currentPlayer;
    private final String saveDate;

    public GameState(int gameId, String playerId, String playerPawn, String[][] boardState, String currentPlayer, String saveDate) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerPawn = playerPawn;
        this.boardState = boardState;
        this.currentPlayer = currentPlayer;
        this.saveDate = saveDate;
    }

    // Getters
    public int getGameId() { return gameId; }
    public String getPlayerId() { return playerId; }
    public String getPlayerPawn() { return playerPawn; }
    public String[][] getBoardState() { return boardState; }
    public String getCurrentPlayer() { return currentPlayer; }
    public String getSaveDate() { return saveDate; }

    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static GameState fromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, GameState.class);
    }
}