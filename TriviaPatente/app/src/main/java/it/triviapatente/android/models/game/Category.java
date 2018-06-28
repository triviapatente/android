package it.triviapatente.android.models.game;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import it.triviapatente.android.R;
import it.triviapatente.android.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Category extends CommonPK {
    @SerializedName("name") public String name;
    @SerializedName("hint") public String hint;
    @SerializedName("total_answers") public int total_answers;
    @SerializedName("correct_answers") public int correct_answers;
    @SerializedName("color") public String color;
    @SerializedName("imagePath") public String imagePath;

    public Category() {}
    public int getBackground() {
        Integer progress = getProgress();
        if(progress < 75) return R.drawable.button_stats_bad;
        if(progress < 90) return R.drawable.button_stats_medium;
        if(progress < 95) return R.drawable.button_stats_good;
        return R.drawable.button_stats_perfect;
    }
    private int getHint() {
        Integer progress = getProgress();
        if(progress < 75) return R.string.category_status_bad;
        if(progress < 90) return R.string.category_status_medium;
        if(progress < 95) return R.string.category_status_good;
        return R.string.category_status_perfect;
    }
    public String getHint(Context context) {
        return context.getString(getHint());
    }
    public String getDescription(Context context) {
        String infoText = context.getString(total_answers == 1 ? R.string.stats_answered_question : R.string.stats_answered_questions);
        return infoText.replace("%d", "" + total_answers);
    }
    public Integer getProgress() {
        if(total_answers == 0) return 0;
        else return correct_answers * 100 / total_answers;
    }
}
