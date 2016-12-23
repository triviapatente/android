package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Game;

/**
 * Created by Antonio on 11/12/16.
 */
public class Accepted extends Success{
    @SerializedName("accepted") public Boolean accepted;
}
