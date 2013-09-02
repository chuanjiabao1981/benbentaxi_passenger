package com.benbentaxi.passenger.register;

import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.util.DefaultResponse;

public class RegisterVerifyTask extends PostTask{
	private final String API1 			=	"/api/v1/register_verifications";
	private Configure       	mConfigure;
	private DemoApplication 	mApp;
	private RegisterForm    	mRegisterForm;
	private RegisterVerifyRequest mRegisterVerifyRequest;

	
	public RegisterVerifyTask(RegisterForm registerForm)
	{
		mRegisterForm 	 		= registerForm;
		mRegisterVerifyRequest 	= new RegisterVerifyRequest(mRegisterForm);
		mConfigure		 		= new Configure();
		mApp 			 		= (DemoApplication) mRegisterForm.getActivity().getApplication();

	}

	@Override
	protected String getPostParams() {
		if ( mRegisterVerifyRequest != null ) {
			String k = mRegisterVerifyRequest.toJson().toString();
			Log.d(TAG, k);
			return k;
		}
		return "";
	}

	@Override
	protected String getApiUrl() {
		return "http://"+mConfigure.getService()+API1;
	}

	@Override
	public void go() {
		mRegisterForm.disableGetVerifyCode();
		execute();
	}
	
	protected void onPostExecute(Boolean succ) 
	{
//		mRegisterForm.enableGetVerifyCode();
		DefaultResponse defaultResponse = new DefaultResponse(mRegisterForm,this.getResult());
		if (!succ){
			mRegisterForm.enableGetVerifyCode();
			defaultResponse.setSysErrorMessage(this.getErrorMsg());
		}
		if (!defaultResponse.hasError()){
//			DemoApplication app = (DemoApplication)this.mRegisterForm.getActivity().getApplicationContext();
		}else{
			mRegisterForm.enableGetVerifyCode();
		}
	}

}
