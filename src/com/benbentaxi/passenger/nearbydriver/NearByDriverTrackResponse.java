package com.benbentaxi.passenger.nearbydriver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.benbentaxi.api.Response;
import com.benbentaxi.util.JsonHelper;

public class NearByDriverTrackResponse extends Response {
	private String TAG =   NearByDriverTrackResponse.class.getName();
	JSONArray mRes = null;
	public NearByDriverTrackResponse(String s)
	{
		super(s);
	}
	public int getSize()
	{
		if (mRes == null)
			return 0;
		return mRes.length();
	}
	public int getId(int index)
	{
		try {
			return JsonHelper.getInt(mRes.getJSONObject(index), NearyByDriverApiConstant.DRIVER_ID);
		} catch (JSONException e) {
			Log.e(TAG, "数组越界！");
			return -1;
		}
	}
	public double getLat(int index)
	{
		try {
			return JsonHelper.getDouble(mRes.getJSONObject(index), NearyByDriverApiConstant.LAT);
		} catch (JSONException e) {
			Log.e(TAG, "数组越界！");
			return -1;
		}
	}
	public double getLng(int index)
	{
		try {
			return JsonHelper.getDouble(mRes.getJSONObject(index), NearyByDriverApiConstant.LNG);
		} catch (JSONException e) {
			Log.e(TAG, "数组越界！");
			return -1;
		}
	}
	public String getCreatedAt(int index)
	{
		try {
			return JsonHelper.getString(mRes.getJSONObject(index), NearyByDriverApiConstant.CREATED_AT);
		} catch (JSONException e) {
			Log.e(TAG, "数组越界！");
			return "";
		}

	}
	public JSONObject getJsonTaxiRequest(int index)
	{
		try {
			return mRes.getJSONObject(index);
		} catch (JSONException e) {
			return null;
		}
	}
	@Override
	public void parser() {
		mRes = (JSONArray) this.getJsonResult();
	}
	@Override
	protected void dealError(String key, String val) {
		Log.e(TAG,key+":"+val);
	}

}
