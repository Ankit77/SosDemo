package app.sosdemo.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.sosdemo.KavachApp;
import app.sosdemo.MainActivity;
import app.sosdemo.R;
import app.sosdemo.adapter.ContactListAdapter;
import app.sosdemo.adapter.DashboardAdapter;
import app.sosdemo.model.ContactModel;
import app.sosdemo.util.Constant;
import app.sosdemo.util.Utils;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class SettingFragemt extends Fragment implements View.OnClickListener, ContactListAdapter.onCallListner {

    private View view;
    private RadioGroup rgLanguage;
    private TextView tvChangePass;
    private RecyclerView rvContactList;
    private ArrayList<ContactModel> contactList;
    private ContactListAdapter contactListAdapter;
    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
    int PERMISSION_ALL = 1;
    private String mPhoneNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, null);
        init();
        return view;
    }

    private void init() {
        ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_settings));
        ((MainActivity) getActivity()).isshowBackButton(true);
        ((MainActivity) getActivity()).isMenuButton(false);
        rgLanguage = (RadioGroup) view.findViewById(R.id.fragnent_setting_rg_language);
        rvContactList = (RecyclerView) view.findViewById(R.id.fragnent_setting_rv_contactlist);
        rgLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = KavachApp.getInstance().getPref().edit();
                if (checkedId == R.id.fragnent_setting_rb_english) {
                    Utils.setLanguage(getActivity(), Constant.LANGUAGE_ENGLISH);
                    editor.putString(Constant.PREF_LANGUAGE, Constant.LANGUAGE_ENGLISH);
                } else if (checkedId == R.id.fragnent_setting_rb_hindi) {
                    Utils.setLanguage(getActivity(), Constant.LANGUAGE_HINDI);
                    editor.putString(Constant.PREF_LANGUAGE, Constant.LANGUAGE_HINDI);
                } else if (checkedId == R.id.fragnent_setting_rb_gujarati) {
                    Utils.setLanguage(getActivity(), Constant.LANGUAGE_GUJRATI);
                    editor.putString(Constant.PREF_LANGUAGE, Constant.LANGUAGE_GUJRATI);
                }
                editor.commit();
                resetActivity();


            }
        });
        tvChangePass = (TextView) view.findViewById(R.id.fragnent_setting_tv_changePass);
        loadContactList();
        tvChangePass.setOnClickListener(this);

    }

    private void resetActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_settings));
            ((MainActivity) getActivity()).isshowBackButton(true);
            ((MainActivity) getActivity()).isMenuButton(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvChangePass) {
            ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
            Utils.addNextFragmentNoAnim(R.id.container, getActivity(), changePasswordFragment, SettingFragemt.this);
        }
    }

    private void loadContactList() {
        loadContactData();
        contactListAdapter = new ContactListAdapter(getActivity(), contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvContactList.setLayoutManager(mLayoutManager);
        contactListAdapter.setOnCallListner(this);
        rvContactList.setAdapter(contactListAdapter);
    }

    private void loadContactData() {
        contactList = new ArrayList<>();
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
        contactList.add(new ContactModel("My Office", "Prahalad Nagar,Ahmedabad", "079-1234567", "Mrunalbhai", "7565747483", "mrunal@gmail.com"));
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Requests the write permission.
     * If the permission has been denied previously, a alert will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestPermission() {
        // permission has not been granted yet. Request it directly.
        FragmentCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_ALL) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for write permission.
            // Check if the only required permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // write permission has been granted
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + mPhoneNumber));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                getActivity().startActivity(intent);

            } else {
                Toast.makeText(getActivity(), R.string.alert_permision_callphone, Toast.LENGTH_LONG).show();
            }
            // END_INCLUDE(permission_result)

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(String phonenumber) {
        mPhoneNumber = phonenumber;
        if (hasPermissions(getActivity(), PERMISSIONS)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phonenumber));
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            getActivity().startActivity(intent);
        }
    }
}
