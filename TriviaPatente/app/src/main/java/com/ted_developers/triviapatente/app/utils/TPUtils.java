package com.ted_developers.triviapatente.app.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}
