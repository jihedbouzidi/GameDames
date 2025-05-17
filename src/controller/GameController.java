package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import model.Database;
import model.Game;
import model.Piece;
import model.Player;
import view.GameView;

public class GameController {
    private final GameView view;
    private final Database db;
    private final Player player;
    private final Game game;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public GameController(GameView view, Database db, Player player, Game game) {
        this.view = view;
        this.db = db;
        this.player = player;
        this.game = game;
        game.setView(view);
        
        initView();
        initListeners();
    }

    private void initView() {
        view.drawBoard(game.getBoard());
        updateStatus();
    }

    private void initListeners() {
        view.addBoardClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!game.isHumanTurn() || game.isGameOver()) return;

                int row = e.getY() / view.getCellSize();
                int col = e.getX() / view.getCellSize();

                if (row < 0 || row >= model.Board.SIZE || col < 0 || col >= model.Board.SIZE) {
                    return;
                }

                if (selectedRow == -1 && selectedCol == -1) {
                    if (isPlayerPiece(row, col)) {
                        selectedRow = row;
                        selectedCol = col;
                        view.setSelected(row, col);
                        view.drawBoard(game.getBoard());
                    }
                } else {
                    if (game.makeMove(selectedRow, selectedCol, row, col)) {
                        // Vérifier si le même pion peut encore capturer
                        Piece movedPiece = game.getBoard().getPiece(row, col);
                        if (movedPiece != null && movedPiece.hasAvailableCaptures(game.getBoard())) {
                            selectedRow = row;
                            selectedCol = col;
                            view.setSelected(row, col);
                        } else {
                            view.resetSelection();
                            selectedRow = -1;
                            selectedCol = -1;
                        }
                        view.drawBoard(game.getBoard());
                        updateStatus();

                        if (game.isGameOver()) {
                            game.endGame();
                        }
                    } else {
                        if (isPlayerPiece(row, col)) {
                            selectedRow = row;
                            selectedCol = col;
                            view.setSelected(row, col);
                        } else {
                            view.resetSelection();
                            selectedRow = -1;
                            selectedCol = -1;
                        }
                        view.drawBoard(game.getBoard());
                    }
                }
            }
        });

        view.addSaveListener(e -> {
            JOptionPane.showMessageDialog(view, "Fonctionnalité de sauvegarde à implémenter");
        });

        view.addQuitListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                view, 
                "Voulez-vous vraiment quitter la partie?", 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                view.dispose();
            }
        });
    }

    private boolean isPlayerPiece(int row, int col) {
        return game.getBoard().getPiece(row, col) != null && 
               game.getBoard().getPiece(row, col).getColor().equals(game.getHumanPlayerColor());
    }

    private void updateStatus() {
        String status = "Tour: " + (game.getCurrentPlayer().equals("white") ? "Blancs" : "Noirs");
        if (game.isHumanTurn()) {
            status += " (Votre tour)";
        } else {
            status += " (Tour de l'ordinateur)";
        }
        view.setStatus(status);
    }

    public Database getDb() {
        return db;
    }

    public Player getPlayer() {
        return player;
    }
}