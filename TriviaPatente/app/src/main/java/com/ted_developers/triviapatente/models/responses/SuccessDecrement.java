package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Antonio on 04/01/17.
 */
@Parcel
public class SuccessDecrement extends Success {
    @SerializedName("decrement") public Integer decrement;
    public SuccessDecrement() {}
}
