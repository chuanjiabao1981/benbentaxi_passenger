package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;

import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;
import com.benbentaxi.passenger.taxirequest.detail.TaxiRequestDetail;

public class SuccessStateHandler implements StateChangeHandler{
	@Override
	public void handler(DemoApplication app,Handler handler,TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		app.setCurrentShowTaxiRequest(app.getCurrentTaxiRequest());
		Intent taxiRequestDetailIntent = new Intent(app,TaxiRequestDetail.class);
		app.startActivity(taxiRequestDetailIntent);
		app.setCurrentTaxiRequest(null);
	}

}
