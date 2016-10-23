package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class UserToken {
    @SerializedName("user")
    public User user;
    @SerializedName("token")
    public String token;

    public UserToken() {}
}
