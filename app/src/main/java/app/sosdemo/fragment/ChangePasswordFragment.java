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
import app.sosdemo.webservice.WSLogin;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText etNewPassword;
    private EditText etOldPassword;
    private EditText etConfirmPassword;
    private Button btnChangePass;
    private AsyncChangePassword asyncChangePassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_changepassword, null);
        init();
        return view;
    }

    private void init() {
        ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_changepassword));
        ((MainActivity) getActivity()).isshowBackButton(true);
        ((MainActivity) getActivity()).isMenuButton(false);
        etNewPassword = (EditText) view.findViewById(R.id.fragnent_changepassword_et_newpass);
        etOldPassword = (EditText) view.findViewById(R.id.fragnent_changepassword_et_oldpass);
        etConfirmPassword = (EditText) view.findViewById(R.id.fragnent_changepassword_et_confirm);
        btnChangePass = (Button) view.findViewById(R.id.fragnent_changepassword_btn_submit);
        btnChangePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnChangePass) {
            if (isValid()) {
                if (Utils.isNetworkAvailable(getActivity())) {
                    asyncChangePassword = new AsyncChangePassword();
                    asyncChangePassword.execute(KavachApp.getInstance().getPref().getString(Constant.PREF_USERNAME, ""), etOldPassword.getText().toString(), etNewPassword.getText().toString());
                } else {
                    Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_internet));
                }
            }
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etNewPassword.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), "Please enter new password");
            return false;
        } else if (TextUtils.isEmpty(etOldPassword.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), "Please enter old password");
            return false;
        } else if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), "Please enter confirm password");
            return false;
        } else if (!etNewPassword.getText().toString().equalsIgnoreCase(etConfirmPassword.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), "Old password and Confirm password are not same");
            return false;
        }
        return true;
    }


    private class AsyncChangePassword extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog progressDialog;
        private WSChangePassword wsChangePassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity());
        }

        @Override
        protected Boolean doInBackground(String... params) {
            wsChangePassword = new WSChangePassword(getActivity());
            return wsChangePassword.executeService(params[0], params[1], params[2]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!isCancelled()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (aBoolean) {
                    SharedPreferences.Editor editor = KavachApp.getInstance().getPref().edit();
                    editor.putString(Constant.PREF_PASSWORD, etNewPassword.getText().toString());
                    editor.commit();
                    getFragmentManager().popBackStack();
                    Toast.makeText(getActivity(), wsChangePassword.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Utils.displayDialog(getActivity(), getString(R.string.app_name), wsChangePassword.getMessage());
                }
            }
        }
    }
}
