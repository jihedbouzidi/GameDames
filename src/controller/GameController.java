package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.Database;
import model.Game;
import model.Player;
import view.GameView;

public class GameController {
    private GameView view;
    private Database db;
    private Player player;
    private Game game;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public GameController(GameView view, Database db, Player player, Game game) {
        this.view = view;
        this.db = db;
        this.player = player;
        this.game = game;
        
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

                // Vérifier que les coordonnées sont valides
                if (row < 0 || row >= model.Board.SIZE || col < 0 || col >= model.Board.SIZE) {
                    return;
                }

                if (selectedRow == -1 && selectedCol == -1) {
                    // Sélection d'une pièce
                    if (isPlayerPiece(row, col)) {
                        selectedRow = row;
                        selectedCol = col;
                        view.setSelected(row, col);
                        view.drawBoard(game.getBoard());
                    }
                } else {
                    // Tentative de déplacement
                    if (game.makeMove(selectedRow, selectedCol, row, col)) {
                        view.resetSelection();
                        selectedRow = -1;
                        selectedCol = -1;
                        view.drawBoard(game.getBoard());
                        updateStatus();

                        if (game.isGameOver()) {
                            endGame();
                        } else if (!game.isHumanTurn()) {
                            computerTurn();
                        }
                    } else {
                        // Si le déplacement échoue, soit sélectionner une autre pièce, soit annuler
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

    private void computerTurn() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                boolean moveMade = false;
                for (int row = 0; row < model.Board.SIZE && !moveMade; row++) {
                    for (int col = 0; col < model.Board.SIZE && !moveMade; col++) {
                        if (game.getBoard().getPiece(row, col) != null && 
                            game.getBoard().getPiece(row, col).getColor().equals(game.getCurrentPlayer())) {
                            
                            int[][] directions = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
                            for (int[] dir : directions) {
                                int newRow = row + dir[0];
                                int newCol = col + dir[1];
                                
                                if (newRow >= 0 && newRow < model.Board.SIZE && 
                                    newCol >= 0 && newCol < model.Board.SIZE) {
                                    if (game.makeMove(row, col, newRow, newCol)) {
                                        moveMade = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (!moveMade) {
                    game.switchPlayer();
                }
                
                SwingUtilities.invokeLater(() -> {
                    view.drawBoard(game.getBoard());
                    updateStatus();
                    
                    if (game.isGameOver()) {
                        endGame();
                    } else if (!game.isHumanTurn()) {
                        computerTurn();
                    }
                });
                
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void endGame() {
        String winner = game.getWinner().equals("white") ? "Blancs" : "Noirs";
        if (game.getWinner().equals(game.getHumanPlayerColor())) {
            JOptionPane.showMessageDialog(view, "Félicitations! Vous avez gagné!", "Partie terminée", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "Le joueur " + winner + " a gagné!", "Partie terminée", JOptionPane.INFORMATION_MESSAGE);
        }
        
        view.resetSelection();
    }
}