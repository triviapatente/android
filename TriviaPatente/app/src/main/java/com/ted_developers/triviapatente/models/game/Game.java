package com.ted_developers.triviapatente.models.game;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Game extends CommonPK {
    @SerializedName("creator_id")
    public Long creator_id;
    @SerializedName("winner_id")
    public Long winner_id;
    @SerializedName("ended")
    public Boolean ended;
    @SerializedName("opponent_username")
    public String opponent_username;
    @SerializedName("opponent_name")
    public String opponent_name;
    @SerializedName("opponent_surname")
    public String opponent_surname;
    @SerializedName("opponent_id")
    public Long opponent_id;
    @SerializedName("opponent_image")
    public String opponent_image;
    @SerializedName("my_turn")
    public Boolean my_turn;

    public Game() {}
}
