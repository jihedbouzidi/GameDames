package model;

public class Queen extends Piece {
    public Queen(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    protected int[][] getCaptureDirections() {
        return new int[][]{{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
    }

    @Override
    protected int[][] getMoveDirections() {
        return new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    }

    @Override
    public boolean isValidMove(int toRow, int toCol, Board board) {
        if (toRow < 0 || toRow >= Board.SIZE || toCol < 0 || toCol >= Board.SIZE) {
            return false;
        }

        if (board.getPiece(toRow, toCol) != null) {
            return false;
        }

        int rowDiff = toRow - row;
        int colDiff = toCol - col;

        if (Math.abs(rowDiff) != Math.abs(colDiff)) {
            return false;
        }

        int rowStep = rowDiff > 0 ? 1 : -1;
        int colStep = colDiff > 0 ? 1 : -1;
        
        int currentRow = row + rowStep;
        int currentCol = col + colStep;
        
        while (currentRow != toRow && currentCol != toCol) {
            if (board.getPiece(currentRow, currentCol) != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return true;
    }

    @Override
    public boolean canCapture(int toRow, int toCol, Board board) {
        if (toRow < 0 || toRow >= Board.SIZE || toCol < 0 || toCol >= Board.SIZE) {
            return false;
        }

        if (board.getPiece(toRow, toCol) != null) {
            return false;
        }

        int rowDiff = toRow - row;
        int colDiff = toCol - col;

        if (Math.abs(rowDiff) != Math.abs(colDiff)) {
            return false;
        }

        int rowStep = rowDiff > 0 ? 1 : -1;
        int colStep = colDiff > 0 ? 1 : -1;
        
        int currentRow = row + rowStep;
        int currentCol = col + colStep;
        
        Piece capturedPiece = null;
        
        while (currentRow != toRow && currentCol != toCol) {
            Piece piece = board.getPiece(currentRow, currentCol);
            
            if (piece != null) {
                if (capturedPiece != null) {
                    return false; // Plus d'une pièce sur la diagonale
                }
                
                if (!piece.getColor().equals(color)) {
                    capturedPiece = piece;
                } else {
                    return false; // Pièce de même couleur
                }
            }
            
            currentRow += rowStep;
            currentCol += colStep;
        }

        return capturedPiece != null;
    }
}