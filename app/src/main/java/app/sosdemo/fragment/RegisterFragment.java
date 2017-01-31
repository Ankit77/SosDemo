package app.sosdemo.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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

import app.sosdemo.MainActivity;
import app.sosdemo.R;
import app.sosdemo.util.Utils;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, null);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
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

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);
                etBirthdate.setText(dateFormatter.format(newCalendar.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister) {
            int selectedId = radioSexGroup.getCheckedRadioButtonId();
            radioSexButton = (RadioButton) view.findViewById(selectedId);
            if (isValid()) {
                LoginFragment loginFragment = new LoginFragment();
                Utils.addNextFragmentNoAnim(R.id.container, getActivity(), loginFragment, RegisterFragment.this);
            }
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
}
