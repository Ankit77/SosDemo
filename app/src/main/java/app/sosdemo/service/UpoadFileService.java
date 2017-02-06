package app.sosdemo.service;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

import app.sosdemo.webservice.WSUploadPhoto;

/**
 * Created by indianic on 06/02/17.
 */

public class UpoadFileService extends IntentService {

    public UpoadFileService() {
        super(UpoadFileService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                String filepath = intent.getExtras().getString("FILEPATH");
                String url = intent.getExtras().getString("URL");
                String awcode = intent.getExtras().getString("AWCODE");
                String datetime = intent.getExtras().getString("DATETIME");
                WSUploadPhoto wsUploadPhoto = new WSUploadPhoto(url, awcode, datetime);
                final FileInputStream fstrm = new FileInputStream(filepath);
                String res = wsUploadPhoto.Send_Now(fstrm, new File(filepath).getName());
                parseResponse(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean parseResponse(String response) {

        if (response != null && response.toString().trim().length() > 0) {
            try {
                final JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final int success = jsonObject.getInt("Result");
                        if (success <= 0) {
                            return false;
                        } else {
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
}
