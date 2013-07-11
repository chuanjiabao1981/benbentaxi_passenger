package com.benbentaxi.passenger.taxirequest.create;

import android.util.Log;

import com.benbentaxi.Configure;

import com.benbentaxi.Session;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.passenger.location.DemoApplication;


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
		this.mSession 	  = mApp.getCurrentSession();
		
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
	
	
	protected void onPostExecute(Boolean succ) 
	{
		
		CreateTaxiRequestFormResponse createTaxiRequestFormResponse = new CreateTaxiRequestFormResponse(mCeateTaxiRequestForm,this.getResult());
		
		this.mCeateTaxiRequestForm.showProgress(false);
		if (!succ){
			createTaxiRequestFormResponse.setSysErrorMessage(this.getErrorMsg());
		}
		if (!createTaxiRequestFormResponse.hasError()){
			Log.d("res : ",this.getResult());
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
