package com.benbentaxi.passenger.register;

import android.content.Intent;
import android.util.Log;

import com.benbentaxi.common.Configure;
import com.benbentaxi.lib.session.SessionApiConstant;
import com.benbentaxi.lib.session.SessionResponse;
import com.benbentaxi.passenger.demo.LocationOverlayDemo;
import com.benbentaxi.passenger.v1.function.DataPreference;
import com.benbentaxi.passenger.v1.function.GetInfoTask;

public class RegisterTask extends GetInfoTask{
	private final String API1 			="/api/v1/users/create_passenger";
	private RegisterRequest 	mRegisterRequest;
	private Configure       mConfigure;
	private RegisterForm    mRegisterForm;
	private final String TAG			     = RegisterTask.class.getName();

	public RegisterTask(RegisterForm registerForm)
	{
		mRegisterForm 	 = registerForm;
		mRegisterRequest = new RegisterRequest(mRegisterForm);
		mConfigure		 = new Configure();
	}
	public void go()
	{
		initHeaders("Content-Type", "application/json");
		
        
		this.mRegisterForm.showProgress(true);
		execute(getApiUrl(), "11111111", GetInfoTask.TYPE_POST);
	}
	private String getApiUrl()
	{
		return "http://"+mConfigure.getHost()+API1;
	}
	
	
	protected void onPostExecPost(Boolean succ) 
	{
		SessionResponse registerResponse = new SessionResponse(mRegisterForm,this.getResult());
		this.mRegisterForm.showProgress(false);
		if (registerResponse.hasError()){
			//do nothing
		}else{
			registerResponse.parser();
			//TODO：这里应该用application 存数据
			DataPreference mData = this.mRegisterForm.getDataPreference();
			mData.SaveData(SessionApiConstant.TOKEN_KEY, registerResponse.getTokenKey());
			mData.SaveData(SessionApiConstant.TOKEN_VALUE, registerResponse.getTokenVal());
			//TODO::这里需要修改
			mData.SaveData("isdriver", false);
			mData.SaveData("useragent",  mConfigure.getEquipmentId());
			//this.mRegisterForm.getActivity().
			//Toast.makeText(this.mRegisterForm.getActivity().getApplicationContext(), mData., duration)
			Intent locationOverlayIntent = new Intent(this.mRegisterForm.getActivity(),LocationOverlayDemo.class);
			
			this.mRegisterForm.getActivity().startActivity(locationOverlayIntent);
		}
	}
	protected void initPostValues() {
		if ( mRegisterRequest != null ) {
			post_param = mRegisterRequest.toJson().toString();
			Log.d(TAG, mRegisterRequest.toJson().toString());
		}
	}
}
