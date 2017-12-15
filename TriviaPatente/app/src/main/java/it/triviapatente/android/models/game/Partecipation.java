package it.triviapatente.android.models.game;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Antonio on 08/12/16.
 */
@Parcel
public class Partecipation {
    @SerializedName("user_id") public Long user_id;
    @SerializedName("game_id") public Long game_id;
    @SerializedName("score_increment") public Integer score_increment;

    Partecipation() {}
}
