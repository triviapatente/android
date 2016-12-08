package com.ted_developers.triviapatente.models.game;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Antonio on 08/12/16.
 */
@Parcel
public class Partecipation {
    @SerializedName("user_id") Long user_id;
    @SerializedName("game_id") Long game_id;
    @SerializedName("score_increment") Integer score_increment;
}
