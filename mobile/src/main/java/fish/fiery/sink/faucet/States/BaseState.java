package fish.fiery.sink.faucet.States;

import android.os.Build;

import java.util.Date;

import fish.metal.library.Device;

/**
 * Created by Taimur on 7/29/2014.
 */
public class BaseState {

    public BaseState(Date startDate) {

        StartDate = startDate;
        DeviceName = Build.MODEL;
        DeviceType = Device.Phone;

    }

    public Date StartDate;
    public String DeviceName;
    public Device DeviceType;

}
