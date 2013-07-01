package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;

import android.content.Intent;

import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;
import com.benbentaxi.passenger.taxirequest.detail.TaxiRequestDetail;

public class SuccessStateHandler implements StateChangeHandler{
	@SuppressWarnings("static-access")
	@Override
	public void handler(TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		old.getApp().setCurrentShowTaxiRequest(old.getApp().getCurrentTaxiRequest());
		Intent taxiRequestDetailIntent = new Intent(old.getActivity(),TaxiRequestDetail.class);
		old.getActivity().startActivity(taxiRequestDetailIntent);
		old.getApp().setCurrentTaxiRequest(null);
	}

}
