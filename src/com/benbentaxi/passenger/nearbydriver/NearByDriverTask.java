package com.benbentaxi.passenger.nearbydriver;

import android.util.Log;

import com.benbentaxi.Configure;
import com.benbentaxi.Session;
import com.benbentaxi.api.GetTask;
import com.benbentaxi.passenger.location.DemoApplication;

public class NearByDriverTask extends GetTask{
	public static final String TAG = NearByDriverTask.class.getCanonicalName();
	public String mApiUrl			= null;
	public Configure  mConfigure	= null;
	private DemoApplication mApp = null;
	private Session			mSession = null;

//	String url =  "http://"+mTestHost+"/api/v1/users/nearby_driver?lat="+lat+"&lng="+lng;

	@SuppressWarnings("static-access")
	public NearByDriverTask(DemoApplication app)
	{
		mConfigure = new Configure();
		mApp 	   = app;
		this.mSession 	  		= mApp.getCurrentSession();
		if (this.mSession != null){
			initCookies(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());
		}else{
			Log.e(TAG,"Session 获取出错!");
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
	@Override
	protected String getApiUrl() {
		return "http://"+mConfigure.getService()+"/api/v1/users/nearby_driver?lat="+lat()+"&lng="+lng();
	}

	@Override
	public void go() {
		if (lat() >0 && lng() > 0)
			execute();
			
	}
	
	@SuppressWarnings("static-access")
	protected void onPostExecute(final Boolean succ) 
	{
		NearByDriverTrackResponse nearByDriverTrackResponse = new NearByDriverTrackResponse(this.getResult());
		if (!succ){
			nearByDriverTrackResponse.setSysErrorMessage(this.getErrorMsg());
		}
		if (!nearByDriverTrackResponse.hasError()){
					this.mApp.setCurrentNearByDrivers(nearByDriverTrackResponse);
		}
	}

}
