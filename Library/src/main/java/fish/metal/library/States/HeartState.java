package fish.metal.library.States;

import java.util.Date;

import fish.metal.library.Device;

/**
 * Created by Taimur on 9/18/2014.
 */
public class HeartState extends BaseState {

    public HeartState(double bpm, Date startDate, String deviceType) {

        super(startDate, Device.Watch, deviceType);
        BPM = bpm;

    }

    public double BPM;
}
