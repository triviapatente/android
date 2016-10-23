package com.ted_developers.triviapatente.models.game;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Question extends CommonPK {
    @SerializedName("answer")
    public boolean answer;
    @SerializedName("quiz_id")
    public long quiz_id;
    @SerializedName("round_id")
    public long round_id;
    @SerializedName("user_id")
    public long user_id;

    public Question() {}
}
