package com.benbentaxi.passenger.nearbydriver;

import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.Session;
import com.benbentaxi.api.ApiConstant;
import com.benbentaxi.api.JsonHttpRequest;
import com.benbentaxi.passenger.location.DemoApplication;

public class NearByDriverTask {
	public static final String TAG 					= NearByDriverTask.class.getCanonicalName();
	public String mApiUrl							= null;
	public Configure  mConfigure					= null;
	private DemoApplication mApp 					= null;
	private Session			mSession 				= null;
	private String			mTenantName				= null;
	private JsonHttpRequest mJsonHttpRequest		= null;

	public NearByDriverTask(DemoApplication app)
	{
		mConfigure 				= new Configure();
		mApp 	   				= app;
		mSession 	  			= mApp.getCurrentSession();
		if (mApp.getCurrentPassengerLocation() != null){
			mTenantName				= mApp.getCurrentPassengerLocation().getCity();
		}
		
	}
	private double lng()
	{
		if (mApp.getCurrentPassengerLocation() != null)
			return mApp.getCurrentPassengerLocation().getLongitude();
		return -1;
	}
	private double lat()
	{
		if (mApp.getCurrentPassengerLocation() != null)
			return mApp.getCurrentPassengerLocation().getLatitude();
		return -1;
	}
	protected String getApiUrl() {
		String prefix	=	 "http://"+mConfigure.getService()+"/api/v1/users/nearby_driver?lat="+lat()+"&lng="+lng();
		if (mTenantName == null){
			return prefix;
		}
		else{
			return prefix+"&"+ApiConstant.TENAT_NAME+"="+mTenantName;
		}
	}

	public NearByDriverTrackResponse send() {
		mJsonHttpRequest = new JsonHttpRequest();
		setCookie();
		if (lat() >0 && lng() > 0){
			boolean succ 										= mJsonHttpRequest.get(getApiUrl());
			NearByDriverTrackResponse nearByDriverTrackResponse = new NearByDriverTrackResponse(mJsonHttpRequest.getResult());
			if (!succ){
				nearByDriverTrackResponse.setSysErrorMessage(mJsonHttpRequest.getErrorMsg());
			}
			if (!nearByDriverTrackResponse.hasError()){
				Log.d(TAG,mJsonHttpRequest.getResult());
				return nearByDriverTrackResponse;
			}else{
				//错误已经在hasError中处理过了，所以这里不再处理
			}
			return null;
		}else{
			Log.e(TAG,"获取经纬度数据出错!");
			return null;
		}
			
	}
	private void setCookie()
	{
		if (this.mSession != null){
			mJsonHttpRequest.setCookie(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());
		}else{
			Log.e(TAG,"Session 获取出错!");
		}
	}

}
