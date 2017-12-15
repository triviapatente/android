package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;

import it.triviapatente.android.models.game.Partecipation;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 11/12/16.
 */
@Parcel
public class WinnerPartecipationsUserleft extends Success{
    @SerializedName("winner_id") public Long winner;
    @SerializedName("partecipations") public List<Partecipation> partecipations;
    @SerializedName("user_id") public Long userLeft;

    public WinnerPartecipationsUserleft() {}
}
