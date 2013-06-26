package com.benbentaxi.passenger.register;



import org.json.JSONException;
import org.json.JSONObject;

import com.benbentaxi.common.api.FormResponse;

import android.util.Log;


public class RegisterResponse extends FormResponse{
	private String mTokenKey;
	private String mTokenVal;
	private final String TAG			     = RegisterResponse.class.getName();
	
	
	public RegisterResponse(RegisterForm registerForm,String rStr)
	{
		super(registerForm,rStr);		
	}
	
	public void parser()
	{
		try {
			if (((JSONObject)getJsonResult()).has(RegisterApiConstant.TOKEN_KEY)){
				mTokenKey = ((JSONObject)getJsonResult()).getString(RegisterApiConstant.TOKEN_KEY);
			}
			if (((JSONObject)getJsonResult()).has(RegisterApiConstant.TOKEN_VALUE)){
				mTokenVal = ((JSONObject)getJsonResult()).getString(RegisterApiConstant.TOKEN_VALUE);
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
