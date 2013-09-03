package com.benbentaxi.passenger.register;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.benbentaxi.api.FormRequest;
import com.benbentaxi.api.ViewForm;

public class RegisterVerifyRequest  extends FormRequest
{
	private final String TAG			     = RegisterVerifyRequest.class.getName();

	public RegisterVerifyRequest(ViewForm viewForm) {
		super(viewForm);
	}

	@Override
	public JSONObject toJson() {
		JSONObject _json_data = new JSONObject();
		try {
			JSONObject verify = new JSONObject();
			setJsonKVFromControl(verify,RegisterApiConstant.MOBILE);
			_json_data.put(RegisterApiConstant.REGISTER_VERIFICATION, verify);
		} catch (JSONException e) {
			Log.e(TAG,"获取(设置)数据出错["+RegisterApiConstant.REGISTER_VERIFICATION+"]");
		}
		return _json_data;	}

}
