package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Game;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class SuccessGameUser extends Success {
    @SerializedName("game")
    public Game game;
    @SerializedName("user")
    public User user;

    public SuccessGameUser() {}
}
