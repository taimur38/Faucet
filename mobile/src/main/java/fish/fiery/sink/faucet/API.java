package fish.fiery.sink.faucet;

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

/**
 * Created by Taimur on 7/26/2014.
 */
public class API {

    static HttpClient client = new DefaultHttpClient();
    static String TAG = "API";
    static String root = "http://sink.metal.fish/faucet/";

    //TODO: Make async
    private static void PostJson(String url, Object obj) {

        HttpPost post = new HttpPost(url);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(obj);
        post.setEntity(new ByteArrayEntity(json.getBytes()));

        try {
            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());

            Log.d(TAG, result);
        } catch(Exception ex) {
            Log.d(TAG, url + " failure: " + ex.getMessage());
        }
    }

    public static void PostBatteryState(BatteryState state) {
        String url = root + "battery";
        PostJson(url, state);
    }
    
    public static void PostAppState(AppState state) {
        String url = root + "app";
        PostJson(url, state);
    }

    public static void PostConnectivityState(ConnectivityState state) {
        String url = root + "conn";
        PostJson(url, state);
    }

}
