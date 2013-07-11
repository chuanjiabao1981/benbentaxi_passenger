package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;

import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;



public class FinalStateHandler implements StateChangeHandler{
	
	@Override
	public void handler(TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		old.getApp().setCurrentTaxiRequest(null);
	}

}
