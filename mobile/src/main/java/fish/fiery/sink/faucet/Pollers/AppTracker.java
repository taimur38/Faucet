package fish.fiery.sink.faucet.Pollers;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import fish.fiery.sink.faucet.API;
import fish.fiery.sink.faucet.States.AppState;

/**
 * Created by Taimur on 7/27/2014.
 */
public class AppTracker extends Poller {

    public AppTracker(Context context) {
        super(context, 0, 1, TimeUnit.SECONDS);

        _start = Calendar.getInstance().getTime();
        _previous = getForegroundTask();
    }

    private String _previous;
    private Date _start;

    @Override
    public void run() {
        String current = getForegroundTask();

        if(_previous.equals(current))
            return;

        AppEnd();
        _previous = current;

    }

    public void AppEnd()
    {
        Date now = Calendar.getInstance().getTime();
        new API().PostAppState(new AppState(_previous, _start, now.getTime() - _start.getTime()));        //TODO: make these async
        _start = now;
    }

    @Override
    protected void onPause() {

        AppEnd();
        _previous = "SCREEN OFF";
    }

    @Override
    protected void onResume() {
        AppEnd();
        _previous = getForegroundTask();
    }

    public String getForegroundTask()
    {
        try {
            ActivityManager am = (ActivityManager) _Context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo foregroundTasks = am.getRunningTasks(1).get(0);
            //PackageManager pm = _Context.getPackageManager();      TODO: get app label

            return foregroundTasks.topActivity.getPackageName();
        }
        catch(Exception ex)
        {
            Toast.makeText(_Context, ex.getMessage(), Toast.LENGTH_LONG).show();
            return "FAILURE";
        }
    }
}
