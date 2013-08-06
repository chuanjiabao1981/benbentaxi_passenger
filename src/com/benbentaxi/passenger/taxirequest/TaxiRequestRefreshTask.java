package com.benbentaxi.passenger.taxirequest;


import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.Session;
import com.benbentaxi.api.GetTask;
import com.benbentaxi.passenger.location.DemoApplication;

public class TaxiRequestRefreshTask extends GetTask{
	private final String TAG			     = TaxiRequestRefreshTask.class.getName();

	private final long DEFAILT_ID   = -1;
	
	
	private long mTaxiRequestId = DEFAILT_ID;
	private Configure mConfigure;
	private TaxiRequest mTaxiRequest;
	private DemoApplication mApp;
	private Session mSession = null;
	private Handler mHandler = null; 

	public TaxiRequestRefreshTask(Activity activity,Handler handler)
	{
		this.mConfigure 		= new  Configure();
		this.mHandler 			= handler;
		this.mApp 				= (DemoApplication) activity.getApplication();
		this.mTaxiRequest		= mApp.getCurrentTaxiRequest();
		this.mSession 	  		= mApp.getCurrentSession();

		if (this.mTaxiRequest != null && this.mTaxiRequest.getId() > 0){
			this.mTaxiRequestId = this.mTaxiRequest.getId();
		}
		if (this.mSession != null){
			setCookie(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());
//			Log.d(TAG,mSession.getTokenKey()+":"+mSession.getTokenVal());
		}else{
			Log.e(TAG,"Session 获取出错!");
		}
	}
	
	@Override
	protected String getApiUrl() {
		return "http://"+mConfigure.getService()+"/api/v1/taxi_requests/"+this.mTaxiRequestId;
	}

	@Override
	public void go() {
		if (this.mTaxiRequestId > 0){
			Log.d(TAG,"id is ["+this.mTaxiRequestId+"]");
			execute();
		}else{
			Log.e(TAG,"id is not illagle ["+this.mTaxiRequestId+"]");
		}
	}
	
	protected void onPostExecute(final Boolean succ) 
	{
		TaxiRequestResponse taxiRequestResponse = new TaxiRequestResponse(this.getResult());
		if (!succ){
			taxiRequestResponse.setSysErrorMessage(this.getErrorMsg());
		}
		if (!taxiRequestResponse.hasError()){
					this.mTaxiRequest.refresh(mApp,mHandler,taxiRequestResponse);
		}
	}


}
