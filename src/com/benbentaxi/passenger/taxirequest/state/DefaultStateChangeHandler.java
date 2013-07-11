package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;

import android.app.Activity;

import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;

public class DefaultStateChangeHandler implements StateChangeHandler{
	//private final static String TAG = DefaultStateChangeHandler.class.getName();
	@Override
	public void handler(Activity activity,TaxiRequest oldState, TaxiRequestResponse newState) {
		oldState.init((JSONObject) newState.getJsonResult());
	}

}
