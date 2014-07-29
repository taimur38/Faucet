package fish.fiery.sink.faucet;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MasterService extends Service {
    public MasterService() {
    }

    private static HashMap<Poller, ScheduledFuture> ScheduledTasks = new HashMap<Poller, ScheduledFuture>();
    private static ScheduledExecutorService appScheduler = Executors.newSingleThreadScheduledExecutor();

    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "started tracking service", Toast.LENGTH_LONG).show();

        // initialize all pollers. TODO: use reflection

        AppTracker appTracker = new AppTracker(getApplicationContext());
        ScheduledFuture appFuture = appScheduler.scheduleAtFixedRate(appTracker, appTracker.Delay, appTracker.Interval, appTracker.Units);

        ScheduledTasks.put(appTracker, appFuture);

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
