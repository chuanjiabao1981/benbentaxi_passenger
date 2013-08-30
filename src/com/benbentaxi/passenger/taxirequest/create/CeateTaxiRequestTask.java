package com.benbentaxi.passenger.taxirequest.create;

import org.json.JSONObject;

import android.util.Log;

import com.benbentaxi.Configure;

import com.benbentaxi.Session;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;


public class CeateTaxiRequestTask extends PostTask{
	private final static String API1 									=	"/api/v1/taxi_requests";
	private final static String TAG			     						= CeateTaxiRequestTask.class.getName();

	private CeateTaxiRequest 	mCeateTaxiRequest						= 	null;
	private Configure       mConfigure									=	null;
	private CeateTaxiRequestForm    mCeateTaxiRequestForm				=	null;
	private Session mSession 											= 	null;
	private DemoApplication mApp 										= 	null;

	public CeateTaxiRequestTask(CeateTaxiRequestForm ceateTaxiRequestForm)
	{
		mCeateTaxiRequestForm	 = ceateTaxiRequestForm;
		mCeateTaxiRequest = new CeateTaxiRequest(mCeateTaxiRequestForm);
		mConfigure		 = new Configure();
		
		this.mApp =  (DemoApplication)this.mCeateTaxiRequestForm.getActivity().getApplicationContext();;
		this.mSession 	  = mApp.getCurrentSession();
		
		if (this.mSession != null){
			setCookie(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());//TODO::测试这个加端口
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
			this.mApp.setCurrentTaxiRequest(
											new TaxiRequest(
													(JSONObject) createTaxiRequestFormResponse.getJsonResult()));
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
