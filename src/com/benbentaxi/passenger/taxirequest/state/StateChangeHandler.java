package com.benbentaxi.passenger.taxirequest.state;

import android.os.Handler;

import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;

public interface StateChangeHandler {
	public void handler(DemoApplication app,Handler handler,TaxiRequest old,TaxiRequestResponse newState);
}
