package fish.fiery.sink.faucet.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import java.util.Calendar;

import fish.fiery.sink.faucet.API;
import fish.fiery.sink.faucet.States.BatteryState;

/**
 * Created by Taimur on 7/26/2014.
 */
public class BatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int max = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        double pct = 0;

        if(max >= 0 && level >= 0)
            pct = (level * 100d) / max;

        int charging_data = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        boolean charging = (charging_data == BatteryManager.BATTERY_PLUGGED_AC) || (charging_data == BatteryManager.BATTERY_PLUGGED_USB) || (charging_data == BatteryManager.BATTERY_PLUGGED_WIRELESS);
        
        BatteryState state = new BatteryState(pct, charging, Calendar.getInstance().getTime());

        new API().PostBatteryState(state);
    }
}
