package com.benbentaxi.passenger.register;

import org.json.JSONException;
import org.json.JSONObject;

import com.benbentaxi.api.FormRequest;

import android.util.Log;

public class RegisterRequest extends FormRequest
{
	private final String TAG			     = RegisterRequest.class.getName();
	public RegisterRequest(RegisterForm registerForm)
	{
		super(registerForm);
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
}
