package com.benbentaxi.passenger.taxirequest;

import org.json.JSONObject;

import android.util.Log;

public class DefaultStateChangeHandler implements StateChangeHandler{
	private final static String TAG = DefaultStateChangeHandler.class.getName();
	@Override
	public void handler(TaxiRequest oldState, TaxiRequestResponse newState) {
		if (oldState == null){
			Log.e(TAG,"oldState is null");
			return;
		}
		if (newState == null){
			Log.e(TAG,"newState is null");
			return;
		}
		if (newState.getJsonResult() == null){
			Log.e(TAG,"newState has no resualt");
		}
		if (oldState != null ){
			oldState.init((JSONObject) newState.getJsonResult());
		}
	}

}
