package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.Database;
import model.Game;
import model.Player;
import view.GameView;
import view.MainMenuView;
import view.ProfileView;
import view.StatsView;

public class MainMenuController {
    private MainMenuView view;
    private Database db;
    private Player player;

    public MainMenuController(MainMenuView view, Database db, Player player) {
        this.view = view;
        this.db = db;
        this.player = player;
        
        if (view == null || db == null || player == null) {
            throw new IllegalArgumentException("Les arguments ne peuvent pas être null");
        }
        
        initListeners();
    }

    private void initListeners() {
        view.addNewGameListener(new NewGameListener());
        view.addLoadGameListener(new LoadGameListener());
        view.addStatsListener(new StatsListener());
        view.addProfileListener(new ProfileListener());
        view.addQuitListener(new QuitListener());
    }

    class NewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Demander la couleur des pions
                Object[] options = {"Blancs", "Noirs"};
                int choice = JOptionPane.showOptionDialog(
                    view, 
                    "Choisissez la couleur de vos pions", 
                    "Nouvelle Partie", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    options, 
                    options[0]
                );
                
                if (choice == JOptionPane.CLOSED_OPTION) {
                    return;
                }
                
                String playerColor = (choice == 0) ? "white" : "black";
                
                // Demander qui commence
                Object[] startOptions = {"Moi", "L'ordinateur"};
                int startChoice = JOptionPane.showOptionDialog(
                    view, 
                    "Qui doit commencer?", 
                    "Nouvelle Partie", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    startOptions, 
                    startOptions[0]
                );
                
                if (startChoice == JOptionPane.CLOSED_OPTION) {
                    return;
                }
                
                // Créer la partie et la vue
                Game game = new Game(playerColor);
                GameView gameView = new GameView();
                
                // Créer le contrôleur de jeu
                new GameController(gameView, db, player, game);
                
                // Afficher la vue du jeu
                gameView.setVisible(true);
                
                // Fermer le menu principal
                view.setVisible(false);
                view.dispose();
                
                if (startChoice == 1) {
                    // TODO: Implémenter l'IA pour le premier coup
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    view, 
                    "Erreur lors de la création de la partie: " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        }
    }

    class LoadGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                view.showMessage("Fonctionnalité à implémenter");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    view, 
                    "Erreur lors du chargement: " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    class StatsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                StatsView statsView = new StatsView();
                new StatsController(statsView, db, player);
                statsView.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    view, 
                    "Erreur lors de l'affichage des statistiques: " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    class ProfileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ProfileView profileView = new ProfileView();
                new ProfileController(profileView, db, player);
                profileView.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    view, 
                    "Erreur lors de l'ouverture du profil: " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    class QuitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                db.close();
                System.exit(0);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    view, 
                    "Erreur lors de la fermeture: " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        }
    }
}