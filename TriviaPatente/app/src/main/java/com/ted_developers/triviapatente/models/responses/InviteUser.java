package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Invite;

import org.parceler.Parcel;

/**
 * Created by Antonio on 11/12/16.
 */
@Parcel
public class InviteUser extends Success {
    @SerializedName("invite") public Invite invite;
    @SerializedName("user") public User user;

    public InviteUser() {}
}
