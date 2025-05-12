import controller.LoginController;
import model.Database;
import view.LoginView;

public class Main {
    public static void main(String[] args) {
        // Initialiser la connexion à la base de données
        Database db = new Database();
        db.connect();
        
        // Créer et afficher la vue de login
        LoginView loginView = new LoginView();
        new LoginController(loginView, db);
        
        loginView.setVisible(true);
        
        // Ajouter un shutdown hook pour fermer la connexion à la base de données
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            db.close();
        }));
    }
}