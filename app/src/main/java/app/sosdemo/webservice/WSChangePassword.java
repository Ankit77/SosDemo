package app.sosdemo.webservice;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import app.sosdemo.util.Constant;

/**
 * Created by ANKIT on 2/1/2017.
 */

public class WSChangePassword {

    private Context context;
    private boolean isSuccess = false;
    private String message;

    public WSChangePassword(final Context context) {
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
    public boolean executeService(String userid, String oldpassword, String newpassword) {
        try {
            final String url = WSConstants.BASE_URL + WSConstants.METHOD_CHANGEPASSWORD + generateRequest(userid, oldpassword, newpassword);
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


    private String generateRequest(String userid, String oldpassword, String newpassword) {
        final StringBuilder builder = new StringBuilder();
        builder.append(WSConstants.URL_QUESTION_MARK);
        builder.append(WSConstants.PARAMS_USERID);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(userid);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_OLDPASSWORD);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(oldpassword);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_NEWPASSWORD);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(newpassword);
        return builder.toString();

    }

}
