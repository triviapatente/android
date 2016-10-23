package com.ted_developers.triviapatente.models.game;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.Base;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Invite extends Base {
    @SerializedName("accepted")
    public boolean accepted;
    @SerializedName("receiver_id")
    public long receiver_id;
    @SerializedName("sender_id")
    public long sender_id;
    @SerializedName("game_id")
    public long game_id;

    public Invite() {}
}
