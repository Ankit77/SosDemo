package app.sosdemo.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.sosdemo.KavachApp;
import app.sosdemo.MainActivity;
import app.sosdemo.R;
import app.sosdemo.util.Constant;
import app.sosdemo.util.Utils;
import app.sosdemo.webservice.WSChangePassword;
import app.sosdemo.webservice.WSForgotPassword;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class ForgotPassFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText etEmail;
    private Button btnSubmit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgotpassword, null);
        init();
        return view;
    }

    private void init() {
        ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_forgotpass));
        ((MainActivity) getActivity()).isshowBackButton(true);
        ((MainActivity) getActivity()).isMenuButton(false);
        etEmail = (EditText) view.findViewById(R.id.fragnent_forgotpass_et_email);
        btnSubmit = (Button) view.findViewById(R.id.fragnent_forgotpass_btn_submit);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            if (isvalid()) {

            }
        }
    }

    private boolean isvalid() {
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_empty_email));
            return false;
        } else if (Utils.isValidEmail(etEmail.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_valid_email));
            return false;
        }
        return true;
    }


}
