package com.benbentaxi.passenger.taxirequest.create;

import org.json.JSONException;
import org.json.JSONObject;

import com.benbentaxi.api.FormRequest;

import android.util.Log;

public class CeateTaxiRequest extends FormRequest
{
	private final String TAG			     = CeateTaxiRequest.class.getName();
	private final CeateTaxiRequestForm frm;
	public CeateTaxiRequest(CeateTaxiRequestForm ceateTaxiRequestForm)
	{
		super(ceateTaxiRequestForm);
		frm=ceateTaxiRequestForm;
	}
	
	public JSONObject toJson()
	{
		JSONObject _json_data = new JSONObject();
		try {
			JSONObject sess = new JSONObject();
			//setJsonKVFromControl(sess,CreateTaxiRequestApiConstant.START_FROM);
			//setJsonKVFromControl(sess,CreateTaxiRequestApiConstant.WANT_TO);
			String strXY=frm.getXY();
			String strAudio=frm.getAudio();
			Log.i("strAudio:",strAudio);
			sess.put("passenger_mobile", frm.getMobile());
			sess.put("passenger_lng", 25.21);
			sess.put("passenger_lat", 31.21);
			sess.put("waiting_time_range", 10);
			sess.put("passenger_voice", strAudio);//"aSB3aWxsIGJlIHRoZXJl");
			sess.put("passenger_voice_format", "3gp");
			//_json_data.put("taxi_request", sess);
			//setJsonKVFromControl(sess,CreateTaxiRequestApiConstant.DRIVER_MOBILE);
			_json_data.put(CreateTaxiRequestApiConstant.TAXI_REQUEST_CREATE, sess);
		} catch (JSONException e) {
			Log.e(TAG,"获取(设置)数据出错["+CreateTaxiRequestApiConstant.START_FROM+"]");
		}
		return _json_data;
	}		
}
