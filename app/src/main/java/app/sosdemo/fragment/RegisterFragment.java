package app.sosdemo.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.sosdemo.KavachApp;
import app.sosdemo.MainActivity;
import app.sosdemo.R;
import app.sosdemo.util.Utils;
import app.sosdemo.webservice.WSRegister;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText etEmailId;
    private EditText etPrefix;
    private EditText etFirstName;
    private EditText etMiddleName;
    private EditText etLastName;
    private EditText etAddress1;
    private EditText etAddress2;
    private EditText etArea;
    private EditText etCity;
    private EditText etPincode;
    private EditText etDistrict;
    private EditText etState;
    private EditText etLandLine;
    private EditText etMobile;
    private EditText etBirthdate;
    private RadioGroup rgGender;
    private Button btnRegister;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Calendar newCalendar;
    private AsyncRegister asyncRegister;
    private int selectedIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, null);
        dateFormatter = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
        newCalendar = Calendar.getInstance();
        init();
        return view;
    }

    private void init() {
        ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_register));
        ((MainActivity) getActivity()).isshowBackButton(true);
        ((MainActivity) getActivity()).isMenuButton(false);
        etEmailId = (EditText) view.findViewById(R.id.fragnent_register_et_email);
        etPrefix = (EditText) view.findViewById(R.id.fragnent_register_et_prefix);
        etFirstName = (EditText) view.findViewById(R.id.fragnent_register_et_firstname);
        etMiddleName = (EditText) view.findViewById(R.id.fragnent_register_et_middelename);
        etLastName = (EditText) view.findViewById(R.id.fragnent_register_et_lastname);
        etAddress1 = (EditText) view.findViewById(R.id.fragnent_register_et_address1);
        etAddress2 = (EditText) view.findViewById(R.id.fragnent_register_et_address2);
        etArea = (EditText) view.findViewById(R.id.fragnent_register_et_area);
        etCity = (EditText) view.findViewById(R.id.fragnent_register_et_city);
        etPincode = (EditText) view.findViewById(R.id.fragnent_register_et_pincode);
        etDistrict = (EditText) view.findViewById(R.id.fragnent_register_et_district);
        etState = (EditText) view.findViewById(R.id.fragnent_register_et_state);
        etLandLine = (EditText) view.findViewById(R.id.fragnent_register_et_landline);
        etMobile = (EditText) view.findViewById(R.id.fragnent_register_et_mobile);
        etBirthdate = (EditText) view.findViewById(R.id.fragnent_register_et_birthdate);
        btnRegister = (Button) view.findViewById(R.id.fragnent_register_btn_submit);
        radioSexGroup = (RadioGroup) view.findViewById(R.id.fragnent_register_rg_gender);
        btnRegister.setOnClickListener(this);
        etBirthdate.setOnClickListener(this);
        etPrefix.setOnClickListener(this);

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);
                etBirthdate.setText(dateFormatter.format(newCalendar.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    public Dialog onCreateDialogSingleChoice() {

//Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//Source of the data in the DIalog
        final CharSequence[] array = {getString(R.string.lbl_pre_mr), getString(R.string.lbl_reg_miss), getString(R.string.lbl_reg_mrs)};

// Set the dialog title
        builder.setTitle(R.string.lbl_reg_prefix)
// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(array, selectedIndex, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                        selectedIndex = which;
                        etPrefix.setText(array[selectedIndex]);
                    }
                })

// Set the action buttons
                .setPositiveButton(R.string.lbl_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog
                        dialog.dismiss();

                    }
                })
                .setNegativeButton(R.string.lbl_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister) {
            int selectedId = radioSexGroup.getCheckedRadioButtonId();
            radioSexButton = (RadioButton) view.findViewById(selectedId);
            if (isValid()) {
                if (Utils.isNetworkAvailable(getActivity())) {
                    asyncRegister = new AsyncRegister();
                    asyncRegister.execute(etPrefix.getText().toString(),
                            etFirstName.getText().toString(),
                            etMiddleName.getText().toString(),
                            etLastName.getText().toString(),
                            radioSexButton.getTag().toString(),
                            etAddress1.getText().toString() + "," + etAddress2.getText().toString(),
                            etArea.getText().toString(),
                            etCity.getText().toString(),
                            etPincode.getText().toString(),
                            etDistrict.getText().toString(),
                            etState.getText().toString(),
                            etMobile.getText().toString(),
                            etLandLine.getText().toString(),
                            etEmailId.getText().toString(),
                            etBirthdate.getText().toString(),
                            KavachApp.getInstance().getDeviceID(),
                            KavachApp.getInstance().getIMEI()
                    );
                } else {
                    Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_internet));
                }
            }
        } else if (v == etPrefix) {
            Dialog dialog = onCreateDialogSingleChoice();
            dialog.show();
        } else if (v == etBirthdate) {
            datePickerDialog.show();
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etEmailId.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_empty_email));
            return false;
        } else if (!Utils.isValidEmailId(etEmailId.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_valid_email));
            return false;
        } else if (TextUtils.isEmpty(etPrefix.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_prefix));
            return false;
        } else if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_fname));
            return false;
        } else if (TextUtils.isEmpty(etMiddleName.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_mname));
            return false;
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_lname));
            return false;
        } else if (TextUtils.isEmpty(etAddress1.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_addr1));
            return false;
        } else if (TextUtils.isEmpty(etArea.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_area));
            return false;
        } else if (TextUtils.isEmpty(etCity.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_city));
            return false;
        } else if (TextUtils.isEmpty(etPincode.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_pincode));
            return false;
        } else if (TextUtils.isEmpty(etDistrict.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_district));
            return false;
        } else if (TextUtils.isEmpty(etState.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_state));
            return false;
        } else if (TextUtils.isEmpty(etLandLine.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_landline));
            return false;
        } else if (TextUtils.isEmpty(etMobile.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_mobile));
            return false;
        } else if (TextUtils.isEmpty(etBirthdate.getText().toString())) {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_empty_birthday));
            return false;
        }
        return true;

    }

    private class AsyncRegister extends AsyncTask<String, Void, Boolean> {
        WSRegister wsRegister;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity());

        }

        @Override
        protected Boolean doInBackground(String... params) {
            String prefix = params[0];
            String firstname = params[1];
            String middlename = params[2];
            String lastname = params[3];
            String gender = params[4];
            String addres = params[5];
            String area = params[6];
            String city = params[7];
            String pincode = params[8];
            String district = params[9];
            String state = params[10];
            String mobile = params[11];
            String landline = params[12];
            String emailaddress = params[13];
            String dob = params[14];
            String macid = params[15];
            String simid = params[16];
            wsRegister = new WSRegister(getActivity());
            return wsRegister.executeService(prefix, firstname, middlename, lastname, gender, addres, area, city, pincode, district, state, mobile, landline, emailaddress, dob, macid, simid);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (!isCancelled()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (s) {
                    getFragmentManager().popBackStack();
                } else {
                    Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_register_fail));
                }
            }
        }
    }
}
