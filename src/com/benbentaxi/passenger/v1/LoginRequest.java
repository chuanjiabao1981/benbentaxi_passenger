package com.benbentaxi.passenger.v1;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.benbentaxi.common.api.FormRequest;
import com.benbentaxi.common.api.ViewForm;

public class LoginRequest extends FormRequest{
	private final String TAG			     = LoginRequest.class.getName();

	public LoginRequest(ViewForm viewForm) {
		super(viewForm);
	}

	@Override
	public JSONObject toJson() {
		JSONObject _json_data = new JSONObject();
		try {
			JSONObject login = new JSONObject();
			setJsonKVFromControl(login,LoginApiConstant.MOBILE);
			setJsonKVFromControl(login,LoginApiConstant.PASSWORD);
			_json_data.put(LoginApiConstant.SESSION, login);
		} catch (JSONException e) {
			Log.e(TAG,"获取(设置)数据出错["+LoginApiConstant.SESSION+"]");
		}
		return _json_data;
	}

}
