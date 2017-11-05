package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.auth.User;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 05/11/17.
 */

@Parcel
public class Matches {
    @SerializedName("matches") public List<User> matches;

    public Matches() {}
}
