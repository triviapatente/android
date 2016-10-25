package com.ted_developers.triviapatente.models.auth;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.Base;
import com.ted_developers.triviapatente.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class User extends CommonPK {
    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("name")
    public String name;
    @SerializedName("surname")
    public String surname;
    @SerializedName("image")
    public String image;
    @SerializedName("score")
    public Integer score;

    public User() {}
}
