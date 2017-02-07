package app.sosdemo.webservice;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import app.sosdemo.util.Constant;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class WSRegister {
    private Context context;
    private boolean isSuccess = false;
    private String message = "";

    public WSRegister(final Context context) {
        this.context = context;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Method to execute service and parse response.
     */
    public boolean executeService(String prefix, String firstname, String middlename, String lastname, String gender, String address, String area, String city, String pincode, String district, String state, String mobile, String landline, String emailaddress, String dob, String macid, String simid) {
        try {
            final String url = WSConstants.BASE_URL + WSConstants.METHOD_REGISTER + generateRequest(prefix, firstname, middlename, lastname, gender, address, area, city, pincode, district, state, mobile, landline, emailaddress, dob, macid, simid);
            String response = new WSUtil().callServiceHttpGet(url);
            response = response.replace(WSConstants.CONST_REPLACE_STRING1, "");
            response = response.replace(WSConstants.CONST_REPLACE_STRING2, "");
            response = response.replace(WSConstants.CONST_REPLACE_STRING3, "");
            return parseResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Parse the json response in string.
     *
     * @param response
     */
    private boolean parseResponse(final String response) {
        if (response != null && response.toString().trim().length() > 0) {
            try {
                final JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        isSuccess = true;
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final int success = jsonObject.getInt("Result");
                        message = jsonObject.getString("Message");
                        if (success == Constant.SUCCESS) {
                            return true;
                        } else {
                            return false;
                        }

                    }
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private String generateRequest(String prefix, String firstname, String middlename, String lastname, String gender, String address, String area, String city, String pincode, String district, String state, String mobile, String landline, String emailaddress, String dob, String macid, String simid) {
        final StringBuilder builder = new StringBuilder();
        builder.append(WSConstants.URL_QUESTION_MARK);
        builder.append(WSConstants.PARAMS_prefix);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(prefix);
        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_FIRSTNAME);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(firstname);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_MIDDLENAME);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(middlename);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_LASTNAME);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(lastname);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_GENDER);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(gender);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_ADDRESS);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(address);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_AREA);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(area);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_CITY);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(city);


        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_PINCODE);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(pincode);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_DISTRICT);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(district);


        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_STATE);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(state);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_MOBILE);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(mobile);


        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_LANDLINE);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(landline);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_EMAILADDRESS);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(emailaddress);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_DOB);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(dob);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_MACID);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(macid);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_SIMID);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(simid);
        return builder.toString();

    }
}
