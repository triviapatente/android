package com.ted_developers.triviapatente.app.views.first_access.Register;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.mViews.LoadingButton.LoadingButton;

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
    @BindView(R.id.username_field) EditText usernameField;
    @BindView(R.id.password_field) EditText passwordField;
    @BindView(R.id.repeat_password_field) EditText repeatPasswordField;
    @BindView(R.id.email_field) EditText emailField;
    @BindView(R.id.register_button) LoadingButton registerButton;
    private Unbinder unbinder;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
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
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.register_button)
    public void register() {
        RegisterPresenter.register(
                usernameField.getText().toString(),
                emailField.getText().toString(),
                passwordField.getText().toString(),
                repeatPasswordField.getText().toString(),
                registerButton);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
