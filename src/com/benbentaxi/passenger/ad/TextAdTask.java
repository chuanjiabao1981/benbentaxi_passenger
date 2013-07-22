package com.benbentaxi.passenger.ad;

import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.Session;
import com.benbentaxi.api.JsonHttpRequest;
import com.benbentaxi.passenger.location.DemoApplication;

public class TextAdTask {
	private final static String	TAG					= TextAdTask.class.getName();
	public String mApiUrl							= "/api/v1/advertisements";
	public Configure  mConfigure					= null;
	private DemoApplication mApp 					= null;
	private Session			mSession 				= null;
	
	private JsonHttpRequest mJsonHttpRequest		= null;
	
	public TextAdTask(DemoApplication app)
	{
		mApp				=	app;
		mSession 	  		= 	mApp.getCurrentSession();
		mConfigure 			= new Configure();
	}
	
	public TextAds send() {
		mJsonHttpRequest 	= new JsonHttpRequest();
		setCookie();
		boolean succ 	 	= mJsonHttpRequest.get(getApiUrl());
		TextAds textAds 	= new TextAds(mJsonHttpRequest.getResult());
		if (!succ){
			textAds.setSysErrorMessage(mJsonHttpRequest.getErrorMsg());
		}
		if (!textAds.hasError()){
				Log.d(TAG,mJsonHttpRequest.getResult());
				return textAds;
		}else{
				//错误已经在hasError中处理过了，所以这里不再处理
		}
		return null;
	}
	private void setCookie()
	{
		if (this.mSession != null){
			mJsonHttpRequest.setCookie(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());
		}else{
			Log.e(TAG,"Session 获取出错!");
		}
	}
	protected String getApiUrl() {
		return "http://"+mConfigure.getService()+mApiUrl;
	}

}
