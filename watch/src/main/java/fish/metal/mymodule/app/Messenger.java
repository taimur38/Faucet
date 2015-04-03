package fish.metal.mymodule.app;

import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by Taimur on 10/2/2014.
 */
public class Messenger {

    public void SendJson(String json, final GoogleApiClient client) {

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                String json = params[0];
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(client).await();
                for(Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(client, node.getId(), json, null);
                }

                return null;
            }
        }.execute(json);
    }
}
