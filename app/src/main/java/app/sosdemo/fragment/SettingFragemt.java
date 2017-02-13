package app.sosdemo.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import app.sosdemo.KavachApp;
import app.sosdemo.MainActivity;
import app.sosdemo.R;
import app.sosdemo.adapter.ContactListAdapter;
import app.sosdemo.common.ExifUtil;
import app.sosdemo.model.ContactModel;
import app.sosdemo.util.Constant;
import app.sosdemo.util.GetFilePath;
import app.sosdemo.util.Utils;
import app.sosdemo.webservice.WSContactList;
import app.sosdemo.webservice.WSUploadProfile;

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
    private AsyncContactList asyncContactList;
    private TextView tvContact;
    private final int REQUEST_CAPTURE_IMAGE = 111;
    private String cameraFilePath;
    private ImageView imgImageUser;
    private ImageView imgImageEdit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, null);
        init();
//        if (Utils.isNetworkAvailable(getActivity())) {
//            asyncContactList = new AsyncContactList();
//            asyncContactList.execute();
//        } else {
//            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_internet));
//        }
        return view;
    }

    private void init() {
        ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_settings));
        ((MainActivity) getActivity()).isshowBackButton(true);
        ((MainActivity) getActivity()).isMenuButton(false);
        imgImageUser = (ImageView) view.findViewById(R.id.fragment_setting_imgUser);
        imgImageEdit = (ImageView) view.findViewById(R.id.fragment_setting_imgEdit);
        tvContact = (TextView) view.findViewById(R.id.fragment_setting_tvContact);
        rgLanguage = (RadioGroup) view.findViewById(R.id.fragnent_setting_rg_language);
        rvContactList = (RecyclerView) view.findViewById(R.id.fragnent_setting_rv_contactlist);
        if (KavachApp.getInstance().getPref().getString(Constant.PREF_LANGUAGE, Constant.LANGUAGE_ENGLISH).equalsIgnoreCase(Constant.LANGUAGE_ENGLISH)) {
            ((RadioButton) rgLanguage.getChildAt(0)).setChecked(true);
        } else if (KavachApp.getInstance().getPref().getString(Constant.PREF_LANGUAGE, Constant.LANGUAGE_ENGLISH).equalsIgnoreCase(Constant.LANGUAGE_HINDI)) {
            ((RadioButton) rgLanguage.getChildAt(1)).setChecked(true);
        } else if (KavachApp.getInstance().getPref().getString(Constant.PREF_LANGUAGE, Constant.LANGUAGE_ENGLISH).equalsIgnoreCase(Constant.LANGUAGE_GUJRATI)) {
            ((RadioButton) rgLanguage.getChildAt(2)).setChecked(true);
        } else {
            ((RadioButton) rgLanguage.getChildAt(0)).setChecked(true);
        }

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

        tvChangePass.setOnClickListener(this);
        tvContact.setOnClickListener(this);
        imgImageEdit.setOnClickListener(this);

        Glide.with(getActivity()).load("http://api.androidhive.info/images/glide/medium/deadpool.jpg")
                .thumbnail(0.5f).placeholder(R.drawable.ic_user)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgImageUser);

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

    /**
     * Opens camera activity
     */

    private void captureImage() {
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {

            Uri outputFileUri = getPostImageUri(true);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent1.putExtra("return-data", true);
            startActivityForResult(intent1, REQUEST_CAPTURE_IMAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets Uri of image to capture. An External Cache Directory is created and
     * a file for current_post is created in it, this method Uri for it.
     *
     * @param canCleanup : Can delete the file if already exists
     * @return : Generated Uri.
     */
    private Uri getPostImageUri(boolean canCleanup) {
        final File file = new File(getActivity().getExternalCacheDir() + File.separator + System.currentTimeMillis() + Constant.IMAGE_EXTENSION);
        if (canCleanup) {
            if (file.exists()) {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cameraFilePath = file.getAbsolutePath();
        return Uri.fromFile(file);
    }

    @Override
    public void onClick(View v) {
        if (v == tvChangePass) {
            ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
            Utils.addNextFragmentNoAnim(R.id.container, getActivity(), changePasswordFragment, SettingFragemt.this);
        } else if (v == tvContact) {
            ContactListFragment contactListFragment = new ContactListFragment();
            // Show Alert DialogFragment
//            alertdFragment.show(getFragmentManager(), getString(R.string.lbl_contactlist));
//            ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
            Utils.addNextFragmentNoAnim(R.id.container, getActivity(), contactListFragment, SettingFragemt.this);
        } else if (v == imgImageEdit) {
            captureImage();
        }
    }

    private void loadContactList() {
        contactListAdapter = new ContactListAdapter(getActivity(), contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvContactList.setLayoutManager(mLayoutManager);
        contactListAdapter.setOnCallListner(this);
        rvContactList.setAdapter(contactListAdapter);
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

    private class AsyncContactList extends AsyncTask<Void, Void, ArrayList<ContactModel>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity());
        }

        @Override
        protected ArrayList<ContactModel> doInBackground(Void... params) {
            WSContactList wsContactList = new WSContactList(getActivity());
            return wsContactList.executeService();
        }

        @Override
        protected void onPostExecute(ArrayList<ContactModel> contactModels) {
            super.onPostExecute(contactModels);
            if (!isCancelled()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (contactModels != null && contactModels.size() > 0) {
                    contactList = new ArrayList<>();
                    contactList.addAll(contactModels);
                    loadContactList();
                }


            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == Activity.RESULT_OK) {
                // Callback from Gallery
                if (requestCode == REQUEST_CAPTURE_IMAGE) {
                    try {
                        if (!TextUtils.isEmpty(cameraFilePath)) {
                            Bitmap bitmap = null;
                            final Uri uri = Uri.fromFile(new File(cameraFilePath));
                            String filePath = GetFilePath.getPath(getActivity(), uri);
                            if (!android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                                imgImageUser.setImageURI(uri);
                                bitmap = BitmapFactory.decodeFile(filePath);
                            } else {

//                            if (!TextUtils.isEmpty(filePath)) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 8;
                                Bitmap local = BitmapFactory.decodeStream(new FileInputStream(filePath), null, options);
                                local = ExifUtil.rotateBitmap(filePath, local);
                                imgImageUser.setImageBitmap(local);
                                bitmap = local;


                            }
                            if (bitmap != null) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                                byte[] b = baos.toByteArray();
                                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                                if (Utils.isNetworkAvailable(getActivity())) {
                                    AsyncUploadProfile asyncUploadProfile = new AsyncUploadProfile();
                                    asyncUploadProfile.execute(KavachApp.getInstance().getDeviceID(), KavachApp.getInstance().getIMEI(), encodedImage);
                                } else {
                                    Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_internet));
                                }

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AsyncUploadProfile extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progressDialog;
        private WSUploadProfile wsUploadProfile;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity());
        }


        @Override
        protected Boolean doInBackground(String... strings) {
            wsUploadProfile = new WSUploadProfile(getActivity());
            return wsUploadProfile.executeService(strings[0], strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!isCancelled()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (aBoolean) {
                    Utils.displayDialog(getActivity(), getString(R.string.app_name), wsUploadProfile.getMessage());
                } else {
                    Utils.displayDialog(getActivity(), getString(R.string.app_name), wsUploadProfile.getMessage());
                }
            }
        }
    }
}
