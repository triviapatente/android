package it.triviapatente.android.socket.modules.events;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.auth.User;

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
