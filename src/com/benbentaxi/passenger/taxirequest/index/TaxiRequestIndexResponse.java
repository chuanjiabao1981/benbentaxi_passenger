package com.benbentaxi.passenger.taxirequest.index;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.benbentaxi.api.Response;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;

public class TaxiRequestIndexResponse extends Response {

	private String TAG =   TaxiRequestIndexResponse.class.getName();
	JSONArray mRes = null;
	
	public TaxiRequestIndexResponse(String s)
	{
		super(s);
	}
	public int getSize()
	{
		if (mRes == null)
			return 0;
		return mRes.length();
	}
	private  JSONObject getJSONbyIndex(int Index){
		if(Index<getSize())
			try {
				return mRes.getJSONObject(Index);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		else
			return null;
	}
	
	public TaxiRequest getTaxiRequest(int index)
	{
		if(getJSONbyIndex(index)!=null)
			return new TaxiRequest(null,getJSONbyIndex(index));
		else
			return null;
	}
	
	
	
	@Override
	public void parser() {
		// TODO Auto-generated method stub
		mRes = (JSONArray) this.getJsonResult();
	}

	@Override
	protected void dealError(String key, String val) {
		// TODO Auto-generated method stub
		Log.e(TAG,key+":"+val);
	}

}
