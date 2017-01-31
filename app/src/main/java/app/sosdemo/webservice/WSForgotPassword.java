package app.sosdemo.webservice;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import app.sosdemo.util.Constant;

/**
 * Created by ANKIT on 2/1/2017.
 */

public class WSForgotPassword {
    private Context context;
    private boolean isSuccess = false;

    public WSForgotPassword(final Context context) {
        this.context = context;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Method to execute service and parse response.
     */
    public boolean executeService(String macid, String simid) {
        try {
            final String url = WSConstants.BASE_URL + WSConstants.METHOD_FORGOTPASSWORD + generateRequest(macid, simid);
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


    private String generateRequest(String macid, String simid) {
        final StringBuilder builder = new StringBuilder();
        builder.append(WSConstants.URL_QUESTION_MARK);
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
