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
    public Boolean accepted;
    @SerializedName("receiver_id")
    public Long receiver_id;
    @SerializedName("sender_id")
    public Long sender_id;
    @SerializedName("game_id")
    public Long game_id;

    public Invite() {}
}
