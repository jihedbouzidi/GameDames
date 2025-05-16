package model;

public class Queen extends Piece {
    public Queen(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    protected int[][] getCaptureDirections() {
        // All diagonal directions for captures (forward and backward)
        return new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    }

    @Override
    protected int[][] getMoveDirections() {
        // All diagonal directions for moves
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
                    return false; // More than one piece in the path
                }
                if (!piece.getColor().equals(color)) {
                    capturedPiece = piece;
                } else {
                    return false; // Own piece in the path
                }
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return capturedPiece != null;
    }

    @Override
    public boolean hasAvailableCaptures(Board board) {
        for (int[] dir : getCaptureDirections()) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            while (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                if (canCapture(newRow, newCol, board)) {
                    return true;
                }
                newRow += dir[0];
                newCol += dir[1];
            }
        }
        return false;
    }
}