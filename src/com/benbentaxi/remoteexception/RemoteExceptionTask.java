package com.benbentaxi.remoteexception;

import org.json.JSONObject;

import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.Session;
import com.benbentaxi.api.JsonHttpRequest;
import com.benbentaxi.api.PostTask;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.create.CreateTaxiRequestFormResponse;

public class RemoteExceptionTask extends PostTask{
	private final static String API						=	"/api/v1/client_exceptions";
	private final String TAG			     			= RemoteExceptionTask.class.getName();
	private Configure       mConfigure					=  null;
	private Session mSession 							= null;
	private DemoApplication mApp 						= null;
	private JsonHttpRequest mJsonHttpRequest			= null;
	private RemoteExceptionRequest	mExceptionRequest		= null;
	
	public RemoteExceptionTask(String exception)
	{
		mExceptionRequest		= new RemoteExceptionRequest(exception);
		mConfigure				= new Configure();
		mJsonHttpRequest		= new JsonHttpRequest();
	}
	protected String getPostParams() {
		String str = mExceptionRequest.toJson().toString();
		Log.d(TAG,"get exception data " + str);
		return str;
	}

	protected String getApiUrl() {
		return "http://"+mConfigure.getService()+API;
	}
	protected void onPostExecute(Boolean succ) 
	{
		
		//Log.d(TAG,"the result is "+this.getResult() + "|"+succ);
	}

	public void go() 
	{
		execute();
		//boolean s = mJsonHttpRequest.post(getApiUrl(), getPostParams());
		//Log.d(TAG,"============|"+s);
		
	}
}
