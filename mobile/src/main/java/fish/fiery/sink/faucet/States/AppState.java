package fish.fiery.sink.faucet.States;

import com.google.gson.annotations.Expose;

import java.util.Date;

import fish.fiery.sink.faucet.States.BaseState;

/**
 * Created by Taimur on 7/21/2014.
 */
public class AppState extends BaseState {

    public AppState(String name, Date start, long duration){
        super(start);
        Name = name;
        Duration = duration;
    }

    public String Name;
    public long Duration;

}
