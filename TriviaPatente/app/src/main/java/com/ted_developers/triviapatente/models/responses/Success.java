package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Success {
    @SerializedName("success")
    public boolean success;

    public Success() {}
}
