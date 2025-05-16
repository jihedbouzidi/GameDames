package model;

public class Pawn extends Piece {
    public Pawn(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    protected int[][] getCaptureDirections() {
        // Peut capturer en avant et en arrière
        return new int[][]{{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
    }

    @Override
    protected int[][] getMoveDirections() {
        if (color.equals("white")) {
            return new int[][]{{-1, -1}, {-1, 1}};
        } else {
            return new int[][]{{1, -1}, {1, 1}};
        }
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
        int colDiff = Math.abs(toCol - col);

        if (colDiff != 1) {
            return false;
        }

        if (color.equals("white")) {
            return rowDiff == -1;
        } else {
            return rowDiff == 1;
        }
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

        if (Math.abs(rowDiff) != 2 || Math.abs(colDiff) != 2) {
            return false;
        }

        int middleRow = row + rowDiff / 2;
        int middleCol = col + colDiff / 2;
        
        Piece middlePiece = board.getPiece(middleRow, middleCol);
        
        return middlePiece != null && !middlePiece.getColor().equals(color);
    }
}