package com.ted_developers.triviapatente.app.utils;

import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 11/12/16.
 */
public class ReceivedData {
    // rank
    public static Integer global_rank_position = new Integer(0), friends_rank_position = new Integer(0);
    // stats
    public static Category[] statsHints = null;
    // recent games
    public static List<Game> recentGames = null;
}
