package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.auth.User;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class SuccessUserToken extends Success {
    @SerializedName("user")
    public User user;
    @SerializedName("token")
    public String token;

    public SuccessUserToken() {}
}
