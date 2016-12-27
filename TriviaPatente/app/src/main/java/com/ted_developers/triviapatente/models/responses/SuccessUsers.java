package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 29/11/16.
 */
@Parcel
public class SuccessUsers extends Success {
    @SerializedName("users") public List<User> users;

    public SuccessUsers() {}
}
