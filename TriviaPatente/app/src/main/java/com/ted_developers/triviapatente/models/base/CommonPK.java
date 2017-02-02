package com.ted_developers.triviapatente.models.base;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public abstract class CommonPK extends  Base {
    @SerializedName("id") public Long id;

    public CommonPK() {}
}
