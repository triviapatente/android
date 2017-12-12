package com.ted_developers.triviapatente.socket.modules.events;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.responses.Success;

import org.parceler.Parcel;

/**
 * Created by donadev on 15/11/17.
 */
@Parcel
public class GameLeftEvent extends GameEndedEvent {

    @SerializedName("user")
    public User user;
    @SerializedName("annulled")
    public Boolean annulled;

    public GameLeftEvent() {}
}
