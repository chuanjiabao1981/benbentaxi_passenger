package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;

import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;
import com.benbentaxi.passenger.taxirequest.detail.TaxiRequestDetail;

public class SuccessStateHandler implements StateChangeHandler{
	@Override
	public void handler(Activity activity,TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		DemoApplication app = (DemoApplication) activity.getApplication();
		app.setCurrentShowTaxiRequest(app.getCurrentTaxiRequest());
		Intent taxiRequestDetailIntent = new Intent(activity,TaxiRequestDetail.class);
		activity.startActivity(taxiRequestDetailIntent);
		app.setCurrentTaxiRequest(null);
	}

}
