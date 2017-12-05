package com.ted_developers.triviapatente.app.utils.custom_classes.actionBar;

/**
 * Created by Antonio on 04/01/17.
 */
// TODO remove
@Deprecated
public enum TPActionBarType {
    backPictureMenu(0),
    backPicture(1),
    heartPictureMenu(2);

    public int id;

    TPActionBarType(int id) {
        this.id = id;
    }

    static TPActionBarType fromId(int id) {
        for (TPActionBarType type : values()) {
            if (type.id == id) return type;
        }
        throw new IllegalArgumentException();
    }
}
