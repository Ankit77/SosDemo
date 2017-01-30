package app.sosdemo;

import android.app.Application;
import android.location.Location;

/**
 * Created by ANKIT on 1/30/2017.
 */

public class KavachApp extends Application {
    private Location currentLocation;
    private static KavachApp sInstance;

    public static KavachApp getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
