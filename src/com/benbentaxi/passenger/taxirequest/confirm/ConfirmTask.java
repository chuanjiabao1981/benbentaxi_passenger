package com.benbentaxi.passenger.taxirequest.confirm;


import org.json.JSONObject;

import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.Session;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.passenger.demo.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;

public class ConfirmTask extends PostTask{
	private final String TAG			     = ConfirmTask.class.getName();

	private final long DEFAILT_ID   = -1;
	private DemoApplication mApp = null;
	private TaxiRequest mTaxiRequest = null;
	private long mTaxiRequestId = DEFAILT_ID;
	private Session mSession = null;
	private Configure mConfigure =null;
	private boolean mIsConfirm = true;
	
	@SuppressWarnings("static-access")
	public ConfirmTask(DemoApplication app,boolean isConfirm)
	{
		this.mApp = app;
		this.mConfigure = new Configure();
		this.mTaxiRequest = mApp.getCurrentTaxiRequest();
		this.mSession 	  = mApp.getCurrentSession();
		if (this.mTaxiRequest != null && this.mTaxiRequest.getId() > 0){
			this.mTaxiRequestId = this.mTaxiRequest.getId();
		}else{
			Log.e(TAG,"获取taxi request 出错!");
		}
		if (this.mSession != null){
			initCookies(mSession.getTokenKey(), mSession.getTokenVal(),"42.121.55.211");//TODO::测试这个加端口
			Log.d(TAG,mSession.getTokenKey()+":"+mSession.getTokenVal());
		}else{
			Log.e(TAG,"Session 获取出错!");
		}
		this.mIsConfirm = isConfirm;
	}
	@Override
	protected String getPostParams() {
		return "";
	}

	@Override
	protected String getApiUrl() {
		if (this.mIsConfirm)
			return "http://"+mConfigure.getHost()+"/api/v1/taxi_requests/"+this.mTaxiRequestId+"/confirm";
		else
			return "http://"+mConfigure.getHost()+"/api/v1/taxi_requests/"+this.mTaxiRequestId+"/cancel";
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
			Log.d(TAG,"xxxxxxxxxxxxxxxxx");
		}
		if (!taxiRequestResponse.hasError()){
					this.mTaxiRequest.refresh((JSONObject) taxiRequestResponse.getJsonResult());
		}
	}

}
