package app.sosdemo.webservice;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.sosdemo.model.ActionModel;
import app.sosdemo.model.ContactModel;

/**
 * Created by ANKIT on 2/1/2017.
 */

public class WSContactList {
    private Context context;
    private boolean isSuccess = false;

    public WSContactList(final Context context) {
        this.context = context;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Method to execute service and parse response.
     */
    public ArrayList<ContactModel> executeService() {
        try {
            final String url = WSConstants.BASE_URL + WSConstants.METHOD_EMERGENCY;
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
    private ArrayList<ContactModel> parseResponse(final String response) {

        if (response != null && response.toString().trim().length() > 0) {
            ArrayList<ContactModel> contactList = new ArrayList<>();
            try {
                final JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        isSuccess = true;
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ContactModel contactModel = new ContactModel();
                        contactModel.setOrgName(jsonObject.getString("OrgName"));
                        contactModel.setAddress(jsonObject.getString("Address"));
                        contactModel.setLandline1(jsonObject.getString("Landline1"));
                        contactModel.setLandline2(jsonObject.getString("Landline2"));
                        contactModel.setFax1(jsonObject.getString("Fax1"));
                        contactModel.setEmailAddress(jsonObject.getString("EmailAddress"));
                        contactList.add(contactModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return contactList;
        }
        return null;
    }
}
