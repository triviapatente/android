package it.triviapatente.android.app.utils;

import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Antonio on 11/12/16.
 */
public class ReceivedData {
    // rank
    public static Integer global_rank_position = new Integer(0), friends_rank_position = new Integer(0);

    // stats
    public static List<Category> statsHints = new ArrayList<>(0);

    // recent games
    public static List<Game> recentGames = new ArrayList<>(0);
    public static void updateGame(Game newGame) {
        for(int i = 0; i < recentGames.size(); i++) {
            if(recentGames.get(i).id.equals(newGame.id)) {
                recentGames.set(i, newGame);
            }
        }
        orderGames();
    }
    public static void addGame(Game newGame) {
        recentGames.add(newGame);
        orderGames();
    }
    public static void orderGames() {
        Collections.sort(recentGames);
    }
}
