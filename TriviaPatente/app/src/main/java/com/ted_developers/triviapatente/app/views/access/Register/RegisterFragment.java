package com.ted_developers.triviapatente.app.views.access.Register;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.custom_classes.input.LabeledInput;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.LoadingButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.output.MessageBox;
import com.ted_developers.triviapatente.app.views.access.FirstAccessActivity;

import butterknife.BindColor;
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
    @BindView(R.id.terms_and_conditions) TextView termsandconditionsLink;
    @BindView(R.id.dummy_layout) LinearLayout dummyLayout;
    // useful strings
    @BindString(R.string.hint_password) String passwordHint;
    @BindString(R.string.hint_username) String usernameHint;
    @BindString(R.string.hint_email) String emailHint;
    @BindString(R.string.hint_repeat_password) String repeatPasswordHint;
    @BindString(R.string.operation_failed) String operationFailed;
    @BindString(R.string.username_already_exist) String already_registered_username;
    @BindString(R.string.email_already_exist) String already_registered_email;
    @BindString(R.string.terms_and_conditions_registration1) String terms_and_conditions_registration;
    @BindString(R.string.terms_and_conditions_registration2) String terms_and_conditions_registration2;
    @BindString(R.string.terms_and_conditions_registration3) String terms_and_conditions_registration3;

    @BindString(R.string.terms_and_conditions_easteregg) String terms_and_conditions_easteregg;
    // colors
    @BindColor(R.color.greenTermsAndConditions) int green;

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
        // change color to terms and conditions
        String str = terms_and_conditions_registration + terms_and_conditions_registration2 + terms_and_conditions_registration3;
        Spannable spannable = new SpannableString(str);
        spannable.setSpan(
                new ForegroundColorSpan(green),
                terms_and_conditions_registration.length(),
                (terms_and_conditions_registration + terms_and_conditions_registration2).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        termsandconditionsLink.setText(spannable, TextView.BufferType.SPANNABLE);
        terms_and_conditions_easteregg = TPUtils.translateEmoticons(terms_and_conditions_easteregg);
        // translate emoticon
        operationFailed = TPUtils.translateEmoticons(operationFailed);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set hide keyboard on click on view
        setHideOnClick();
    }

    public void setHideOnClick() {
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TPUtils.hideKeyboard(getActivity(), dummyLayout);
            }
        });
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
        RegisterPresenter.register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.already_registered)
    public void swipeToLogin() {
        ((FirstAccessActivity) getActivity()).mViewPager.setCurrentItem(1);
    }

    @OnClick(R.id.terms_and_conditions)
    public void seeTermsConditions() {
        Toast.makeText(getContext(), terms_and_conditions_easteregg, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Belli sti termini", Toast.LENGTH_SHORT).show();
        // TODO open terms
    }
}
