package model;

public class Queen extends Piece {
    public Queen(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    protected int[][] getCaptureDirections() {
        // La dame peut capturer dans les 4 directions
        return new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
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
        int captureRow = -1;
        int captureCol = -1;
        
        while (currentRow != toRow && currentCol != toCol) {
            Piece piece = board.getPiece(currentRow, currentCol);
            
            if (piece != null) {
                if (capturedPiece != null) {
                    return false; // Plus d'une pièce sur le chemin
                }
                
                if (piece.getColor().equals(color)) {
                    return false; // Pièce de la même couleur
                } else {
                    capturedPiece = piece;
                    captureRow = currentRow;
                    captureCol = currentCol;
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
            for (int distance = 1; distance < Board.SIZE; distance++) {
                int newRow = row + dir[0] * distance;
                int newCol = col + dir[1] * distance;
                
                if (newRow < 0 || newRow >= Board.SIZE || newCol < 0 || newCol >= Board.SIZE) {
                    break;
                }
                
                if (canCapture(newRow, newCol, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasAvailableMoves(Board board) {
        // Vérifie les mouvements simples
        for (int[] dir : getMoveDirections()) {
            for (int distance = 1; distance < Board.SIZE; distance++) {
                int newRow = row + dir[0] * distance;
                int newCol = col + dir[1] * distance;
                
                if (newRow < 0 || newRow >= Board.SIZE || newCol < 0 || newCol >= Board.SIZE) {
                    break;
                }
                
                if (isValidMove(newRow, newCol, board)) {
                    return true;
                }
            }
        }
        
        // Vérifie les captures
        return hasAvailableCaptures(board);
    }
}