package model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import view.GameView;

public class Game {
    private final Board board;
    private String currentPlayer;
    private final String humanPlayerColor;
    private boolean gameOver;
    private String winner;
    private final String difficulty;
    private GameView view;
    private boolean isHumanTurn;

    public Game(String humanPlayerColor, String difficulty, boolean humanStarts) {
        this.board = new Board();
        this.humanPlayerColor = humanPlayerColor;
        this.currentPlayer = "white";
        this.gameOver = false;
        this.winner = null;
        this.difficulty = difficulty;
        this.isHumanTurn = humanStarts == (humanPlayerColor.equals("white"));
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (gameOver || !isHumanTurn) {
            return false;
        }

        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || !piece.getColor().equals(currentPlayer)) {
            return false;
        }

        boolean mandatoryCaptures = board.hasMandatoryCaptures(currentPlayer);
        
        if (piece.canCapture(toRow, toCol, board)) {
            if (mandatoryCaptures) {
                int middleRow = (fromRow + toRow) / 2;
                int middleCol = (fromCol + toCol) / 2;
                
                board.movePiece(fromRow, fromCol, toRow, toCol);
                board.capturePiece(middleRow, middleCol);
                
                // Vérifier si d'autres captures sont possibles avec la même pièce
                if (!piece.hasAvailableCaptures(board)) {
                    switchPlayer();
                }
                return true;
            }
            return false;
        } else if (!mandatoryCaptures && piece.isValidMove(toRow, toCol, board)) {
            board.movePiece(fromRow, fromCol, toRow, toCol);
            switchPlayer();
            return true;
        }
        
        return false;
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer.equals("white") ? "black" : "white";
        isHumanTurn = currentPlayer.equals(humanPlayerColor);
        checkGameOver();
        
