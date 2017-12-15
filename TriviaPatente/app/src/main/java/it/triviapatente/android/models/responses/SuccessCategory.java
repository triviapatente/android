package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.game.Category;

import org.parceler.Parcel;

/**
 * Created by Antonio on 24/12/16.
 */
@Parcel
public class SuccessCategory extends Success {
    @SerializedName("category") public Category category;
    public SuccessCategory() {}
}
