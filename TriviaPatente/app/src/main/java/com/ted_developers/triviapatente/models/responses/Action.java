package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Antonio on 24/01/17.
 */
@Parcel
public class Action extends Success {
    @SerializedName("action") public String action;
     public Action() {}
}
