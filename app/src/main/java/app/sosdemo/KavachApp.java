package app.sosdemo;

import android.app.Application;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;

/**
 * Created by ANKIT on 1/30/2017.
 */

public class KavachApp extends Application {
    private Location currentLocation;
    private static KavachApp sInstance;
    private String IMEI;
    private String DeviceID;
    private String OS;
    private SharedPreferences pref;
    private static final String PREFER_NAME = "AndroidExamplePref";

    public static KavachApp getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        pref = getSharedPreferences(PREFER_NAME, 0);
        OS = String.valueOf(Build.VERSION.SDK_INT);

    }

    public SharedPreferences getPref() {
        return pref;
    }

    public String getOS() {
        return OS;
    }


    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }


    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
