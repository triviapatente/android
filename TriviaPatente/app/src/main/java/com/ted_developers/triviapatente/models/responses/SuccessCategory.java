package com.ted_developers.triviapatente.models.responses;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Category;

/**
 * Created by Antonio on 24/12/16.
 */
public class SuccessCategory extends Success {
    @SerializedName("category") public Category category;
    public SuccessCategory() {}
}
