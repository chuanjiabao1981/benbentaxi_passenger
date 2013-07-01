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
	
	public void refresh(JSONObject obj)
	{
		//TODO::Lock
		//TODO::时间戳判断
		if (JsonHelper.getLong(obj, TaxiRequestApiConstant.ID) == this.mId){
			init(obj);
			this.mTaxiRequestJson = obj;
		}else{
			Log.e(TAG,"Old Id["+this.mId+"] New Id ["+JsonHelper.getLong(obj, TaxiRequestApiConstant.ID)+"]");
		}
		Log.d(TAG,this.mId+":"+this.mTaxiRequestState.getHumanText());
		//Log.d(TAG,"Refresh State To:"+this.getState().toString());
		//Log.d(TAG,"Refresh Json is:"+this.mTaxiRequestJson.toString());

	}
	public String getField(String key)
	{
		if (TaxiRequestApiConstant.DISTANCE.equals(key)){
			return "0.2";
			/*
			return   String.valueOf(
							Math.sqrt(
										((mDriverLat - mPassengerLat)*(mDriverLat - mPassengerLat) + (mDriverLng-mPassengerLng) *  (mDriverLng-mPassengerLng))
									  )/1000.0
									);*/
		}
		if (TaxiRequestApiConstant.PLATE.equals(key)){
			return "晋C13452";
		}
//		Log.d(TAG,this.mTaxiRequestJson.toString());
		return JsonHelper.getString(this.mTaxiRequestJson, key);
	}
	public String getState()
	{
		return this.mTaxiRequestState.toString();
	}
	public String getDriverMobile()
	{
		return this.mDriverMobile;
	}
	public Float getDistance()
	{
		return 0.2f ;
	}
	public long getId()
	{
		return this.mId;
	}
	
	public boolean isWaitingPassengerConfirm()
	{
		if (this.mTaxiRequestState  == TaxiRequestState.Waiting_Passenger_Confirm){
			return true;
		}
		return false;
	}
	public boolean isTaxiRequestSuccess()
	{
		if(this.mTaxiRequestState == TaxiRequestState.Success){
			return true;
		}
		return false;
	}
	public void init(JSONObject obj)
	{
		this.mTaxiRequestJson 	= obj;
		mTaxiRequestState 		= TaxiRequestApiConstant.getState(JsonHelper.getString(obj, TaxiRequestApiConstant.STATE));
		mId 				 	= JsonHelper.getLong(obj, TaxiRequestApiConstant.ID);
		mPassengerMobile		= JsonHelper.getString(obj, TaxiRequestApiConstant.PASSENGER_MOBILE);
		mDriverMobile			= JsonHelper.getString(obj, TaxiRequestApiConstant.DRIVER_MOBILE);
		mDriverLat 				= JsonHelper.getFloat(obj, TaxiRequestApiConstant.DRIVER_LAT);
		mDriverLng				= JsonHelper.getFloat(obj, TaxiRequestApiConstant.DRIVER_LNG);
		mPassengerLat			= JsonHelper.getFloat(obj, TaxiRequestApiConstant.PASSENGER_LAT);
		mPassengerLng			= JsonHelper.getFloat(obj, TaxiRequestApiConstant.PASSENGER_LNG);
	}
	
	
	
	
}
