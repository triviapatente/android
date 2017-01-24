package com.ted_developers.triviapatente.models.game;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Game extends CommonPK implements Comparable<Game> {
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

    public Game() {}

    @Override
    public int compareTo(Game anotherGame) {
        // i'm finished and the other not
        if(ended && !anotherGame.ended) {
            return 1;
        }
        // it's my turn
        else if(my_turn) {
            // if the other is finished or it is not my turn i have the precedence
            if(!anotherGame.my_turn || anotherGame.ended) return -1;
        }
        // it is not my turn but i'm not finished
        else {
            if(anotherGame.my_turn) return 1;
            else if(anotherGame.ended) return -1;
        }
        // if they are the same they are ordered by updated date
        return anotherGame.updatedAt.compareTo(updatedAt);
    }
}
