package fish.fiery.sink.faucet;

import android.content.Context;

import java.util.concurrent.TimeUnit;

/**
 * Created by Taimur on 7/29/2014.
 */
public abstract class Poller implements Runnable{

    private boolean _paused;

    public Poller(Context context, long delay, long interval, TimeUnit units) {

        _Context = context;
        Delay = delay;
        Interval = interval;
        Units = units;

    }

    public final void Pause() {
        if(!_paused)
            onPause();
        _paused = true;
    }

    public final void Resume() {
        if(_paused)
            onResume();
        _paused = false;
    }

    //Optional methods to override
    protected void onPause() {

    }
    protected void onResume() {

    }

    protected Context _Context;
    public long Delay;
    public long Interval;
    public TimeUnit Units;

}
