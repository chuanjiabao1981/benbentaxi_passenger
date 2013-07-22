package com.benbentaxi.passenger.ad;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.benbentaxi.api.Response;
import com.benbentaxi.util.JsonHelper;

public class TextAds extends  Response{
	private final static String TAG			=	TextAds.class.getName();
	private JSONArray mRes 					= null;

	public TextAds(String r) {
		super(r);
	}
	public String getContent(int index)
	{
		if (index >= getSize())
			return "";
		try {
			return JsonHelper.getString(mRes.getJSONObject(index),TextAdApiConstant.CONTENT);
		} catch (JSONException e) {
			Log.e(TAG, "数组越界！");
			return "";
		}
	}
	public int getSize()
	{
		if (mRes == null)
			return 0;
		return mRes.length();
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
