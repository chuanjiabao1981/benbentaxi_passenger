package com.benbentaxi.passenger.register;

import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.util.DefaultResponse;

public class RegisterVerifyTask extends PostTask{
	private final String API1 			=	"/api/v1/register_verifications";
	private Configure       	mConfigure;
	private RegisterForm    	mRegisterForm;
	private RegisterVerifyRequest mRegisterVerifyRequest;
	
	
	public RegisterVerifyTask(RegisterForm registerForm)
	{
		mRegisterForm 	 		= registerForm;
		mRegisterVerifyRequest 	= new RegisterVerifyRequest(mRegisterForm);
		mConfigure		 		= new Configure();
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
		DefaultResponse defaultResponse = new DefaultResponse(mRegisterForm,this.getResult());
		if (!succ){
			mRegisterForm.enableGetVerifyCode();
			defaultResponse.setSysErrorMessage(this.getErrorMsg());
		}
		if (!defaultResponse.hasError()){
			mRegisterForm.waitingVerifyCode();
		}else{
			mRegisterForm.enableGetVerifyCode();
		}
	}

}
