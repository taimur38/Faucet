package fish.fiery.sink.faucet.States;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by Taimur on 7/25/2014.
 */
public class ConnectivityState extends BaseState{

    public ConnectivityState(String netType, boolean wifiConn, boolean wifiAvail, Date start) {
        super(start);

        NetworkType = netType;
        WiFiConn = wifiConn;
        WiFiAvailable = wifiAvail;
    }


    public String NetworkType;
    public boolean WiFiConn;
    public boolean WiFiAvailable;
}
