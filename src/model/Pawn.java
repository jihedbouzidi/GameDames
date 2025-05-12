package model;

public class Pawn extends Piece {
    public Pawn(String color, int row, int col) {
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

        int rowDiff = toRow - row;
        int colDiff = Math.abs(toCol - col);

        // Le déplacement doit être d'une case en diagonale
        if (colDiff != 1 || Math.abs(rowDiff) != 1) {
            return false;
        }

        // Pour un pion blanc (qui se déplace vers le haut)
        if (color.equals("white")) {
            return (rowDiff == -1);
        } 
        // Pour un pion noir (qui se déplace vers le bas)
        else {
            return (rowDiff == 1);
        }
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

        int rowDiff = toRow - row;
        int colDiff = Math.abs(toCol - col);

        // Pour une capture, le déplacement doit être de 2 cases en diagonale
        if (Math.abs(rowDiff) == 2 && colDiff == 2) {
            int middleRow = row + rowDiff / 2;
            int middleCol = col + (toCol - col) / 2;
            
            Piece middlePiece = board.getPiece(middleRow, middleCol);
            
            // Vérifier qu'il y a une pièce adverse au milieu
            return middlePiece != null && !middlePiece.getColor().equals(color);
        }

        return false;
    }
}