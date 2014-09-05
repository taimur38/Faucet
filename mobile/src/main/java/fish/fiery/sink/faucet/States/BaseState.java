package fish.fiery.sink.faucet.States;

import android.os.Build;

import java.util.Date;

import fish.fiery.sink.faucet.Device;

/**
 * Created by Taimur on 7/29/2014.
 */
public class BaseState {

    public BaseState(Date startDate) {

        Date = startDate;
        DeviceName = Build.MODEL;
        DeviceType = Device.Phone;

    }

    public Date Date;
    public String DeviceName;
    public Device DeviceType;

}
