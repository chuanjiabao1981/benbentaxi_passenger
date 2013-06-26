package com.benbentaxi.passenger.register;

import org.json.JSONException;
import org.json.JSONObject;

import com.benbentaxi.common.api.ViewForm;
import android.util.Log;

public class RegisterRequest 
{
	private RegisterForm mRegisterForm;
	private final String TAG			     = RegisterRequest.class.getName();

	
	
	public RegisterRequest(RegisterForm registerForm)
	{
		this.mRegisterForm = registerForm;
	}
	
	public JSONObject toJson()
	{
		JSONObject _json_data = new JSONObject();
		try {
			JSONObject sess = new JSONObject();
			setJsonKVFromControl(sess,RegisterApiConstant.MOBILE);
			setJsonKVFromControl(sess,RegisterApiConstant.PASSWORD);
			setJsonKVFromControl(sess,RegisterApiConstant.PAWWWORD_CONFIRM);
			_json_data.put(RegisterApiConstant.USER, sess);
		} catch (JSONException e) {
			Log.e(TAG,"获取(设置)数据出错["+RegisterApiConstant.USER+"]");
		}
		return _json_data;
	}
	
	protected void setJsonKVFromControl(JSONObject obj,String controlName)
	{
		if (obj != null && getViewForm() != null){
			try {
				obj.put(controlName, getViewForm().getControlVal(controlName));
			} catch (JSONException e) {
				Log.e(TAG,"获取(设置)数据出错["+controlName+"]");
			}
		}
	}
	
	protected ViewForm getViewForm()
	{
		return mRegisterForm;
	}
	
	
}
