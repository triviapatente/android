package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.game.Game;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 13/11/16.
 */
@Parcel
public class SuccessGames extends Success {
    @SerializedName("recent_games")
    public List<Game> recent_games;
}
