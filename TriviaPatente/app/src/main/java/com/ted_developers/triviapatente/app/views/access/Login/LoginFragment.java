package com.ted_developers.triviapatente.app.views.access.Login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.custom_classes.loading.LoadingButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.output.MessageBox;
import com.ted_developers.triviapatente.app.views.access.FirstAccessActivity;

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
    @BindView(R.id.alertMessage) MessageBox alertMessageView;
    @BindView(R.id.forgot_button) Button loginFailedButton;
    // useful strings
    @BindString(R.string.hint_password) String passwordHint;
    @BindString(R.string.hint_username) String usernameHint;
    @BindString(R.string.login_failed) String forgotUsernamePassword;
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
        alertMessageView.hideAlert();
        hideForgotButton();
        return v;
    }

    private void initLoginButtonLayoutParams() {
        loginButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, fieldHeight);
        loginButton.setLayoutParams(loginButtonParams);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // show forgor username or password button
    public void showForgotButton() {
        // show forgot button
        if (loginFailedButton.getVisibility() == View.GONE) {
            // remove padding from login button
            loginFailedButton.setVisibility(View.VISIBLE);
        }
    }

    // hide button
    public void hideForgotButton() {
        // hide forgot button
        if (loginFailedButton.getVisibility() == View.VISIBLE) {
            loginFailedButton.setVisibility(View.GONE);
        }
    }
}