        if (!isHumanTurn && !gameOver) {
            computerTurn();
        }
    }

    public void computerTurn() {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Pause to allow the human player to see the move
                
                switch (difficulty) {
                    case "easy":
                        makeRandomMove();
                        break;
                    case "medium":
                        makeMediumMove();
                        break;
                    case "hard":
                        makeHardMove();
                        break;
                    default:
                        makeRandomMove();
                }
                
                SwingUtilities.invokeLater(() -> {
                    view.drawBoard(board);
                    updateStatus();
                });
            } catch (InterruptedException e) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, e);
            }
        }).start();
    }

    private void makeRandomMove() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor().equals(currentPlayer)) {
                    int[][] directions = piece instanceof Queen ? 
                        new int[][]{{1,1}, {1,-1}, {-1,1}, {-1,-1}} :
                        currentPlayer.equals("white") ? 
                            new int[][]{{-1,-1}, {-1,1}} : 
                            new int[][]{{1,-1}, {1,1}};
                    
                    for (int[] dir : directions) {
                        int newRow = row + dir[0];
                        int newCol = col + dir[1];
                        
                        if (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                            if (makeComputerMove(row, col, newRow, newCol)) {
                                return;
                            }
                        }
                        
                        // Try captures
                        int captureRow = row + 2*dir[0];
                        int captureCol = col + 2*dir[1];
                        if (captureRow >= 0 && captureRow < Board.SIZE && captureCol >= 0 && captureCol < Board.SIZE) {
                            if (makeComputerMove(row, col, captureRow, captureCol)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
        switchPlayer(); // If no move is possible, skip the turn
    }

    private boolean makeComputerMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || !piece.getColor().equals(currentPlayer)) {
            return false;
        }

        if (piece.canCapture(toRow, toCol, board)) {
            int middleRow = (fromRow + toRow) / 2;
            int middleCol = (fromCol + toCol) / 2;
            
            board.movePiece(fromRow, fromCol, toRow, toCol);
            board.capturePiece(middleRow, middleCol);
            
            if (!piece.hasAvailableCaptures(board)) {
                switchPlayer();
            }
            return true;
        } else if (!board.hasMandatoryCaptures(currentPlayer) && piece.isValidMove(toRow, toCol, board)) {
            board.movePiece(fromRow, fromCol, toRow, toCol);
            switchPlayer();
            return true;
        }
        
        return false;
    }

    private void makeMediumMove() {
        // First, try to capture
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor().equals(currentPlayer)) {
                    int[][] directions = piece instanceof Queen ? 
                        new int[][]{{2,2}, {2,-2}, {-2,2}, {-2,-2}} :
                        currentPlayer.equals("white") ? 
                            new int[][]{{-2,-2}, {-2,2}} : 
                            new int[][]{{2,-2}, {2,2}};
                    
                    for (int[] dir : directions) {
                        int newRow = row + dir[0];
                        int newCol = col + dir[1];
                        
                        if (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                            if (makeComputerMove(row, col, newRow, newCol)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
        // If no capture is possible, make a random move
        makeRandomMove();
    }

    private void makeHardMove() {
        // First try to capture
        if (tryToCapture()) {
            return;
        }
        // Try to promote a pawn
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof Pawn && piece.getColor().equals(currentPlayer)) {
                    if (currentPlayer.equals("white")) {
                        if (makeComputerMove(row, col, row-1, col-1) || 
                            makeComputerMove(row, col, row-1, col+1)) {
                            return;
                        }
                    } else {
                        if (makeComputerMove(row, col, row+1, col-1) || 
                            makeComputerMove(row, col, row+1, col+1)) {
                            return;
                        }
                    }
                }
            }
        }
        
        // If nothing else, make a random move
        makeRandomMove();
    }

    private boolean tryToCapture() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor().equals(currentPlayer)) {
                    int[][] directions = piece instanceof Queen ? 
                        new int[][]{{2,2}, {2,-2}, {-2,2}, {-2,-2}} :
                        currentPlayer.equals("white") ? 
                            new int[][]{{-2,-2}, {-2,2}} : 
                            new int[][]{{2,-2}, {2,2}};
                    
                    for (int[] dir : directions) {
                        int newRow = row + dir[0];
                        int newCol = col + dir[1];
                        
                        if (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                            if (makeComputerMove(row, col, newRow, newCol)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void checkGameOver() {
        boolean whiteHasPieces = false;
        boolean blackHasPieces = false;
        boolean whiteCanMove = false;
        boolean blackCanMove = false;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    if (piece.getColor().equals("white")) {
                        whiteHasPieces = true;
                        if (piece.hasAvailableMoves(board)) {
                            whiteCanMove = true;
                        }
                    } else {
                        blackHasPieces = true;
                        if (piece.hasAvailableMoves(board)) {
                            blackCanMove = true;
                        }
                    }
                }
            }
        }

        if (!whiteHasPieces || (currentPlayer.equals("white") && !whiteCanMove)) {
            gameOver = true;
            winner = "black";
        } else if (!blackHasPieces || (currentPlayer.equals("black") && !blackCanMove)) {
            gameOver = true;
            winner = "white";
        }
    }

    public boolean isGameOver() { return gameOver; }
    public String getWinner() { return winner; }
    public String getCurrentPlayer() { return currentPlayer; }
    public Board getBoard() { return board; }
    public String getHumanPlayerColor() { return humanPlayerColor; }
    public boolean isHumanTurn() { return isHumanTurn; }

    private void updateStatus() {
        String status = "Tour: " + (currentPlayer.equals("white") ? "Blancs" : "Noirs");
        if (isHumanTurn) {
            status += " (Votre tour)";
        } else {
            status += " (Tour de l'ordinateur)";
        }
        view.setStatus(status);
    }

    public void endGame() {
        String winner = getWinner();
        if (winner.equals(humanPlayerColor)) {
            view.showMessage("Félicitations! Vous avez gagné!");
        } else {
            view.showMessage("L'ordinateur a gagné!");
        }
    }
}