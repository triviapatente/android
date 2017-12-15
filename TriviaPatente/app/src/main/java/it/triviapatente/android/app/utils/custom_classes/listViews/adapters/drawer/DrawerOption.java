package it.triviapatente.android.app.utils.custom_classes.listViews.adapters.drawer;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;

public class DrawerOption {
    int layoutResource, drawableResource;
    public drawer_options_type optionType;
    public String optionName;

    public DrawerOption (@LayoutRes int layoutResource, @DrawableRes int drawableResource, drawer_options_type optionType, String optionName){
        this.layoutResource = layoutResource;
        this.drawableResource = drawableResource;
        this.optionType = optionType;
        this.optionName = optionName;
    }

    public DrawerOption (@LayoutRes int layoutResource, drawer_options_type optionType){
        this.layoutResource = layoutResource;
        this.optionType = optionType;
    }
}
