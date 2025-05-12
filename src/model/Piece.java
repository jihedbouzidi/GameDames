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
}