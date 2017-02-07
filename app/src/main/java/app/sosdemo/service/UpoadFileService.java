package app.sosdemo.service;

import android.app.IntentService;
import android.content.Intent;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
                boolean issuccess = parseResponse(res);
                if (issuccess) {
                    File file = new File(filepath);
                    if (file.exists()) {
                        file.delete();
                    }
                }

                //doFileUpload(filepath, new File(filepath).getName());
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


    public void doFileUpload(String filepath, String fileName) {
        // TODO Auto-generated method stub

        String hostName = "kawach.ilabindia.com";
        String username = "administrator";
        String password = "ilab@257";
        String location = filepath;
        FTPClient ftp = null;

        InputStream in = null;
        try {
            ftp = new FTPClient();
            ftp.connect(hostName);
            ftp.login(username, password);

            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            ftp.changeWorkingDirectory("/MediaStorage");
            // tripid_deviceid
            int reply = ftp.getReplyCode();
            System.out.println("Received Reply from FTP Connection:" + reply);

            if (FTPReply.isPositiveCompletion(reply)) {
                System.out.println("Connected Success");
            }

            File f1 = new File(location);
            in = new FileInputStream(f1);

            ftp.storeFile(fileName, in);

            System.out.println("SUCCESS");

            ftp.logout();
            ftp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
