package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;

import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;



public class FinalStateHandler implements StateChangeHandler{
	
	@Override
	public void handler(Activity activity,Handler handler,TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		((DemoApplication)activity.getApplication()).setCurrentTaxiRequest(null);
	}

}
