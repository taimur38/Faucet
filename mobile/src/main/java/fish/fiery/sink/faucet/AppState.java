package fish.fiery.sink.faucet;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by Taimur on 7/21/2014.
 */
public class AppState extends BaseState {

    public AppState(String name, Date start, long duration){
        super(start);
        Name = name;
        Duration = duration;
    }

    @Expose
    public String Name;
    @Expose
    public long Duration;

}
