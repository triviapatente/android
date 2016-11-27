package com.ted_developers.triviapatente.models.game;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.Base;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Invite extends Base {
    @SerializedName("sender_id")
    public Integer sender_id;
    @SerializedName("game_id")
    public Integer game_id;
    @SerializedName("sender_name")
    public String sender_name;
    @SerializedName("sender_surname")
    public String sender_surname;
    @SerializedName("sender_username")
    public String sender_username;
    @SerializedName("sender_image")
    public String sender_image;

    public Invite() {}
}
