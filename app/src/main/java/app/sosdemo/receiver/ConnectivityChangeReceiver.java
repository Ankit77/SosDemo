package app.sosdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;

import app.sosdemo.KavachApp;
import app.sosdemo.model.FileModel;
import app.sosdemo.service.UpoadFileService;
import app.sosdemo.webservice.WSConstants;


public class ConnectivityChangeReceiver extends BroadcastReceiver {
    private static boolean firstConnect = true;

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo networkInfo = intent
                .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (networkInfo != null) {

            // do subroutines here
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                //get the different network states
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    Log.e(ConnectivityChangeReceiver.class.getSimpleName(), "Connect");
                    ArrayList<FileModel> filelist = KavachApp.getInstance().getDatabaseHelper().getFileList();
                    if (filelist.size() > 0) {
                        for (int i = 0; i < filelist.size(); i++) {
                            Intent intent1 = new Intent(context, UpoadFileService.class);
                            intent1.putExtra("FILEPATH", filelist.get(i).getFilepath());
                            intent1.putExtra("URL", "http://kawach.ilabindia.com/" + WSConstants.METHOD_FILEUPLOAD);
                            intent1.putExtra("AWCODE", filelist.get(i).getAwcode());
                            intent1.putExtra("DATETIME", filelist.get(i).getDatetime());
                            context.startService(intent);
                            KavachApp.getInstance().getDatabaseHelper().deleteSMS(filelist.get(i).getAwcode());
                        }
                    }

                } else {
                    Log.e(ConnectivityChangeReceiver.class.getSimpleName(), "disconnect");
                }
            }


        }

    }


}
