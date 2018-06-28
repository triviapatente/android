package it.triviapatente.android.models.base;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public abstract class Base {
    @SerializedName("createdAt") public String createdAt;
    @SerializedName("updatedAt") public String updatedAt;
    public static transient final SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss ZZZZ" , Locale.ENGLISH);

    public Date getCreatedAt() {
        try {
            return format.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Date getUpdatedAt() {
        try {
            return format.parse(updatedAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getUpdatedAtMillis() {

        long time = 0;
        try {
            time = format.parse(updatedAt).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    protected Base() {}
}
