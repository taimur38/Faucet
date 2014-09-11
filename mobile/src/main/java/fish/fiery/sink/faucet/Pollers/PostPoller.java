package fish.fiery.sink.faucet.Pollers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import fish.fiery.sink.faucet.API;

/**
 * Created by Taimur on 9/5/2014.
 */
public class PostPoller extends Poller {

    public PostPoller(Context context) {
        super(context, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void run() {

       ConnectivityManager cm = (ConnectivityManager) _Context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Log.d("POLLER", "RUNNING");
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            Log.d("POLLER", "connected");
            new API().PostJson();
        }
        else
            Log.d("POLLER", "errorz");

    }
}
