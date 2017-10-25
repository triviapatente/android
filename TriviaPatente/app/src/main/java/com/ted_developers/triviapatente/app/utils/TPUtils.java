package com.ted_developers.triviapatente.app.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.models.game.Question;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.blurry.Blurry;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;;

/**
 * Created by Antonio on 30/12/16.
 */
public class TPUtils {
    public static boolean isPointInsideView(int x, int y, View view) {
        Rect area = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

        //point is inside view bounds
        return area.contains(x, y);
    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    private static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String translateEmoticons(String string) {
        Matcher matcher = Pattern.compile("0x[0-9A-Za-z]*").matcher(string);
        while(matcher.find()) {
            String unicode = matcher.group();
            string = string.replaceAll(unicode, getEmojiByUnicode(Integer.decode(unicode)));
        }
        return string;
    }

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public static void blurContainerIntoImageView(Context context, ViewGroup container, ImageView imageView) {
        Blurry.with(context).sampling(3).radius(13).capture(container).into(imageView);
    }


    // picasso utils
    public static Picasso picasso;
    public static void initPicasso(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header(context.getString(R.string.shared_token_key), SharedTPPreferences.getToken())
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();
    }
    public static String getUserImageFromID(Context context, long ID) {
        return context.getString(R.string.baseUrl)+ "account/image/" + ID;
    }

    public static String getQuizImageFromID(Context context, long ID) {
        return context.getString(R.string.baseUrl)+ "quiz/image/" + ID;
    }

    public static String getCategoryImageFromID(Context context, long ID) {
        return context.getString(R.string.baseUrl)+ "category/image/" + ID;
    }

    public static int getUserScoreFromID(List<Question> answers, Long userID) {
        int score = 0;
        for(Question question : answers) {
            if(question.user_id.equals(userID) && question.correct) {
                score++;
            }
        }
        return score;
    }
}
