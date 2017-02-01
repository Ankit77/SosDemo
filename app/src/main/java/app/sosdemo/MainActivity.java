package app.sosdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.sosdemo.fragment.DashboardFragment;
import app.sosdemo.fragment.LoginFragment;
import app.sosdemo.fragment.SettingFragemt;
import app.sosdemo.util.Constant;
import app.sosdemo.util.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;
    private ImageView imgSetting;
    private ImageView imgLogout;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if (KavachApp.getInstance().getPref().getBoolean(Constant.PREF_IS_LOGIN, false)) {
            DashboardFragment dashboardFragment = new DashboardFragment();
            Utils.replaceNextFragment(R.id.container, this, dashboardFragment);
        } else {
            LoginFragment loginFragment = new LoginFragment();
            Utils.replaceNextFragment(R.id.container, this, loginFragment);
        }

    }

    private void init() {
        imgBack = (ImageView) findViewById(R.id.activity_main_iv_back);
        imgLogout = (ImageView) findViewById(R.id.activity_main_iv_logout);
        imgSetting = (ImageView) findViewById(R.id.activity_main_iv_setting);
        tvTitle = (TextView) findViewById(R.id.activity_main_tv_title);
        imgBack.setOnClickListener(this);
        imgLogout.setOnClickListener(this);
        imgSetting.setOnClickListener(this);

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void isshowBackButton(boolean ishow) {
        if (ishow) {
            imgBack.setVisibility(View.VISIBLE);
        } else {
            imgBack.setVisibility(View.GONE);
        }
    }

    public void isMenuButton(boolean ishow) {
        if (ishow) {
            imgSetting.setVisibility(View.VISIBLE);
            imgLogout.setVisibility(View.VISIBLE);
        } else {
            imgSetting.setVisibility(View.GONE);
            imgLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            }
        } else if (v == imgLogout) {
            DashboardFragment dashboardFragment = (DashboardFragment) getFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName());
            if (dashboardFragment != null && dashboardFragment.isVisible()) {
                SharedPreferences.Editor editor = KavachApp.getInstance().getPref().edit();
                editor.putBoolean(Constant.PREF_IS_LOGIN, false);
                if (!KavachApp.getInstance().getPref().getBoolean(Constant.PREF_ISREMEMBER, false)) {
                    editor.putString(Constant.PREF_USERNAME, "");
                    editor.putString(Constant.PREF_PASSWORD, "");
                }
                editor.commit();
                Utils.replaceNextFragment(R.id.container, MainActivity.this, new LoginFragment());
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
                        getFragmentManager().popBackStack();
                    }
                }
            }

        } else if (v == imgSetting) {
            DashboardFragment dashboardFragment = (DashboardFragment) getFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName());
            if (dashboardFragment != null && dashboardFragment.isVisible()) {
                SettingFragemt settingFragemt = new SettingFragemt();
                Utils.addNextFragmentNoAnim(R.id.container, MainActivity.this, settingFragemt, dashboardFragment);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
