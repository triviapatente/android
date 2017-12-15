package it.triviapatente.android.app.utils.custom_classes.input;

import android.content.Context;
import android.widget.EditText;

import it.triviapatente.android.R;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Antonio on 05/12/17.
 */

public enum LabeledInputError {
    EMAIL(new ErrorChecker() {
        @Override
       public boolean isValid(EditText input) {
           return android.util.Patterns.EMAIL_ADDRESS.matcher(input.getText().toString()).matches();
       }
    }),
    EMPTY(new ErrorChecker() {
        @Override
        public boolean isValid(EditText input) {
            return !input.getText().toString().replace(" ", "").equals("");
        }
    }),
    BLANK(new ErrorChecker() {
        @Override
        public boolean isValid(EditText input) {
            return !input.getText().toString().trim().contains(" ");
        }
    }),
    USERNAME_LENGTH(new ErrorChecker() {
        @Override
        public boolean isValid(EditText input) {
            Integer min_length = (Integer) USERNAME_LENGTH.otherData.get(min_username_length_key);
            return min_length != null &&
                    input.getText().toString().length() >= min_length;
        }
    }),
    PASSWORD_LENGTH(new ErrorChecker() {
        @Override
        public boolean isValid(EditText input) {
            Integer min_length = (Integer) PASSWORD_LENGTH.otherData.get(min_password_length_key);
            return min_length != null &&
                    input.getText().toString().length() >= min_length;
        }
    }),
    PASSWORD_EQUALS(new ErrorChecker() {
        @Override
        public boolean isValid(EditText input) {
            EditText otherInput = (EditText) PASSWORD_EQUALS.otherData.get(other_pwd_key);
            return otherInput != null &&
                    otherInput.getText().toString().equals(input.getText().toString());
        }
    });

    private String errorMessage;
    private ErrorChecker checker;
    private Dictionary<String, Object> otherData = new Hashtable<>();

    private static String min_username_length_key = "min_username_length",
                          min_password_length_key = "min_password_length_key",
                          other_pwd_key = "other_pwd";

    LabeledInputError(ErrorChecker checker) {
        this.checker = checker;
    }

    public static void initAll(Context context) {
        for(LabeledInputError e : LabeledInputError.values()) {
            switch (e) {
                case EMAIL:
                    EMAIL.errorMessage = context.getString(R.string.not_valid_email);
                    break;
                case USERNAME_LENGTH:
                    USERNAME_LENGTH.errorMessage = context.getString(R.string.not_valid_username);
                    USERNAME_LENGTH.otherData.put(min_username_length_key, context.getResources().getInteger(R.integer.min_username_length));
                    break;
                case PASSWORD_LENGTH:
                    PASSWORD_LENGTH.errorMessage = context.getString(R.string.not_valid_password);
                    PASSWORD_LENGTH.otherData.put(min_password_length_key, context.getResources().getInteger(R.integer.min_password_length));
                    break;
                case EMPTY:
                    EMPTY.errorMessage = context.getString(R.string.field_required);
                    break;
                case BLANK:
                    BLANK.errorMessage = context.getString(R.string.blank_not_allowed);
                    break;
                case PASSWORD_EQUALS:
                    PASSWORD_EQUALS.errorMessage = context.getString(R.string.not_matching_pwd);
            }
        }
    }

    public void setOtherInput(EditText input) {
        this.otherData.put(other_pwd_key, input);
    }

    @Override
    public String toString() {
        return errorMessage;
    }

    public boolean isValid(EditText input) {
        return checker.isValid(input);
    }
}

abstract class ErrorChecker {
    public abstract boolean isValid(EditText input);
    public ErrorChecker(){}
}