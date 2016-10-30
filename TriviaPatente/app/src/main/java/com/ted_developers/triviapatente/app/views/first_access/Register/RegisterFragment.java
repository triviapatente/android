package com.ted_developers.triviapatente.app.views.first_access.Register;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.loading.LoadingButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.output.MessageBox;
import com.ted_developers.triviapatente.app.views.first_access.FirstAccessActivity;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    // dom elements
    @BindView(R.id.username_field) LabeledInput usernameField;
    @BindView(R.id.password_field) LabeledInput passwordField;
    @BindView(R.id.repeat_password_field) LabeledInput repeatPasswordField;
    @BindView(R.id.email_field) LabeledInput emailField;
    @BindView(R.id.register_button) LoadingButton registerButton;
    @BindView(R.id.alertMessage) MessageBox alertMessageView;
    // useful strings
    @BindString(R.string.hint_password) String passwordHint;
    @BindString(R.string.hint_username) String usernameHint;
    @BindString(R.string.hint_email) String emailHint;
    @BindString(R.string.hint_repeat_password) String repeatPasswordHint;
    @BindString(R.string.operation_failed) String operationFailed;
    @BindString(R.string.username_already_exist) String already_registered_username;
    @BindString(R.string.email_already_exist) String already_registered_email;

    private Unbinder unbinder;

    public RegisterFragment() {
        // Required empty public constructor
    }

    // get fragment new instance in this way
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        // bind butterknife
        unbinder = ButterKnife.bind(this, v);
        // init labeled inputs
        initLabeledInputs();
        // hide alert
        alertMessageView.hideAlert();
        return v;
    }

    // set hints and passwords (can't do this from xml)
    private void initLabeledInputs() {
        usernameField.setHint(usernameHint);
        emailField.setHint(emailHint);
        passwordField.setHint(passwordHint);
        passwordField.setPassword(true);
        repeatPasswordField.setHint(repeatPasswordHint);
        repeatPasswordField.setPassword(true);
    }

    // do registration
    @OnClick(R.id.register_button)
    public void register() {
        ((FirstAccessActivity) getActivity()).hideKeyboard();
        RegisterPresenter.register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
