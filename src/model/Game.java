package model;

import java.util.ArrayList;
import java.util.List;
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

    public Game(String humanPlayerColor, String difficulty) {
        this.board = new Board();
        this.humanPlayerColor = humanPlayerColor;
        this.gameOver = false;
        this.winner = null;
        this.difficulty = difficulty;
        
        // White always starts
        this.currentPlayer = "white";
        this.isHumanTurn = humanPlayerColor.equals("white");
        
        board.initializeBoard(humanPlayerColor);
    }

    public void setView(GameView view) {
        this.view = view;
        updateStatus();
        if (!isHumanTurn) {
            computerTurn();
        }
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
        
        if (mandatoryCaptures && piece.canCapture(toRow, toCol, board)) {
            // Handle capture
            if (piece instanceof Queen) {
                // Queen capture: find the captured piece
                int[] capturedPos = findCapturedPieceForQueen(fromRow, fromCol, toRow, toCol);
                if (capturedPos != null) {
                    board.movePiece(fromRow, fromCol, toRow, toCol);
                    board.capturePiece(capturedPos[0], capturedPos[1]);
                } else {
                    return false;
                }
            } else {
                // Pawn capture
                int middleRow = (fromRow + toRow) / 2;
                int middleCol = (fromCol + toCol) / 2;
                board.movePiece(fromRow, fromCol, toRow, toCol);
                board.capturePiece(middleRow, middleCol);
            }
            
            // Check if more captures are possible with this piece
            if (!piece.hasAvailableCaptures(board)) {
                switchPlayer();
            }
            return true;
        } else if (!mandatoryCaptures && piece.isValidMove(toRow, toCol, board)) {
            board.movePiece(fromRow, fromCol, toRow, toCol);
            switchPlayer();
            return true;
        }
        
        return false;
    }

    private int[] findCapturedPieceForQueen(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;
        if (Math.abs(rowDiff) != Math.abs(colDiff)) {
            return null;
        }

        int rowStep = rowDiff > 0 ? 1 : -1;
        int colStep = colDiff > 0 ? 1 : -1;
        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;

        while (currentRow != toRow && currentCol != toCol) {
            Piece piece = board.getPiece(currentRow, currentCol);
            if (piece != null && !piece.getColor().equals(currentPlayer)) {
                return new int[]{currentRow, currentCol};
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return null;
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
                Thread.sleep(1000);
                
                // Find all possible captures
                List<Move> captureMoves = findAllCaptures();
                if (!captureMoves.isEmpty()) {
                    // Execute a capture sequence
                    executeCaptureSequence();
                } else {
                    // No captures, make a normal move
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
                }
                
                SwingUtilities.invokeLater(() -> {
                    view.drawBoard(board);
                    updateStatus();
                    if (gameOver) {
                        endGame();
                    }
                });
            } catch (InterruptedException e) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, e);
            }
        }).start();
    }

    private void executeCaptureSequence() {
        List<Move> sequence = findCaptureSequence();
        if (sequence.isEmpty()) {
            switchPlayer();
            return;
        }

        for (Move move : sequence) {
            Piece piece = board.getPiece(move.getFromRow(), move.getFromCol());
            if (piece instanceof Queen) {
                int[] capturedPos = findCapturedPieceForQueen(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());
                if (capturedPos != null) {
                    board.movePiece(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());
                    board.capturePiece(capturedPos[0], capturedPos[1]);
                }
            } else {
                int middleRow = (move.getFromRow() + move.getToRow()) / 2;
                int middleCol = (move.getFromCol() + move.getToCol()) / 2;
                board.movePiece(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());
                board.capturePiece(middleRow, middleCol);
            }

            // Update board visually and wait 1 second
            SwingUtilities.invokeLater(() -> view.drawBoard(board));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        switchPlayer();
    }

    private List<Move> findCaptureSequence() {
        List<Move> bestSequence = new ArrayList<>();
        String[][] originalState = board.getBoardState();

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor().equals(currentPlayer)) {
                    List<Move> currentSequence = new ArrayList<>();
                    findCaptureSequenceRecursive(piece, row, col, currentSequence, bestSequence);
                    board.setBoardState(originalState); // Reset board after each piece
                }
            }
        }
        return bestSequence;
    }

    private void findCaptureSequenceRecursive(Piece piece, int row, int col, List<Move> currentSequence, List<Move> bestSequence) {
        int[][] directions = piece.getCaptureDirections();
        boolean foundCapture = false;
        String[][] originalState = board.getBoardState();

        for (int[] dir : directions) {
            if (piece instanceof Queen) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                while (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                    if (piece.canCapture(newRow, newCol, board)) {
                        Move move = new Move(row, col, newRow, newCol);
                        currentSequence.add(move);

                        // Simulate the move
                        int[] capturedPos = findCapturedPieceForQueen(row, col, newRow, newCol);
                        board.movePiece(row, col, newRow, newCol);
                        board.capturePiece(capturedPos[0], capturedPos[1]);

                        // Check for promotion
                        Piece movedPiece = board.getPiece(newRow, newCol);
                        if (movedPiece instanceof Pawn && 
                            ((movedPiece.getColor().equals("white") && newRow == 0) ||
                             (movedPiece.getColor().equals("black") && newRow == Board.SIZE - 1))) {
                            movedPiece = new Queen(movedPiece.getColor(), newRow, newCol);
                            board.movePiece(newRow, newCol, newRow, newCol); // Update piece
                        }

                        // Recurse for further captures
                        findCaptureSequenceRecursive(movedPiece, newRow, newCol, currentSequence, bestSequence);

                        // Undo the move
                        board.setBoardState(originalState);
                        currentSequence.remove(currentSequence.size() - 1);
                        foundCapture = true;
                    }
                    newRow += dir[0];
                    newCol += dir[1];
                }
            } else {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                    if (piece.canCapture(newRow, newCol, board)) {
                        Move move = new Move(row, col, newRow, newCol);
                        currentSequence.add(move);

                        // Simulate the move
                        int middleRow = (row + newRow) / 2;
                        int middleCol = (col + newCol) / 2;
                        board.movePiece(row, col, newRow, newCol);
                        board.capturePiece(middleRow, middleCol);

                        // Check for promotion
                        Piece movedPiece = board.getPiece(newRow, newCol);
                        if (movedPiece instanceof Pawn && 
                            ((movedPiece.getColor().equals("white") && newRow == 0) ||
                             (movedPiece.getColor().equals("black") && newRow == Board.SIZE - 1))) {
                            movedPiece = new Queen(movedPiece.getColor(), newRow, newCol);
                            board.movePiece(newRow, newCol, newRow, newCol); // Update piece
                        }

                        // Recurse for further captures
                        findCaptureSequenceRecursive(movedPiece, newRow, newCol, currentSequence, bestSequence);

                        // Undo the move
                        board.setBoardState(originalState);
                        currentSequence.remove(currentSequence.size() - 1);
                        foundCapture = true;
                    }
                }
            }
        }

        if (!foundCapture && currentSequence.size() > bestSequence.size()) {
            bestSequence.clear();
            bestSequence.addAll(currentSequence);
        }
    }

    private List<Move> findAllCaptures() {
        List<Move> captures = new ArrayList<>();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor().equals(currentPlayer)) {
                    int[][] directions = piece.getCaptureDirections();
                    for (int[] dir : directions) {
                        if (piece instanceof Queen) {
                            int newRow = row + dir[0];
                            int newCol = col + dir[1];
                            while (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                                if (piece.canCapture(newRow, newCol, board)) {
                                    captures.add(new Move(row, col, newRow, newCol));
                                }
                                newRow += dir[0];
                                newCol += dir[1];
                            }
                        } else {
                            int newRow = row + dir[0];
                            int newCol = col + dir[1];
                            if (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                                if (piece.canCapture(newRow, newCol, board)) {
                                    captures.add(new Move(row, col, newRow, newCol));
                                }
                            }
                        }
                    }
                }
            }
        }
        return captures;
    }

    private void makeRandomMove() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor().equals(currentPlayer)) {
                    int[][] directions = piece.getMoveDirections();
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
        switchPlayer();
    }

    private boolean makeComputerMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || !piece.getColor().equals(currentPlayer)) {
            return false;
        }

        if (!board.hasMandatoryCaptures(currentPlayer) && piece.isValidMove(toRow, toCol, board)) {
            board.movePiece(fromRow, fromCol, toRow, toCol);
            switchPlayer();
            return true;
        }
        return false;
    }

    private void makeMediumMove() {
        // Try to promote a pawn
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof Pawn && piece.getColor().equals(currentPlayer)) {
                    if (currentPlayer.equals("white") && row == 1) {
                        if (makeComputerMove(row, col, row-1, col-1) || 
                            makeComputerMove(row, col, row-1, col+1)) {
                            return;
                        }
                    } else if (currentPlayer.equals("black") && row == Board.SIZE-2) {
                        if (makeComputerMove(row, col, row+1, col-1) || 
                            makeComputerMove(row, col, row+1, col+1)) {
                            return;
                        }
                    }
                }
            }
        }
        makeRandomMove();
    }

    private void makeHardMove() {
        makeMediumMove();
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

        if (!whiteHasPieces || !whiteCanMove) {
            gameOver = true;
            winner = "black";
        } else if (!blackHasPieces || !blackCanMove) {
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
            view.showMessage("L'ordinateur a gagné! Vous avez perdu!");
        }
        gameOver = true;
    }
}