package model;

public class Game {
    private Board board;
    private String currentPlayer;
    private String humanPlayerColor;
    private boolean gameOver;
    private String winner;

    public Game(String humanPlayerColor) {
        this.board = new Board();
        this.humanPlayerColor = humanPlayerColor;
        this.currentPlayer = "white"; // Les blancs commencent toujours
        this.gameOver = false;
        this.winner = null;
    }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPiece(fromRow, fromCol);
        
        // Vérifications de base
        if (gameOver || piece == null || !piece.getColor().equals(currentPlayer)) {
            return false;
        }

        // Vérifier s'il y a des captures obligatoires
        boolean mandatoryCaptures = board.hasMandatoryCaptures(currentPlayer);
        
        if (piece.canCapture(toRow, toCol, board)) {
            // Effectuer la capture
            int middleRow = (fromRow + toRow) / 2;
            int middleCol = (fromCol + toCol) / 2;
            
            board.movePiece(fromRow, fromCol, toRow, toCol);
            board.capturePiece(middleRow, middleCol);
            
            // Vérifier si d'autres captures sont possibles avec la même pièce
            if (!piece.canCapture(toRow + 2, toCol + 2, board) &&
                !piece.canCapture(toRow + 2, toCol - 2, board) &&
                !piece.canCapture(toRow - 2, toCol + 2, board) &&
                !piece.canCapture(toRow - 2, toCol - 2, board)) {
                switchPlayer();
            }
            
            return true;
        } else if (!mandatoryCaptures && piece.isValidMove(toRow, toCol, board)) {
            // Déplacement simple
            board.movePiece(fromRow, fromCol, toRow, toCol);
            switchPlayer();
            return true;
        }
        
        return false;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer.equals("white") ? "black" : "white";
        checkGameOver();
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
                        // Vérifier si la pièce peut bouger
                        for (int r = 0; r < Board.SIZE; r++) {
                            for (int c = 0; c < Board.SIZE; c++) {
                                if (piece.isValidMove(r, c, board) || piece.canCapture(r, c, board)) {
                                    whiteCanMove = true;
                                    break;
                                }
                            }
                            if (whiteCanMove) break;
                        }
                    } else {
                        blackHasPieces = true;
                        // Vérifier si la pièce peut bouger
                        for (int r = 0; r < Board.SIZE; r++) {
                            for (int c = 0; c < Board.SIZE; c++) {
                                if (piece.isValidMove(r, c, board) || piece.canCapture(r, c, board)) {
                                    blackCanMove = true;
                                    break;
                                }
                            }
                            if (blackCanMove) break;
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

    public boolean isHumanTurn() {
        return currentPlayer.equals(humanPlayerColor);
    }
}