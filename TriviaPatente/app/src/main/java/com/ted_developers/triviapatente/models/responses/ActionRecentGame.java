package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Game;

import org.parceler.Parcel;

/**
 * Created by Antonio on 24/01/17.
 */
@Parcel
public class ActionRecentGame extends Action {
    @SerializedName("game") public Game game;
}
