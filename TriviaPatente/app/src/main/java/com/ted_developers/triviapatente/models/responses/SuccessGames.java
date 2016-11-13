package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Game;

import java.util.List;

/**
 * Created by Antonio on 13/11/16.
 */
public class SuccessGames extends Success {
    @SerializedName("recent_games")
    public List<Game> recent_games;
}
