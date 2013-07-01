package com.benbentaxi.passenger.taxirequest;

import org.json.JSONObject;

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

	@SuppressWarnings("static-access")
	public TaxiRequestRefreshTask(DemoApplication app)
	{
		this.mConfigure 		= new  Configure();
		this.mApp 				= app;
		this.mTaxiRequest		= mApp.getCurrentTaxiRequest();
		this.mSession 	  		= mApp.getCurrentSession();

		if (this.mTaxiRequest != null && this.mTaxiRequest.getId() > 0){
			this.mTaxiRequestId = this.mTaxiRequest.getId();
		}
		if (this.mSession != null){
			initCookies(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());
			Log.d(TAG,mSession.getTokenKey()+":"+mSession.getTokenVal());
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
					this.mTaxiRequest.refresh(taxiRequestResponse);
		}
	}


}
