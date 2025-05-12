package model;

public class Board {
    public static final int SIZE = 10;
    private Piece[][] grid;

    public Board() {
        grid = new Piece[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        // Placement des pions noirs
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    grid[row][col] = new Pawn("black", row, col);
                }
            }
        }

        // Placement des pions blancs
        for (int row = 6; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    grid[row][col] = new Pawn("white", row, col);
                }
            }
        }
    }

    public Piece getPiece(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            return grid[row][col];
        }
        return null;
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = grid[fromRow][fromCol];
        if (piece == null) return;
        
        grid[fromRow][fromCol] = null;
        grid[toRow][toCol] = piece;
        piece.setPosition(toRow, toCol);

        // Promotion en dame si le pion atteint la dernière rangée
        if (piece instanceof Pawn) {
            if ((piece.getColor().equals("white") && toRow == 0) || 
                (piece.getColor().equals("black") && toRow == SIZE - 1)) {
                grid[toRow][toCol] = new Queen(piece.getColor(), toRow, toCol);
            }
        }
    }

    public void capturePiece(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            grid[row][col] = null;
        }
    }

    public boolean hasMandatoryCaptures(String color) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Piece piece = grid[row][col];
                if (piece != null && piece.getColor().equals(color)) {
                    // Vérifier les 4 directions possibles pour une capture
                    int[][] directions = {{2,2}, {2,-2}, {-2,2}, {-2,-2}};
                    for (int[] dir : directions) {
                        int newRow = row + dir[0];
                        int newCol = col + dir[1];
                        if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
                            if (piece.canCapture(newRow, newCol, this)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public String[][] getBoardState() {
        String[][] state = new String[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Piece piece = grid[row][col];
                if (piece == null) {
                    state[row][col] = "empty";
                } else if (piece instanceof Pawn) {
                    state[row][col] = piece.getColor();
                } else if (piece instanceof Queen) {
                    state[row][col] = piece.getColor() + "_d";
                }
            }
        }
        return state;
    }

    public void setBoardState(String[][] state) {
        grid = new Piece[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String pieceStr = state[row][col];
                if (pieceStr.equals("white")) {
                    grid[row][col] = new Pawn("white", row, col);
                } else if (pieceStr.equals("black")) {
                    grid[row][col] = new Pawn("black", row, col);
                } else if (pieceStr.equals("white_d")) {
                    grid[row][col] = new Queen("white", row, col);
                } else if (pieceStr.equals("black_d")) {
                    grid[row][col] = new Queen("black", row, col);
                }
            }
        }
    }
}