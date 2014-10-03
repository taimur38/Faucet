package fish.metal.mymodule.app.Receivers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

import fish.metal.library.Device;
import fish.metal.library.States.HeartState;

/**
 * Created by Taimur on 9/17/2014.
 */
public class HeartRateReceiver implements SensorEventListener {

    private SensorManager _manager;
    private int total;
    private int count = 0;
    private final int MAX = 10;
    private GoogleApiClient _client;

    public HeartRateReceiver(SensorManager sm, GoogleApiClient client)
    {
        _manager = sm;
        _client = client;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.accuracy < 2)
            return;

        if(count++ < MAX)
            total += (int)sensorEvent.values[0];
        else
        {
            double bpm = ((double)total)/MAX;

            Log.d("average bpm", "" + bpm);

            Date date = Calendar.getInstance().getTime();
            HeartState state = new HeartState(bpm, date, Build.MODEL);
            String json = new Gson().toJson(state);
            //send it to phone

            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... params) {
                    String json = params[0];
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(_client).await(); //TODO: abstract this
                    for(Node node : nodes.getNodes()) {
                        MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(_client, node.getId(), json, null).await();
                        Log.d("message", result.toString());
                    }

                    return null;
                }
            }.execute(json);



            _manager.unregisterListener(this);
        }
    }
}
