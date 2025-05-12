package model;

public abstract class Piece {
    protected String color;
    protected int row;
    protected int col;

    public Piece(String color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
    }

    public String getColor() { return color; }
    public int getRow() { return row; }
    public int getCol() { return col; }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract boolean isValidMove(int toRow, int toCol, Board board);
    public abstract boolean canCapture(int toRow, int toCol, Board board);
    
    public boolean hasAvailableCaptures(Board board) {
        int[][] captureDirections = getCaptureDirections();
        for (int[] dir : captureDirections) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                if (canCapture(newRow, newCol, board)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean hasAvailableMoves(Board board) {
        // Vérifie les mouvements simples
        int[][] moveDirections = getMoveDirections();
        for (int[] dir : moveDirections) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < Board.SIZE && newCol >= 0 && newCol < Board.SIZE) {
                if (isValidMove(newRow, newCol, board)) {
                    return true;
                }
            }
        }
        
        // Vérifie les captures
        return hasAvailableCaptures(board);
    }
    
    protected abstract int[][] getCaptureDirections();
    protected abstract int[][] getMoveDirections();
}