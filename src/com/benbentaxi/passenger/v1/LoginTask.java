package com.benbentaxi.passenger.v1;

import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.passenger.v1.function.GetInfoTask;

public class LoginTask extends GetInfoTask{
	private final String API1 			="/api/v1/sessions/passenger_signin";

	private LoginForm 			mLoginForm;
	private LoginRequest		mLoginRequest;
	private Configure       	mConfigure;

	private final String TAG			     = LoginTask.class.getName();
	
	public LoginTask(LoginForm loginForm)
	{
		this.mLoginForm 			= loginForm;
		this.mLoginRequest			= new LoginRequest(mLoginForm);
		this.mConfigure 			= new Configure();
	}
	
	public void go()
	{
		initHeaders("Content-Type", "application/json");
		this.mLoginForm.showProgress(true);
		execute(getApiUrl(), "11111111", GetInfoTask.TYPE_POST);
	}
	
	
	private String getApiUrl()
	{
		return "http://"+mConfigure.getHost()+API1;
	}
	
	protected void initPostValues() {
		if ( mLoginRequest != null ) {
			post_param = mLoginRequest.toJson().toString();
			Log.d(TAG, mLoginRequest.toJson().toString());
		}
	}



}
