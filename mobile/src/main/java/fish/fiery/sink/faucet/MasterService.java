package fish.fiery.sink.faucet;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
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
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import fish.fiery.sink.faucet.Pollers.AppTracker;
import fish.fiery.sink.faucet.Pollers.Poller;
import fish.fiery.sink.faucet.Pollers.PostPoller;
import fish.fiery.sink.faucet.Receivers.BatteryReceiver;
import fish.fiery.sink.faucet.Receivers.LocationReceiver;
import fish.metal.library.API;

public class MasterService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener {
    public MasterService() {
    }

    private static HashMap<Poller, ScheduledFuture> ScheduledTasks = new HashMap<Poller, ScheduledFuture>();
    private static ScheduledExecutorService appScheduler = Executors.newSingleThreadScheduledExecutor();

    GoogleApiClient client;

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

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                public void onConnectionFailed(ConnectionResult result) {
                        Log.d("API CLIENT", "failed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();

        client.connect();

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
    public void onMessageReceived(MessageEvent messageEvent) {

        String json = messageEvent.getPath();
        Log.d("MESSAGE", json);
        new API().QueueJson(json);
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        String id = peer.getId();
        String name = peer.getDisplayName();

        Log.d("connect", id + " " + name);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(client, this);
        Log.d("API CLIENT", "connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("API CLIENT", "connection suspended");
    }
}
