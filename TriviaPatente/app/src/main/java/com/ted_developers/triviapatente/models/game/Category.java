package com.ted_developers.triviapatente.models.game;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Category extends CommonPK {
    @SerializedName("name")
    public String name;
    @SerializedName("hint")
    public String hint;
    @SerializedName("total_answers")
    public int total_answers;
    @SerializedName("correct_answers")
    public int correct_answers;

    public Category() {}
}
