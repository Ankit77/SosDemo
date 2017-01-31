package app.sosdemo.webservice;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.sosdemo.model.ActionModel;
import app.sosdemo.util.Constant;

/**
 * Created by ANKIT on 2/1/2017.
 */

public class WSGetAlertList {
    private Context context;
    private boolean isSuccess = false;

    public WSGetAlertList(final Context context) {
        this.context = context;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Method to execute service and parse response.
     */
    public ArrayList<ActionModel> executeService() {
        try {
            final String url = WSConstants.BASE_URL + WSConstants.METHOD_ALERTTYPELIST;
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
    private ArrayList<ActionModel> parseResponse(final String response) {

        if (response != null && response.toString().trim().length() > 0) {
            ArrayList<ActionModel> actionList = new ArrayList<>();
            try {
                final JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        isSuccess = true;
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ActionModel actionModel = new ActionModel();
                        actionModel.setCode(jsonObject.getString("pkID"));
                        actionModel.setAction(jsonObject.getString("FileMode"));
                        actionModel.setCaption(jsonObject.getString("AlertType"));
                        actionList.add(actionModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return actionList;
        }
        return null;
    }
}
