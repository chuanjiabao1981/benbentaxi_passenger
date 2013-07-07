package com.benbentaxi.passenger.taxirequest.create;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import com.benbentaxi.Configure;

import com.benbentaxi.Session;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.location.LocationOverlayDemo;
import com.benbentaxi.session.SessionResponse;


public class CeateTaxiRequestTask extends PostTask{
	private final String API1 			="/api/v1/taxi_requests";
	private CeateTaxiRequest 	mCeateTaxiRequest;
	private Configure       mConfigure;
	private CeateTaxiRequestForm    mCeateTaxiRequestForm;
	private Session mSession = null;
	private DemoApplication mApp = null;
	private final String TAG			     = CeateTaxiRequestTask.class.getName();

	public CeateTaxiRequestTask(CeateTaxiRequestForm ceateTaxiRequestForm)
	{
		mCeateTaxiRequestForm	 = ceateTaxiRequestForm;
		mCeateTaxiRequest = new CeateTaxiRequest(mCeateTaxiRequestForm);
		mConfigure		 = new Configure();
		
		this.mApp =  (DemoApplication)this.mCeateTaxiRequestForm.getActivity().getApplicationContext();;
		this.mSession 	  = DemoApplication.getCurrentSession();
		
		if (this.mSession != null){
			initCookies(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());//TODO::测试这个加端口
			Log.d(TAG,mSession.getTokenKey()+":"+mSession.getTokenVal());
		}else{
			Log.e(TAG,"Session 获取出错!");
		}
		
	}
	
		
	public void go()
	{
		this.mCeateTaxiRequestForm.showProgress(true);
		execute();
	}
	protected String getApiUrl()
	{
		return "http://"+mConfigure.getService()+API1;
	}
	
	
	@SuppressWarnings("static-access")
	protected void onPostExecute(Boolean succ) 
	{
		Log.i("res : ","onPostExecute");
		SessionResponse sessionResponse = new SessionResponse(mCeateTaxiRequestForm,this.getResult());
		
		Log.i("res : ",this.getResult());
		
		this.mCeateTaxiRequestForm.showProgress(false);
		if (!succ){
			sessionResponse.setSysErrorMessage(this.getErrorMsg());
		}
		Log.i("sessionResponse.hasError() : ", String.valueOf(sessionResponse.hasError()));// sessionResponse.hasError()==false?"0":"1");
		
		if (!sessionResponse.hasError()){
			Log.i("res : ",this.getResult());
			//DemoApplication app = (DemoApplication)this.mCeateTaxiRequestForm.getActivity().getApplicationContext();
			//app.setCurrentSession(sessionResponse);
			//Passenger passenger = new Passenger(this.mCeateTaxiRequestForm.getMobile());
			//app.setCurrentPassenger(passenger);
			//Intent intent = new Intent(this.mCeateTaxiRequestForm.getActivity(),LocationOverlayDemo.class);
			//this.mCeateTaxiRequestForm.getActivity().startActivity(intent);			
			this.mCeateTaxiRequestForm.getActivity().finish();
		}
	}
	@Override
	protected String getPostParams() {
		if ( mCeateTaxiRequest != null ) {
			String k = mCeateTaxiRequest.toJson().toString();
			Log.d(TAG, k);
			return k;
		}
		return "";
	}
	
	
    
}
