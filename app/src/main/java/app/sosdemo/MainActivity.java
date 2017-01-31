package app.sosdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.sosdemo.fragment.DashboardFragment;
import app.sosdemo.fragment.LoginFragment;
import app.sosdemo.fragment.SettingFragemt;
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
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, loginFragment, DashboardFragment.class.getSimpleName());
        fragmentTransaction.commit();

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
            imgLogout.setVisibility(View.VISIBLE);
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

                Toast.makeText(MainActivity.this, "Logout Call", Toast.LENGTH_LONG).show();
            }

        } else if (v == imgSetting) {
            DashboardFragment dashboardFragment = (DashboardFragment) getFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName());
            if (dashboardFragment != null && dashboardFragment.isVisible()) {
                SettingFragemt settingFragemt = new SettingFragemt();
                Utils.addNextFragmentNoAnim(R.id.container, MainActivity.this, settingFragemt, dashboardFragment);
                Toast.makeText(MainActivity.this, "Logout Call", Toast.LENGTH_LONG).show();
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
