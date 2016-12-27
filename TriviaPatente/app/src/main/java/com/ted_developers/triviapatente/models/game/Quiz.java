package com.ted_developers.triviapatente.models.game;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Quiz extends CommonPK {
    @SerializedName("image_id") public String image_id;
    @SerializedName("question") public String question;
    @SerializedName("answer") public Boolean answer;
    @SerializedName("category_id") public Long category_id;
    @SerializedName("my_answer") public Boolean my_answer;
    @SerializedName("answered_correctly") public Boolean answered_correctly;

    public Quiz() {}
}
