package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import it.triviapatente.android.models.auth.User;

/**
 * Created by donadev on 16/02/18.
 */
@Parcel
public class SuccessUser extends Success {

    @SerializedName("user")
    public User user;
}
