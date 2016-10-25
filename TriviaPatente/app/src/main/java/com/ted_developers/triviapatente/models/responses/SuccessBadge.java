package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class SuccessBadge extends Success {
    @SerializedName("badge")
    public Integer badge;

    public SuccessBadge() {}
}