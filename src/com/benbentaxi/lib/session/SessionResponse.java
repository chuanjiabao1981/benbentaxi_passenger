package com.benbentaxi.lib.session;



import org.json.JSONException;
import org.json.JSONObject;

import com.benbentaxi.api.FormResponse;
import com.benbentaxi.api.ViewForm;

import android.util.Log;


public class SessionResponse extends FormResponse{
	private String mTokenKey;
	private String mTokenVal;
	private final String TAG			     = SessionResponse.class.getName();
	
	
	public SessionResponse(ViewForm viewForm,String rStr)
	{
		super(viewForm,rStr);		
	}
	
	public void parser()
	{
		try {
			if (((JSONObject)getJsonResult()).has(SessionApiConstant.TOKEN_KEY)){
				mTokenKey = ((JSONObject)getJsonResult()).getString(SessionApiConstant.TOKEN_KEY);
			}
			if (((JSONObject)getJsonResult()).has(SessionApiConstant.TOKEN_VALUE)){
				mTokenVal = ((JSONObject)getJsonResult()).getString(SessionApiConstant.TOKEN_VALUE);
			}
		} catch (JSONException e) {
			Log.d(TAG, "解析注册信息结果出错！");
		}
	}
	
	public String getTokenKey()
	{
		return this.mTokenKey;
	}
	public String getTokenVal()
	{
		return this.mTokenVal;
	}
	
}
