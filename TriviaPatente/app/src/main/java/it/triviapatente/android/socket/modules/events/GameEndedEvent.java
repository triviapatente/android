package it.triviapatente.android.socket.modules.events;

import com.google.gson.annotations.SerializedName;

import it.triviapatente.android.models.game.Game;
import it.triviapatente.android.models.game.Partecipation;
import it.triviapatente.android.models.responses.Success;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by donadev on 15/11/17.
 */
@Parcel
public class GameEndedEvent extends Success {
    @SerializedName("winner_id")
    public Long winnerId;
    @SerializedName("game")
    public Game game;

    @SerializedName("partecipations")
    public List<Partecipation> partecipations;

    public GameEndedEvent() {}
}
