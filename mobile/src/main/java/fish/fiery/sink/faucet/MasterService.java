package fish.fiery.sink.faucet;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import fish.fiery.sink.faucet.Pollers.AppTracker;
import fish.fiery.sink.faucet.Pollers.Poller;
import fish.fiery.sink.faucet.Pollers.PostPoller;
import fish.fiery.sink.faucet.Receivers.BatteryReceiver;
import fish.fiery.sink.faucet.Receivers.LocationReceiver;

public class MasterService extends Service {
    public MasterService() {
    }

    private static HashMap<Poller, ScheduledFuture> ScheduledTasks = new HashMap<Poller, ScheduledFuture>();
    private static ScheduledExecutorService appScheduler = Executors.newSingleThreadScheduledExecutor();

    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "started tracking service", Toast.LENGTH_LONG).show();
        Log.d("MASTER", "started shit");

        // TODO: use reflection to initialize all pollers.

        AppTracker appTracker = new AppTracker(getApplicationContext());
        ScheduledFuture appFuture = appScheduler.scheduleAtFixedRate(appTracker, appTracker.Delay, appTracker.Interval, appTracker.Units);
        PostPoller poller = new PostPoller(getApplicationContext());

        ScheduledExecutorService pollerScheduler = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture pollerFuture = pollerScheduler.scheduleAtFixedRate(poller, poller.Delay, poller.Interval, poller.Units);

        ScheduledTasks.put(appTracker, appFuture);

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationReceiver locationReceiver = new LocationReceiver();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationReceiver);

        registerReceiver(new BatteryReceiver(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                for (Poller poller : ScheduledTasks.keySet()) {
                    poller.Pause();
                    ScheduledTasks.get(poller).cancel(true);
                }
            }
        }, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                for(Poller poller : ScheduledTasks.keySet()) {
                    poller.Resume();
                    appScheduler.scheduleAtFixedRate(poller, poller.Delay, poller.Interval, poller.Units);
                }
            }
        }, new IntentFilter(Intent.ACTION_SCREEN_ON));

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
