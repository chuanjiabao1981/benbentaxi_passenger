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
			setJsonKVFromControl(json,CreateTaxiRequestApiConstant.SOURCE);
			setJsonKVFromControl(json,CreateTaxiRequestApiConstant.DESTINATION);
			String strLatAndLng=mCeateTaxiRequestForm.getLatAndLng();			
			String[] sLatAndLng = strLatAndLng.split("\\|");
			String strAudio=mCeateTaxiRequestForm.getAudio();	
			json.put("passenger_mobile", mCeateTaxiRequestForm.getMobile());
			json.put("passenger_lng", sLatAndLng[0]);
			json.put("passenger_lat", sLatAndLng[1]);
			json.put("waiting_time_range", 10);
			json.put("passenger_voice", strAudio);
			json.put("passenger_voice_format", "3gp");			
			_json_data.put(CreateTaxiRequestApiConstant.TAXI_REQUEST_CREATE, json);
		} catch (JSONException e) {
			Log.e(TAG,"获取打车数据出错["+CreateTaxiRequestApiConstant.SOURCE+"]");
		}
		return _json_data;
	}		
}
