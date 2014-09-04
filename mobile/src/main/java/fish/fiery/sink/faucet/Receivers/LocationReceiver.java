package fish.fiery.sink.faucet.Receivers;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

import fish.fiery.sink.faucet.API;
import fish.fiery.sink.faucet.States.LocationState;

/**
 * Created by Taimur on 9/2/2014.
 */
public class LocationReceiver implements LocationListener {

    private Location _prevLocation;
    private Date _prevDate;
    private final long TIME_THRESHOLD = 1000 * 60 * 10;
    private final double ACCURACY_THRESHOLD = 1000; //meters of overlap allowed

    public LocationReceiver() {
        _prevLocation = null;
        _prevDate = Calendar.getInstance().getTime();
    }

    @Override
    public void onLocationChanged(Location location) {

        Date curDate = Calendar.getInstance().getTime();

        if(_prevLocation == null || curDate.getTime() - _prevDate.getTime() > TIME_THRESHOLD) {
            Save(location, curDate);
            return;
        }

        if(location.getAccuracy() <= _prevLocation.getAccuracy()) {
            Save(location, curDate);
            return;
        }

        if(location.distanceTo(_prevLocation) > _prevLocation.getAccuracy() + location.getAccuracy() - ACCURACY_THRESHOLD) {
            Save(location, curDate);
            return;
        }

        //otherwise, location is very recent, less accurate and not a significant distance away.
    }

    public void Save(Location location, Date date) {

        LocationState state = new LocationState(location.getLatitude(), location.getLongitude(), location.getSpeed(), location.getAccuracy(), location.getAltitude(), date);
        new API().PostLocationState(state);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
