package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;

public class TimeOutStateHandler implements StateChangeHandler {
	private static final String TAG = TimeOutStateHandler.class.getName();
	@Override
	public void handler(DemoApplication app, Handler handler,TaxiRequest old,
			TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		Log.d(TAG,((JSONObject) newState.getJsonResult()).toString());
		app.setCurrentTaxiRequest(null);
	}

}
