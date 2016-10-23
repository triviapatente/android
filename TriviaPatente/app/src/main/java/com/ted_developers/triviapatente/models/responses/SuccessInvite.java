package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Invite;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class SuccessInvite extends Success {
    @SerializedName("invite")
    public Invite invite;

    public SuccessInvite() {}
}
