package com.benbentaxi.passenger.register;

import android.content.Intent;
import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.Passenger;
import com.benbentaxi.Session;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.lib.session.SessionResponse;
import com.benbentaxi.passenger.demo.DemoApplication;
import com.benbentaxi.passenger.demo.LocationOverlayDemo;

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
		return "http://"+mConfigure.getHost()+API1;
	}
	
	
	@SuppressWarnings("static-access")
	protected void onPostExecute(Boolean succ) 
	{
		
		SessionResponse sessionResponse = new SessionResponse(mRegisterForm,this.getResult());
		this.mRegisterForm.showProgress(false);
		if (!succ){
			sessionResponse.setSysErrorMessage(this.getErrorMsg());
		}
		if (!sessionResponse.hasError()){
			
			DemoApplication app = (DemoApplication)this.mRegisterForm.getActivity().getApplicationContext();

			Session session = new Session(sessionResponse.getTokenKey(),sessionResponse.getTokenVal());
			app.setCurrentSession(session);
			Passenger passenger = new Passenger(this.mRegisterForm.getMobile());
			app.setCurrentPassenger(passenger);

			Intent intent = new Intent(this.mRegisterForm.getActivity(),LocationOverlayDemo.class);
			this.mRegisterForm.getActivity().startActivity(intent);
		}
		/*
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
		}*/
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
