package it.triviapatente.android.models.game;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import it.triviapatente.android.R;
import it.triviapatente.android.models.base.CommonPK;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Game extends CommonPK {
    @SerializedName("creator_id") public Long creator_id;
    @SerializedName("winner_id") public Long winner_id;
    @SerializedName("ended") public Boolean ended;
    @SerializedName("started") public Boolean started;
    @SerializedName("opponent_username") public String opponent_username;
    @SerializedName("opponent_name") public String opponent_name;
    @SerializedName("opponent_surname") public String opponent_surname;
    @SerializedName("opponent_id") public Long opponent_id;
    @SerializedName("opponent_image") public String opponent_image;
    @SerializedName("my_turn") public Boolean my_turn;
    @SerializedName("my_score") public Integer myScore;
    @SerializedName("opponent_score") public Integer opponentScore;
    @SerializedName("remaining_answers_count") public Integer remainingAnswersCount;
    @SerializedName("expired") public Boolean expired;
    public Game() {}

    public Long getRemainingTime(Long maxAge) {
        Date creation = getCreatedAt();
        if(maxAge == null || creation == null) return (long) (60 * 60);
        Long offset = (new Date().getTime() - creation.getTime()) / 1000;
        return maxAge - offset;
    }
    public String getExpirationDescription(Context ctx, Long maxAge) {
        Long remaining = getRemainingTime(maxAge);
        if(remaining < 0) return "";
        Long hour = (long) 60 * 60;
        if(remaining < hour) return ctx.getString(R.string.less_than_hour_expiration);
        Integer hours = (int) (remaining / hour);
        if(hours == 1) {
            return ctx.getString(R.string.explicit_expiration_single).replace("%d", "" + hours);
        } else {
            return ctx.getString(R.string.explicit_expiration_plural).replace("%d", "" + hours);
        }
    }
}
