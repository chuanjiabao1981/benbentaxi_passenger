package com.benbentaxi.passenger.taxirequest.state;

import android.app.Activity;

import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;

public interface StateChangeHandler {
	public void handler(Activity activity,TaxiRequest old,TaxiRequestResponse newState);
}
