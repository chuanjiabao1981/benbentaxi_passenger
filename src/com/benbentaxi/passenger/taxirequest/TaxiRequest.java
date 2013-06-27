package com.benbentaxi.passenger.taxirequest;

import org.json.JSONObject;

import com.benbentaxi.util.JsonHelper;

public class TaxiRequest {
	
	private long   mId;
	private String mPassengerMobile;
	private String mDriverMobile;
	private float  mDriverLat = -1;
	private float  mDriverLng = -1;
	private float  mPassengerLat = -1;
	private float  mPassengerLng = -1;
	private TaxiRequestState mTaxiRequestState = TaxiRequestState.Waiting_Driver_Response;
	
	public TaxiRequest(JSONObject obj)
	{
		init(obj);
	}
	
	private void init(JSONObject obj)
	{
		mId 				= JsonHelper.getLong(obj, TaxiRequestApiConstant.ID);
		mPassengerMobile	= JsonHelper.getString(obj, TaxiRequestApiConstant.PASSENGER_MOBILE);
		mDriverMobile		= JsonHelper.getString(obj, TaxiRequestApiConstant.DRIVER_MOBILE);
		mDriverLat 			= JsonHelper.getFloat(obj, TaxiRequestApiConstant.DRIVER_LAT);
		mDriverLng			= JsonHelper.getFloat(obj, TaxiRequestApiConstant.DRIVER_LNG);
		mPassengerLat		= JsonHelper.getFloat(obj, TaxiRequestApiConstant.PASSENGER_LAT);
		mPassengerLng		= JsonHelper.getFloat(obj, TaxiRequestApiConstant.PASSENGER_LNG);
		mTaxiRequestState   = TaxiRequestApiConstant.getState(JsonHelper.getString(obj,TaxiRequestApiConstant.STATE));
	}
	
}
