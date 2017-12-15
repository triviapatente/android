package it.triviapatente.android.socket.modules.events;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.responses.SuccessRoundDetails;

import org.parceler.Parcel;

/**
 * Created by donadev on 15/11/17.
 */
@Parcel
public class RoundStartedEvent extends SuccessRoundDetails {
    @SerializedName("category")
    public Category category;

    public RoundStartedEvent() {}
}
