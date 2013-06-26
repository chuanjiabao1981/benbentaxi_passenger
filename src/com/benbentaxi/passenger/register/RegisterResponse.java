package com.benbentaxi.passenger.register;


import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.benbentaxi.common.ApiConstant;
import com.benbentaxi.common.SysErrorMessage;
import com.benbentaxi.common.api.ViewForm;

public class RegisterResponse {
	private String mRStr;
	private String mTokenKey;
	private String mTokenVal;
	private Object mRjson = null;
	private String mSysErrorMessage = null;
	private RegisterForm mRegisterForm = null;
	private final String TAG			     = RegisterResponse.class.getName();
	private RESULT_JSON_TYPE mRJsonType = null;
	enum RESULT_JSON_TYPE {JSONObject,JSONArray};
	
	public RegisterResponse(RegisterForm registerForm,String rStr)
	{
		this.mRStr = rStr;
		this.mRegisterForm = registerForm;
		init();
		
	}
	private RESULT_JSON_TYPE getResponseJsonType()
	{
		return mRJsonType;
	}
	
	private void init()
	{
		JSONTokener jsParser = new JSONTokener(mRStr);
		try {
			if (mRStr.startsWith("[")){
				mRJsonType = RESULT_JSON_TYPE.JSONArray;
			}else if (mRStr.startsWith("{")){
				mRJsonType = RESULT_JSON_TYPE.JSONObject;
			}
			mRjson = jsParser.nextValue();
			
		} catch (JSONException e) {
			setSysErrorMessage(SysErrorMessage.ERROR_API_DATA_ERROR);
		}catch (Exception e){
			setSysErrorMessage(SysErrorMessage.ERROR_NET_WORK);
		}
	}
	public void parser()
	{
		try {

			if (((JSONObject)mRjson).has(RegisterApiConstant.TOKEN_KEY)){
				mTokenKey = ((JSONObject)mRjson).getString(RegisterApiConstant.TOKEN_KEY);
			
			}
			if (((JSONObject)mRjson).has(RegisterApiConstant.TOKEN_VALUE)){
				mTokenVal = ((JSONObject)mRjson).getString(RegisterApiConstant.TOKEN_VALUE);
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
	public boolean hasError()
	{
		if (hasSysError()){
			dealError();
			return true;
		}
		if (hasAppError()){
			dealError();
			return true;
		}
		return false;
	}
	
	private void dealError()
	{
		if (hasSysError()){
			mRegisterForm.setControlFieldError(ApiConstant.BASE, getSysErrorMesssage());
			return;
		}
		if (hasAppError()){
			JSONObject err;
			try {
				err = ((JSONObject)mRjson).getJSONObject(ApiConstant.ERROR);
				@SuppressWarnings("rawtypes")
				Iterator i = err.keys();
				while(i.hasNext()){
					String k = (String)i.next();
					String v = err.getJSONArray(k).getString(0);
					if (getViewForm() != null){
						getViewForm().setControlFieldError(k, v);
					}
				}
			} catch (JSONException e) {
				Log.e(TAG, "解析应用层错误数据出错(JSON)!");
			} catch (Exception e) {
				Log.e(TAG, "解析应用层错误数据出错!");
			}
		}
	}
	
	private boolean hasSysError()
	{
		if (mSysErrorMessage != null){
			return true;
		}
		return false;
	}
	private boolean hasAppError()
	{
		if (getResponseJsonType() == RESULT_JSON_TYPE.JSONObject && ((JSONObject)mRjson).has(ApiConstant.ERROR))
			return true;
		return false;
	}
	private void setSysErrorMessage(String m)
	{
		this.mSysErrorMessage = m;
	}
	private String getSysErrorMesssage()
	{
		return this.mSysErrorMessage;
	}
	
	private ViewForm getViewForm()
	{
		return mRegisterForm;
	}
}
