package app.sosdemo.webservice;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import app.sosdemo.util.Constant;

/**
 * Created by ANKIT on 2/1/2017.
 */

public class WSSOS {
    private Context context;
    private boolean isSuccess = false;
    private int ticket=-1;

    public WSSOS(final Context context) {
        this.context = context;
    }

    public int getTicket() {
        return ticket;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Method to execute service and parse response.
     */
    public boolean executeService(String TripID, String MacID, String SimID, String TimeStamp, String Lat, String Long, String AlertType, String LogStatus) {
        try {
            final String url = WSConstants.BASE_URL + WSConstants.METHOD_SOS + generateRequest(TripID, MacID, SimID, TimeStamp, Lat, Long, AlertType, LogStatus);
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
                        if (success <= 0) {
                            return false;
                        } else {
                            ticket = success;
                            return true;

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


    private String generateRequest(String TripID, String MacID, String SimID, String TimeStamp, String Lat, String Long, String AlertType, String LogStatus) {
        final StringBuilder builder = new StringBuilder();

        builder.append(WSConstants.URL_QUESTION_MARK);
        builder.append(WSConstants.PARAMS_TripID);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(TripID);


        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_MacID);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(MacID);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_SimID);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(SimID);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_TimeStamp);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(TimeStamp);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_Lat);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(Lat);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_Long);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(Long);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_AlertType);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(AlertType);

        builder.append(WSConstants.URL_AND);
        builder.append(WSConstants.PARAMS_LogStatus);
        builder.append(WSConstants.URL_EQUALS_TO);
        builder.append(LogStatus);

        return builder.toString();

    }
}
