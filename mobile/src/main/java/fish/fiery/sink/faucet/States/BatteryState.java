package fish.fiery.sink.faucet.States;

import java.util.Date;

/**
 * Created by Taimur on 7/26/2014.
 */
public class BatteryState extends BaseState{

    public BatteryState(double level, boolean charging, Date start) {
        super(start);
        Level = level;
        Charging = charging;
    }

    public double Level;
    public boolean Charging;
}
