package fish.metal.library.States;

import java.util.Date;

import fish.metal.library.Device;

/**
 * Created by Taimur on 7/26/2014.
 */
public class BatteryState extends BaseState{

    public BatteryState(double level, boolean charging, Date start) {
        super(start);
        Level = level;
        Charging = charging;
    }

    public BatteryState(double level, boolean charging, Date start, Device device, String deviceName) {

        super(start, device, deviceName);
        Level = level;
        Charging = charging;
    }

    public double Level;
    public boolean Charging;
}
