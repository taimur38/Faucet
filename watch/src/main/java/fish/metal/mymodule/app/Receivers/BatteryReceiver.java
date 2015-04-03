package fish.metal.mymodule.app.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.util.Calendar;

import fish.metal.library.API;
import fish.metal.library.Device;
import fish.metal.library.States.BatteryState;
import fish.metal.mymodule.app.Messenger;

/**
 * Created by Taimur on 9/18/2014.
 */
public class BatteryReceiver extends BroadcastReceiver {

    private double prev_pct;
    private boolean prev_charge_data;
    private GoogleApiClient _client;

    public BatteryReceiver(GoogleApiClient client) {
        _client = client;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int max = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        double pct = 0;

        if(max >= 0 && level >= 0)
            pct = (level * 100d) / max;

        int charging_data = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean charging = (charging_data == BatteryManager.BATTERY_PLUGGED_AC) || (charging_data == BatteryManager.BATTERY_PLUGGED_USB) || (charging_data == BatteryManager.BATTERY_PLUGGED_WIRELESS);

        if(pct == prev_pct && prev_charge_data == charging)
            return;

        prev_charge_data = charging;
        prev_pct = pct;

        BatteryState state = new BatteryState(pct, charging, Calendar.getInstance().getTime(), Device.Watch, Build.MODEL);
        String json = new Gson().toJson(state);

        new Messenger().SendJson(json, _client);
    }
}
