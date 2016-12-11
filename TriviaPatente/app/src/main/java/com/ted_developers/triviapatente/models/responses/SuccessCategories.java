package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Category;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 11/12/16.
 */
@Parcel
public class SuccessCategories extends Success {
    @SerializedName("categories") public List<Category> categories;

    public SuccessCategories() {}
}
