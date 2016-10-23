package com.ted_developers.triviapatente.models.preferences;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Preferences extends CommonPK {
    @SerializedName("stats")
    public String stats; // TODO make enum
    @SerializedName("chat")
    public String chat; // TODO make enum
    @SerializedName("user_id")
    public long user_id;
    @SerializedName("notification_message")
    public boolean notification_message;
    @SerializedName("notification_round")
    public boolean notification_round;
    @SerializedName("notification_new_game")
    public boolean notification_new_game;
    @SerializedName("notification_full_hearts")
    public boolean notification_full_hearts;

    public Preferences() {}
}
