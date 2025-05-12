package model;

public class Queen extends Piece {
    public Queen(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isValidMove(int toRow, int toCol, Board board) {
        // Vérifier si le déplacement est dans les limites du plateau
        if (toRow < 0 || toRow >= Board.SIZE || toCol < 0 || toCol >= Board.SIZE) {
            return false;
        }

        // Vérifier si la case de destination est vide
        if (board.getPiece(toRow, toCol) != null) {
            return false;
        }

        // Vérifier que le déplacement est diagonal
        if (Math.abs(toRow - row) != Math.abs(toCol - col)) {
            return false;
        }

        // Vérifier qu'il n'y a pas de pièces sur le chemin
        int rowStep = (toRow > row) ? 1 : -1;
        int colStep = (toCol > col) ? 1 : -1;
        
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
        // Vérifier si le déplacement est dans les limites du plateau
        if (toRow < 0 || toRow >= Board.SIZE || toCol < 0 || toCol >= Board.SIZE) {
            return false;
        }

        // Vérifier si la case de destination est vide
        if (board.getPiece(toRow, toCol) != null) {
            return false;
        }

        // Vérifier que le déplacement est diagonal
        if (Math.abs(toRow - row) != Math.abs(toCol - col)) {
            return false;
        }

        int rowStep = (toRow > row) ? 1 : -1;
        int colStep = (toCol > col) ? 1 : -1;
        
        int currentRow = row + rowStep;
        int currentCol = col + colStep;
        
        Piece capturedPiece = null;
        
        while (currentRow != toRow && currentCol != toCol) {
            Piece piece = board.getPiece(currentRow, currentCol);
            
            if (piece != null) {
                // Si on trouve une deuxième pièce sur le chemin, le mouvement est invalide
                if (capturedPiece != null) {
                    return false;
                }
                
                // Vérifier que la pièce est adverse
                if (!piece.getColor().equals(color)) {
                    capturedPiece = piece;
                } else {
                    return false;
                }
            }
            
            currentRow += rowStep;
            currentCol += colStep;
        }

        return capturedPiece != null;
    }
}