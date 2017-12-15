package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Game;

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
