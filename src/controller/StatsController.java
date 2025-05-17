package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Database;
import model.Player;
import view.StatsView;

public class StatsController {
    private final StatsView view;

    private final Database db;
    private final Player player;

    public StatsController(StatsView view, Database db, Player player) {
        this.view = view;
        this.db = db;
        this.player = player;
        
        // TODO: Charger les statistiques du joueur depuis la base de données
        view.setPlayerName(player.getUsername());
        view.setGamesPlayed(0);
        view.setWins(0);
        view.setLosses(0);
        view.setDraws(0);
        
        view.addCloseListener(new CloseListener());
    }

    public Player getPlayer() {
        return player;
    }

    public Database getDb() {
        return db;
    }

    class CloseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }
}