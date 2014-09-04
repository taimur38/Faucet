package fish.fiery.sink.faucet;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import fish.fiery.sink.faucet.States.AppState;
import fish.fiery.sink.faucet.States.BatteryState;
import fish.fiery.sink.faucet.States.ConnectivityState;
import fish.fiery.sink.faucet.States.LocationState;

/**
 * Created by Taimur on 7/26/2014.
 */
public class API {

    static String TAG = "API";
    static String root = "http://sink.metal.fish/faucet/";

    //TODO: Make async
    private void PostJson(String url, Object obj) {

        String json = new Gson().toJson(obj);
        new JsonPoster().execute(url, json);

    }

    public void PostBatteryState(BatteryState state) {
        String url = root + "battery";
        PostJson(url, state);
    }

    public void PostLocationState(LocationState state) {
        String url = root + "location";
        PostJson(url, state);
    }
    
    public void PostAppState(AppState state) {
        String url = root + "app";
        PostJson(url, state);
    }

    public void PostConnectivityState(ConnectivityState state) {
        String url = root + "conn";
        PostJson(url, state);
    }

    class JsonPoster extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String json = strings[1];

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new ByteArrayEntity(json.getBytes()));

            try {
                HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());

                Log.d(TAG, result);
                return result;
            } catch(Exception ex) {
                Log.d(TAG, url + " failure: " + ex.getMessage());
                return ex.getMessage();
            }
        }
    }
}
