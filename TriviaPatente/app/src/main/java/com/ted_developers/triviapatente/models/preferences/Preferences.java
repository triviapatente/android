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
    public Long user_id;
    @SerializedName("notification_message")
    public Boolean notification_message;
    @SerializedName("notification_round")
    public Boolean notification_round;
    @SerializedName("notification_new_game")
    public Boolean notification_new_game;
    @SerializedName("notification_full_hearts")
    public Boolean notification_full_hearts;

    public Preferences() {}
}
