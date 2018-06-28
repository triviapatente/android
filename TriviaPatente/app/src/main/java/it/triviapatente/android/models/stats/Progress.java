package it.triviapatente.android.models.stats;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Random;

import it.triviapatente.android.R;
import it.triviapatente.android.models.base.Base;

/**
 * Created by donadev on 26/06/18.
 */
@Parcel
public class Progress {
    @SerializedName("correct_answers") public Integer correctAnswers;
    @SerializedName("total_answers") public Integer totalAnswers;
    public Progress() {}


    public void generateMock() {
        totalAnswers = new Random().nextInt(50) + 1;
        correctAnswers = new Random().nextInt(totalAnswers);
    }

    public float getPercent() {
        if(correctAnswers == null) correctAnswers = 0;
        if(totalAnswers == 0) return 100;
        return ((float)correctAnswers / totalAnswers) * 100;
    }
    public int numberOfErrors() {
        return totalAnswers - correctAnswers;
    }
    public int getColor() {
        float percent = getPercent();
        if(percent <= 25) return R.color.stats_bad;
        if(percent <= 50) return R.color.stats_medium;
        if(percent <= 75) return R.color.stats_good;
        return R.color.stats_perfect;
    }
}
