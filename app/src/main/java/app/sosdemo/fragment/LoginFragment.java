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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import app.sosdemo.KavachApp;
import app.sosdemo.MainActivity;
import app.sosdemo.R;
import app.sosdemo.util.Constant;
import app.sosdemo.util.Utils;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnForgotPassword;
    private TextView tvRegister;
    private CheckBox chkRememberme;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, null);
        init();
        return view;
    }

    private void init() {
        ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_login));
        ((MainActivity) getActivity()).isshowBackButton(false);
        ((MainActivity) getActivity()).isMenuButton(false);
        etPassword = (EditText) view.findViewById(R.id.fragnent_login_et_password);
        etUserName = (EditText) view.findViewById(R.id.fragnent_login_et_username);
        btnLogin = (Button) view.findViewById(R.id.fragnent_login_btn_login);
        btnForgotPassword = (Button) view.findViewById(R.id.fragnent_login_btn_forgotpassword);
        tvRegister = (TextView) view.findViewById(R.id.fragnent_login_tv_register);
        chkRememberme = (CheckBox) view.findViewById(R.id.fragnent_login_chk_remember);
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        chkRememberme.setChecked(KavachApp.getInstance().getPref().getBoolean(Constant.PREF_ISREMEMBER, false));
        if (KavachApp.getInstance().getPref().getBoolean(Constant.PREF_ISREMEMBER, false)) {
            etUserName.setText(KavachApp.getInstance().getPref().getString(Constant.PREF_USERNAME, ""));
            etPassword.setText(KavachApp.getInstance().getPref().getString(Constant.PREF_PASSWORD, ""));

        }

        chkRememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = KavachApp.getInstance().getPref().edit();
                editor.putBoolean(Constant.PREF_ISREMEMBER, isChecked);
                editor.commit();

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {

            if (TextUtils.isEmpty(etUserName.getText().toString())) {
                Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_username));
            } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
                Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_empty_password));
            } else {

                SharedPreferences.Editor editor = KavachApp.getInstance().getPref().edit();
                editor.putBoolean(Constant.PREF_IS_LOGIN, true);
                editor.putString(Constant.PREF_USERNAME, etUserName.getText().toString());
                editor.putString(Constant.PREF_PASSWORD, etPassword.getText().toString());
                editor.commit();
                DashboardFragment dashboardFragment = new DashboardFragment();
                Utils.replaceNextFragment(R.id.container, getActivity(), dashboardFragment);
            }
        } else if (v == btnForgotPassword) {
            Utils.addNextFragmentNoAnim(R.id.container, getActivity(), new ForgotPassFragment(), LoginFragment.this);
        } else if (v == tvRegister) {
            Utils.addNextFragmentNoAnim(R.id.container, getActivity(), new RegisterFragment(), LoginFragment.this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_login));
        ((MainActivity) getActivity()).isshowBackButton(true);
        ((MainActivity) getActivity()).isMenuButton(false);
    }
}
