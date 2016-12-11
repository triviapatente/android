package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Partecipation;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 11/12/16.
 */
@Parcel
public class WinnerPartecipationsUserleft {
    @SerializedName("winner") public User winner;
    @SerializedName("partecipations") public List<Partecipation> partecipations;
    @SerializedName("user") public User userLeft;

    public WinnerPartecipationsUserleft() {}
}
