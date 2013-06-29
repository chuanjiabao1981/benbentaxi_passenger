package com.benbentaxi.passenger.taxirequest;

import org.json.JSONObject;

import android.util.Log;

import com.benbentaxi.util.JsonHelper;

public class TaxiRequest {
	private String TAG = TaxiRequest.class.getName();

	private long   mId;
	private String mPassengerMobile;
	private String mDriverMobile;
	private float  mDriverLat = -1;
	private float  mDriverLng = -1;
	private float  mPassengerLat = -1;
	private float  mPassengerLng = -1;
	private JSONObject mTaxiRequestJson = null;
	private TaxiRequestState mTaxiRequestState = TaxiRequestState.Waiting_Driver_Response;
	
	public TaxiRequest(JSONObject obj)
	{	this.mTaxiRequestJson = obj;
		init(obj);
	}
	
	
	public String getField(String key)
	{
		if (TaxiRequestApiConstant.DISTANCE.equals(key)){
			return   String.valueOf(
							Math.sqrt(
										((mDriverLat - mPassengerLat)*(mDriverLat - mPassengerLat) + (mDriverLng-mPassengerLng) *  (mDriverLng-mPassengerLng))
									  )/1000.0
									);
		}
		return JsonHelper.getString(this.mTaxiRequestJson, key);
	}
	public String getDriverMobile()
	{
		return this.mDriverMobile;
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
