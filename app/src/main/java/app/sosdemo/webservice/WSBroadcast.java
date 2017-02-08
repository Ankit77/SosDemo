package app.sosdemo.webservice;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import app.sosdemo.model.BroadcastModel;

/**
 * Created by indianic on 08/02/17.
 */

public class WSBroadcast {
    private Context context;
    private boolean isSuccess = false;

    public WSBroadcast(final Context context) {
        this.context = context;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Method to execute service and parse response.
     */
    public BroadcastModel executeService() {
        try {
            final String url = WSConstants.BASE_URL + WSConstants.METHOD_BROADCAST;
            String response = new WSUtil().callServiceHttpGet(url);
            response = response.replace(WSConstants.CONST_REPLACE_STRING1, "");
            response = response.replace(WSConstants.CONST_REPLACE_STRING2, "");
            response = response.replace(WSConstants.CONST_REPLACE_STRING3, "");
            return parseResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parse the json response in string.
     *
     * @param response
     */
    private BroadcastModel parseResponse(final String response) {

        if (response != null && response.toString().trim().length() > 0) {
            BroadcastModel broadcastModel = new BroadcastModel();

            try {
                final JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        isSuccess = true;
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);

                        broadcastModel.setBroadcastEN(jsonObject.getString("Message"));
                        broadcastModel.setBroadcastGU(jsonObject.getString("MessageGujarati"));
                        broadcastModel.setBroadcastHI(jsonObject.getString("MessageHindi"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return broadcastModel;
        }
        return null;
    }
}
