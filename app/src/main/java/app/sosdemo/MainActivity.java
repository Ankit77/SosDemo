package app.sosdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.sosdemo.fragment.DashboardFragment;
import app.sosdemo.fragment.LoginFragment;
import app.sosdemo.fragment.SettingFragemt;
import app.sosdemo.util.Constant;
import app.sosdemo.util.Utils;
import app.sosdemo.util.WriteLog;
import app.sosdemo.video.ui.VideoRecordingActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;
    private ImageView imgSetting;
    private ImageView imgLogout;
    private TextView tvTitle;
    private RelativeLayout rlmain;

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
        rlmain = (RelativeLayout) findViewById(R.id.activity_main);
        imgBack = (ImageView) findViewById(R.id.activity_main_iv_back);
        imgLogout = (ImageView) findViewById(R.id.activity_main_iv_logout);
        imgSetting = (ImageView) findViewById(R.id.activity_main_iv_setting);
        tvTitle = (TextView) findViewById(R.id.activity_main_tv_title);
        imgBack.setOnClickListener(this);
        imgLogout.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rlmain.setBackgroundResource(R.drawable.sub_bg_h);
        } else {
            rlmain.setBackgroundResource(R.drawable.sub_bg_v);
        }

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
                KavachApp.getInstance().getDatabaseHelper().deleteAllData();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rlmain.setBackgroundResource(R.drawable.sub_bg_v);
        } else {
            rlmain.setBackgroundResource(R.drawable.sub_bg_h);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        WriteLog.E("FILEPATH", "DONE");
        if (requestCode == 2) {
            String filepath = data.getStringExtra("FILEPATH");
            WriteLog.E("FILEPATH", filepath);
            DashboardFragment dashboardFragment = (DashboardFragment) getFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName());
            if (dashboardFragment != null && dashboardFragment.isVisible()) {
                dashboardFragment.startVideoCompress(filepath);
            }
        }
    }

    public void recordVideoActivity(String ticket) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Intent intent = new Intent(MainActivity.this, VideoRecordingActivity.class);
            intent.putExtra("TICKET", ticket);
            startActivityForResult(intent, 2);// Activity is started with requestCode 2
        } else {
            Intent intent = new Intent(MainActivity.this, VideoRecordingActivity.class);
            intent.putExtra("TICKET", ticket);
            startActivityForResult(intent, 2);// Activity is started with requestCode 2
        }
    }

}
