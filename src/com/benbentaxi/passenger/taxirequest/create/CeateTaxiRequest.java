package com.benbentaxi.passenger.taxirequest.create;

import org.json.JSONException;
import org.json.JSONObject;

import com.benbentaxi.api.FormRequest;

import android.util.Log;

public class CeateTaxiRequest extends FormRequest
{
	private final String TAG			     = CeateTaxiRequest.class.getName();
	private final CeateTaxiRequestForm mCeateTaxiRequestForm;
	public CeateTaxiRequest(CeateTaxiRequestForm ceateTaxiRequestForm)
	{
		super(ceateTaxiRequestForm);
		mCeateTaxiRequestForm=ceateTaxiRequestForm;
	}
	
	public JSONObject toJson()
	{
		JSONObject _json_data = new JSONObject();
		try {
			JSONObject json = new JSONObject();			
			String strAudio=mCeateTaxiRequestForm.getAudio();	
			json.put(CreateTaxiRequestApiConstant.PASSENGER_MOBILE, mCeateTaxiRequestForm.getMobile());
			json.put(CreateTaxiRequestApiConstant.PASSENGER_LAT, mCeateTaxiRequestForm.getLat());
			json.put(CreateTaxiRequestApiConstant.PASSENGER_LNG, mCeateTaxiRequestForm.getLng());
			json.put(CreateTaxiRequestApiConstant.WAITING_TIME_RANGE, 10);
			json.put(CreateTaxiRequestApiConstant.PASSENGER_VOICE, strAudio);
			json.put(CreateTaxiRequestApiConstant.PASSENGER_VOICE_FORMAT, "3gp");	
			json.put(CreateTaxiRequestApiConstant.SOURCE,mCeateTaxiRequestForm.getSource());
			_json_data.put(CreateTaxiRequestApiConstant.TAXI_REQUEST_CREATE, json);
		} catch (JSONException e) {
			Log.e(TAG,"获取打车数据出错["+CreateTaxiRequestApiConstant.SOURCE+"]");
		}
		return _json_data;
	}		
}
