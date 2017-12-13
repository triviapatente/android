package com.ted_developers.triviapatente.models.auth;


import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.CommonPK;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class User extends CommonPK {
    @SerializedName("username") public String username;
    @SerializedName("email") public String email;
    @SerializedName("name") public String name;
    @SerializedName("surname") public String surname;
    @SerializedName("image") public String image;
    @SerializedName("score") public Integer score;
    @SerializedName("last_game_won") public Boolean last_game_won;
    @SerializedName("showLifePopup") public Boolean showLifePopup = true;
    @SerializedName("lastLifePopupShowedDate") public Long lastLifePopupShowedDateMillis = null; // null if is the first time the app has been opened
    @SerializedName("showRatePopup") public Boolean showRatePopup = true;
    @SerializedName("lastRatePopupShowedDate") public Long lastRatePopupShowedDateMillis = null; // null if is the first time the app has been opened
    @SerializedName("position") public Integer position;
    @SerializedName("internalPosition") public Integer internalPosition;
    @SerializedName("lastAppVersionCode") public Integer lastAppVersionCode;

    public User() {}

    public String initialLetters() {
        if(name != null && surname != null) return (String.valueOf(name.charAt(0)) + String.valueOf(surname.charAt(0))).toUpperCase();
        return username.substring(0, 2).toUpperCase();
    }

    public User(Long id, String username, String image, Boolean last_game_won, Integer score) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.last_game_won = last_game_won;
        this.score = score;
    }

    public User(Long id, String username, String name, String surname, String image) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.image = image;
    }

    public String toString() {
        if(name == null || surname == null) {
            return username;
        } else {
            return name + " " + surname;
        }
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (!(anotherObject instanceof User)) {
            return false;
        }
        return this.id == ((User) anotherObject).id;
    }
}
