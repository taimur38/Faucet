package fish.fiery.sink.faucet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Taimur on 7/27/2014.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent app_tracker_intent = new Intent(context, MasterService.class);
        context.startService(app_tracker_intent);


    }
}
