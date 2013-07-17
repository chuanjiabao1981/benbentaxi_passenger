package com.benbentaxi.passenger.register;

import android.content.Intent;
import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.Passenger;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.location.LocationOverlayDemo;
import com.benbentaxi.session.SessionResponse;

public class RegisterTask extends PostTask{
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
		this.mRegisterForm.showProgress(true);
		execute();
	}
	protected String getApiUrl()
	{
		return "http://"+mConfigure.getService()+API1;
	}
	
	protected void onPostExecute(Boolean succ) 
	{
		
		SessionResponse sessionResponse = new SessionResponse(mRegisterForm,this.getResult());
		this.mRegisterForm.showProgress(false);
		if (!succ){
			sessionResponse.setSysErrorMessage(this.getErrorMsg());
		}
		if (!sessionResponse.hasError()){
			DemoApplication app = (DemoApplication)this.mRegisterForm.getActivity().getApplicationContext();
			app.setCurrentSession(sessionResponse);
			Passenger passenger = new Passenger(this.mRegisterForm.getMobile());
			app.setCurrentPassenger(passenger);
			Intent intent = new Intent(this.mRegisterForm.getActivity(),LocationOverlayDemo.class);
			this.mRegisterForm.getActivity().startActivity(intent);
		}
	}
	@Override
	protected String getPostParams() {
		if ( mRegisterRequest != null ) {
			String k = mRegisterRequest.toJson().toString();
			Log.d(TAG, k);
			return k;
		}
		return "";
	}
}
