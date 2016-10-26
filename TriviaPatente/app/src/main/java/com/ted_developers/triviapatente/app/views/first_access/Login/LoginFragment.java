package com.ted_developers.triviapatente.app.views.first_access.Login;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.mViews.Input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.mViews.LoadingButton.LoadingButton;
import com.ted_developers.triviapatente.app.views.first_access.FirstAccessActivity;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // dom elements
    @BindView(R.id.username_field) LabeledInput usernameField;
    @BindView(R.id.password_field) LabeledInput passwordField;
    @BindView(R.id.login_button) LoadingButton loginButton;
    @BindView(R.id.login_failed) LinearLayout loginFailedAlert;
    @BindView(R.id.alertMessage) TextView alertMessageView;
    @BindView(R.id.forgot_button) Button loginFailedButton;
    // useful strings
    @BindString(R.string.password) String passwordHint;
    @BindString(R.string.username) String usernameHint;
    @BindString(R.string.forgot_username_password) String forgotUsernamePassword;
    @BindString(R.string.operation_failed) String operationFailed;
    // useful dimension
    @BindDimen(R.dimen.element_margin) int marginBottom;
    @BindDimen(R.dimen.field_height) int fieldHeight;
    @BindDimen(R.dimen.field_margin) int fieldMargin;
    // Layout padding for login button
    RelativeLayout.LayoutParams loginButtonParams;

    private Unbinder unbinder;

    public LoginFragment() {
        // Required empty public constructor
    }

    // get fragment new instance in this way
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        // bind butterknife
        unbinder = ButterKnife.bind(this, v);
        // init labeled inputs
        initLabeledInputs();
        // init login button layout params
        initLoginButtonLayoutParams();
        // hide alert and forgot button
        hideAlert();
        hideForgotButton();
        return v;
    }

    // set hints and passwords (can't do this from xml)
    private void initLabeledInputs() {
        usernameField.setHint(usernameHint);
        passwordField.setHint(passwordHint);
        passwordField.setPassword(true);
    }

    // do login
    @OnClick(R.id.login_button)
    public void login() {
        ((FirstAccessActivity) getActivity()).hideKeyboard();
        LoginPresenter.login(this);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    // show alert with given message
    // show forgor username or password button
    // hide alert
    // hide button
}
