package fish.metal.library.States;

import android.os.Build;

import java.util.Date;

import fish.metal.library.Device;

/**
 * Created by Taimur on 7/29/2014.
 */
public class BaseState {

    public BaseState(Date startDate) {

        this(startDate, Device.Phone,  Build.MODEL);

    }

    public BaseState(Date startDate, Device device, String deviceName) {

        Date = startDate;
        DeviceType = device;
        DeviceName = deviceName;

        Type = this.getClass().getSimpleName();
    }

    public Date Date;
    public String DeviceName;
    public Device DeviceType;

    public String Type;

}
