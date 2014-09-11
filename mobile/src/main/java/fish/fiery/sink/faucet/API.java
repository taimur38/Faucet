package fish.fiery.sink.faucet;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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

    static HashSet<JsonPost> Posts = new HashSet<JsonPost>();

    //TODO: Make async
    public void QueueState(Object obj) {

        String json = new Gson().toJson(obj);
        Log.d(TAG, json);

        Posts.add(new JsonPost(json));

        //new JsonPoster().execute(root + "state", json);
    }

    public void PostJson() {

        String json = "{states: [";
        for(JsonPost post : Posts) {
            json += post.Json + ", ";
            post.marked = true;
        }
        json += "]}";

        Log.d(TAG, json);
        new JsonPoster().execute(root + "states", json);

        Log.d(TAG, "POSTED success");

        for(Iterator<JsonPost> i = Posts.iterator(); i.hasNext();) {
            if(i.next().marked)
                i.remove();
        }
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

    class JsonPost {

        String Json;
        boolean marked = false;

        public JsonPost(String json) {
            Json = json;
        }
    }
}
