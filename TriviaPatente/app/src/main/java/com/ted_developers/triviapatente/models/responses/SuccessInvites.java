package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Invite;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class SuccessInvites extends Success {
    @SerializedName("invites")
    public List<Invite> invites;

    public SuccessInvites() {}
}
