package fish.metal.mymodule.app;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fish.metal.mymodule.app.Receivers.BatteryReceiver;
import fish.metal.mymodule.app.Receivers.HeartRateReceiver;
/**
 * Created by Taimur on 9/17/2014.
 */
public class WearService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "starting service", Toast.LENGTH_LONG).show();

        final GoogleApiClient client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d("wear API CLIENT", "connected");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d("wear API CLIENT", "connection suspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d("wear API CLIENT", "failed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();

        client.connect();

        registerReceiver(new BatteryReceiver(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        ScheduledExecutorService heartScheduler = Executors.newSingleThreadScheduledExecutor();
        heartScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                Log.d("poll", "running");

                SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
                Sensor heartbeat = sm.getDefaultSensor(Sensor.TYPE_HEART_RATE);

                sm.registerListener(new HeartRateReceiver(sm, client), heartbeat, SensorManager.SENSOR_DELAY_NORMAL);


                //somehow unlisten after getting heart rate.

            }
        }, 0, 5, TimeUnit.MINUTES);


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
