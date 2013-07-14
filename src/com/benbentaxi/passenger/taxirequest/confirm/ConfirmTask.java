package com.benbentaxi.passenger.taxirequest.confirm;



import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.Session;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.passenger.location.DemoApplication;
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
	private Activity mActivity = null;
	private boolean mIsConfirm = true;
	private Handler mHandler   = null;
	
	public ConfirmTask(Activity activity,Handler handler,boolean isConfirm)
	{
		this.mApp = (DemoApplication) activity.getApplication();
		this.mHandler = handler;
		this.mActivity = activity;
		this.mConfigure = new Configure();
		this.mTaxiRequest = mApp.getCurrentTaxiRequest();
		this.mSession 	  = mApp.getCurrentSession();
		if (this.mTaxiRequest != null && this.mTaxiRequest.getId() > 0){
			this.mTaxiRequestId = this.mTaxiRequest.getId();
		}else{
			Log.e(TAG,"获取taxi request 出错!");
		}
		if (this.mSession != null){
			initCookies(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());//TODO::测试这个加端口
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
			return "http://"+mConfigure.getService()+"/api/v1/taxi_requests/"+this.mTaxiRequestId+"/confirm";
		else
			return "http://"+mConfigure.getService()+"/api/v1/taxi_requests/"+this.mTaxiRequestId+"/cancel";
	}

	@Override
	public void go() {
		if (this.mTaxiRequestId > 0){
			Log.i(TAG,String.format("Send taxi request["+this.mTaxiRequestId+"] is %s Message!",this.mIsConfirm ? "[Confirmed]" : "[Canceled]"));
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
					this.mTaxiRequest.refresh(this.mActivity,this.mHandler,taxiRequestResponse);
		}
	}

}
