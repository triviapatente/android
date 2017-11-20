package com.ted_developers.triviapatente.socket.modules.events;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.responses.SuccessRoundDetails;

import org.parceler.Parcel;

/**
 * Created by donadev on 15/11/17.
 */
@Parcel
public class RoundStartedEvent extends SuccessRoundDetails {
    @SerializedName("category")
    Category category;

    public RoundStartedEvent() {}
}
