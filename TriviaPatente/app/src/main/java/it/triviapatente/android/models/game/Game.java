package it.triviapatente.android.models.game;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Game extends CommonPK {
    @SerializedName("creator_id") public Long creator_id;
    @SerializedName("winner_id") public Long winner_id;
    @SerializedName("ended") public Boolean ended;
    @SerializedName("started") public Boolean started;
    @SerializedName("opponent_username") public String opponent_username;
    @SerializedName("opponent_name") public String opponent_name;
    @SerializedName("opponent_surname") public String opponent_surname;
    @SerializedName("opponent_id") public Long opponent_id;
    @SerializedName("opponent_image") public String opponent_image;
    @SerializedName("my_turn") public Boolean my_turn;
    @SerializedName("my_score") public Integer myScore;
    @SerializedName("opponent_score") public Integer opponentScore;
    @SerializedName("remaining_answers_count") public Integer remainingAnswersCount;
    @SerializedName("expired") public Boolean expired;

    public Game() {}
}
