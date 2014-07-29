package fish.fiery.sink.faucet.States;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by Taimur on 7/29/2014.
 */
public class BaseState {

    public BaseState(Date startDate) {
        StartDate = startDate;
    }

    @Expose public Date StartDate;
}
