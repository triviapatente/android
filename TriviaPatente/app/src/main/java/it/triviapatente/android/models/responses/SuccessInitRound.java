package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Partecipation;
import it.triviapatente.android.models.game.Round;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 08/12/16.
 */
@Parcel
public class SuccessInitRound extends Success {
    @SerializedName("partecipations") public List<Partecipation> partecipations;
    @SerializedName("ended") public Boolean ended;
    @SerializedName("winner") public Long winner_id;
    @SerializedName("waiting") public String waiting;
    @SerializedName("round") public Round round;
    @SerializedName("opponent_online") public Boolean isOpponentOnline;
    @SerializedName("waiting_for") public User waiting_for;
    @SerializedName("category") public Category category;
    @SerializedName("max_age") public Long maxAge;
}
