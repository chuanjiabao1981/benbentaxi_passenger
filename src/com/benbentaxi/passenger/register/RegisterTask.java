package com.benbentaxi.passenger.register;

import android.util.Log;

import com.benbentaxi.common.Configure;
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
		RegisterResponse registerResponse = new RegisterResponse(mRegisterForm,this.getResult());
		this.mRegisterForm.showProgress(false);
		if (registerResponse.hasError()){
			
		}else{
			
		}
	}
	protected void initPostValues() {
		if ( mRegisterRequest != null ) {
			post_param = mRegisterRequest.toJson().toString();
			Log.d(TAG, mRegisterRequest.toJson().toString());
		}
	}
}
