package fish.fiery.sink.faucet.States;

import android.location.Location;

import java.util.Date;

/**
 * Created by Taimur on 8/19/2014.
 */
public class LocationState extends BaseState {

    public LocationState(double lat, double lon, double speed, double accuracy, double alt, Date d) {
        super(d);

        Latitude = lat;
        Longitude = lon;
        Speed = speed;
        Accuracy = accuracy;
        Altitude = alt;

    }

    public double Latitude;
    public double Longitude;
    public double Speed;
    public double Accuracy;
    public double Altitude;


}
