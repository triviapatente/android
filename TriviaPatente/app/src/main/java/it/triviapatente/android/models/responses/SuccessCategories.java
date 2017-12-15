package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.game.Category;

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
