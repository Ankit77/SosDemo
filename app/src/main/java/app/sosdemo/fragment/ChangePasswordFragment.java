package app.sosdemo.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import app.sosdemo.KavachApp;
import app.sosdemo.MainActivity;
import app.sosdemo.R;
import app.sosdemo.util.Constant;
import app.sosdemo.util.Utils;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText etNewPassword;
    private EditText etOldPassword;
    private EditText etConfirmPassword;
    private Button btnChangePass;

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

    }

    @Override
    public void onClick(View v) {
        if (v == btnChangePass) {
            if (isValid()) {
                getFragmentManager().popBackStack();
                SharedPreferences.Editor editor = KavachApp.getInstance().getPref().edit();
                editor.putString(Constant.PREF_PASSWORD, etNewPassword.getText().toString());
                editor.commit();
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
        } else if (!etOldPassword.getText().toString().equalsIgnoreCase(etConfirmPassword.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), "Old password and Confirm password are not same");
            return false;
        }
        return true;
    }
}
