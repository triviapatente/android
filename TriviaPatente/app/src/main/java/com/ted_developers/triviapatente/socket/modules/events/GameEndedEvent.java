package com.ted_developers.triviapatente.socket.modules.events;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Partecipation;
import com.ted_developers.triviapatente.models.responses.Success;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by donadev on 15/11/17.
 */
@Parcel
public class GameEndedEvent extends Success {
    @SerializedName("winner_id")
    Integer winnerId;

    @SerializedName("partecipations")
    List<Partecipation> partecipations;

    public GameEndedEvent() {}
}
