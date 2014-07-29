package fish.fiery.sink.faucet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import java.util.Calendar;

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

        boolean charging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == 0;

        BatteryState state = new BatteryState(pct, charging, Calendar.getInstance().getTime());

        API.PostBatteryState(state);
    }
}
