package it.triviapatente.android.app.utils.custom_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by donadev on 11/12/17.
 */

public class TPPolicyAndTermsDialog extends Dialog {
    @BindString(R.string.baseUrl) String baseUrl;
    @BindString(R.string.privacy_policy_path) String policyPath;
    @BindString(R.string.terms_path) String termsPath;
    @BindString(R.string.revoke_permissions_path) String revokePath;
    @BindString(R.string.drop_account_path) String dropPath;
    @BindString(R.string.get_data_path) String getPath;
    @BindString(R.string.modal_privacy_terms_message) String subtitle;

    @BindView(R.id.modal_message) TextView messageView;


    public TPPolicyAndTermsDialog(Context context) {
        super(context);
        setContentView(R.layout.modal_privacy_terms);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this, this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.3f);
        messageView.setText(TPUtils.translateEmoticons(subtitle));
    }


    private void redirect(String path) {
        String url = baseUrl + path;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        getContext().startActivity(browserIntent);
    }

    @OnClick({R.id.termsButton, R.id.policyButton, R.id.backButton, R.id.negativeButton, R.id.revokeButton, R.id.getDataButton, R.id.dropAccountButton})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.termsButton: this.redirect(termsPath);
                break;
            case R.id.policyButton: this.redirect(policyPath);
                break;

            case R.id.dropAccountButton: this.redirect(dropPath);
                break;

            case R.id.getDataButton: this.redirect(getPath);
                break;

            case R.id.revokeButton: this.redirect(revokePath);
                break;
            default:
                this.dismiss();
        }
    }
}
