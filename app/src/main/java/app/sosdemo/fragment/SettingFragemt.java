package app.sosdemo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.sosdemo.MainActivity;
import app.sosdemo.R;
import app.sosdemo.adapter.ContactListAdapter;
import app.sosdemo.adapter.DashboardAdapter;
import app.sosdemo.model.ContactModel;
import app.sosdemo.util.Utils;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class SettingFragemt extends Fragment implements View.OnClickListener {

    private View view;
    private RadioGroup rgLanguage;
    private TextView tvChangePass;
    private RecyclerView rvContactList;
    private ArrayList<ContactModel> contactList;
    private ContactListAdapter contactListAdapter;

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

            }
        });
        tvChangePass = (TextView) view.findViewById(R.id.fragnent_setting_tv_changePass);
        loadContactList();
        tvChangePass.setOnClickListener(this);

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
}
