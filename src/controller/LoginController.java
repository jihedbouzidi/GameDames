package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Database;
import model.Player;
import view.LoginView;
import view.MainMenuView;
import view.RegisterView;

public class LoginController {
    private final LoginView loginView;
    private RegisterView registerView;
    private final Database db;

    public LoginController(LoginView view, Database db) {
        this.loginView = view;
        this.db = db;
        
        loginView.addLoginListener(new LoginListener());
        loginView.addRegisterListener(new RegisterListener());
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            
            if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                loginView.showMessage("Veuillez remplir tous les champs");
                return;
            }
            
            Player player = db.authenticatePlayer(username, password);
            
            if (player != null) {
                loginView.setVisible(false);
                MainMenuView mainMenuView = new MainMenuView();
                new MainMenuController(mainMenuView, db, player);
                mainMenuView.setVisible(true);
            } else {
                loginView.showMessage("Nom d'utilisateur ou mot de passe incorrect");
            }
        }
    }

    class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Afficher la fenêtre d'inscription
            registerView = new RegisterView();
            new RegisterController(registerView, db);
            registerView.setVisible(true);
        }
    }
}