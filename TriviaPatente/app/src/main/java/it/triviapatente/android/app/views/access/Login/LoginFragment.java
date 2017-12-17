package it.triviapatente.android.app.views.access.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.custom_classes.dialogs.TPPolicyAndTermsDialog;
import it.triviapatente.android.app.utils.custom_classes.input.LabeledInput;
import it.triviapatente.android.app.utils.custom_classes.buttons.LoadingButton;
import it.triviapatente.android.app.utils.custom_classes.input.LabeledInputError;
import it.triviapatente.android.app.utils.custom_classes.output.MessageBox;

import butterknife.BindColor;
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
    @BindView(R.id.username_field)
    LabeledInput usernameField;
    @BindView(R.id.password_field) LabeledInput passwordField;
    @BindView(R.id.login_button)
    LoadingButton loginButton;
    @BindView(R.id.alertMessage)
    MessageBox alertMessageView;
    @BindView(R.id.forgot_button) TextView loginFailedButton;
    @BindView(R.id.dummy_layout) LinearLayout dummyLayout;
    // useful strings
    @BindString(R.string.hint_password) String passwordHint;
    @BindString(R.string.hint_username_or_email) String usernameHint;
    @BindString(R.string.login_failed) String forgotUsernamePassword;
    @BindString(R.string.operation_failed) String operationFailed;
    // useful dimension
    @BindDimen(R.dimen.element_margin) int marginBottom;
    @BindDimen(R.dimen.field_height) int fieldHeight;
    @BindDimen(R.dimen.field_margin) int fieldMargin;
    // terms and conditions
    @BindString(R.string.activity_contacts_terms_and_conditions1) String terms_and_conditions1;
    @BindString(R.string.activity_contacts_terms_and_conditions2) String terms_and_conditions2;
    @BindString(R.string.activity_contacts_terms_and_conditions3) String terms_and_conditions3;
    @BindString(R.string.activity_contacts_terms_and_conditions4) String terms_and_conditions4;
    @BindColor(R.color.greenTermsAndConditions) int green;
    @BindView(R.id.terms_and_conditions) TextView termsAndPolicylink;

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
        // translate emoticon
        operationFailed = TPUtils.translateEmoticons(operationFailed);
        // terms and conditions
        setTermsAndPolicyText();
        return v;
    }

    @OnClick(R.id.terms_and_conditions)
    public void termsAndConditionsClick() {
        TPPolicyAndTermsDialog dialog = new TPPolicyAndTermsDialog(getContext());
        dialog.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set current dummy layout
        ((TPActivity) getActivity()).dummyLayout = dummyLayout;
    }

    private Spannable markTermsText(int lower, int upper, Spannable spannable) {
        spannable.setSpan(new ForegroundColorSpan(green),
                lower,
                upper,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void setTermsAndPolicyText() {
        String str = terms_and_conditions1 + terms_and_conditions2 + terms_and_conditions3 + terms_and_conditions4;
        Spannable spannable = new SpannableString(str);
        String lower = terms_and_conditions1;
        String upper = terms_and_conditions1 + terms_and_conditions2;
        spannable = markTermsText(lower.length(), upper.length(), spannable);
        lower = upper + terms_and_conditions3;
        spannable = markTermsText(lower.length(), str.length(), spannable);
        termsAndPolicylink.setText(spannable, TextView.BufferType.SPANNABLE);
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

        // remove auto correction
        usernameField.setAutoCheck(false);
        passwordField.setAutoCheck(false);

        // set errors to be aware of
        usernameField.setErrorsToCheck(null, LabeledInputError.EMPTY, LabeledInputError.BLANK);
        passwordField.setErrorsToCheck(null, LabeledInputError.EMPTY);
    }

    // do login
    private boolean isFirstAttempt = true;
    @OnClick(R.id.login_button)
    public void login() {
        TPUtils.hideKeyboard(getActivity(), dummyLayout);
        LoginPresenter.login(this, isFirstAttempt);
        isFirstAttempt = false;
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

    // forgot password or username
    @OnClick(R.id.forgot_button)
    public void forgotUsernamePassword(){
        Intent i = new Intent(getActivity(), CredentialsRecovery.class);
        getActivity().startActivity(i);
    }
}
