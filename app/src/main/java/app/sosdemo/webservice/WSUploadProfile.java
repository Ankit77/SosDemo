package app.sosdemo.webservice;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import app.sosdemo.util.Constant;

/**
 * Created by indianic on 13/02/17.
 */

public class WSUploadProfile {
    private Context context;
    private boolean isSuccess = false;
    private String message = "";

    public WSUploadProfile(final Context context) {
        this.context = context;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Method to execute service and parse response.
     */
    public boolean executeService(final String macid, final String simid, final String base64String) {
        try {
            final String url = WSConstants.BASE_URL + WSConstants.METHOD_PROFILE + generateRequest(macid, simid, base64String);
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


    private String generateRequest(final String macid, final String simid, String base64String) {
        final StringBuilder builder = new StringBuilder();
        builder.append(WSConstants.URL_QUESTION_MARK);
        builder.append(WSConstants.PARAMS_MACID);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(macid);
        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_SIMID);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(simid);
        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_BASE64);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(base64String);
        return builder.toString();

    }

}
