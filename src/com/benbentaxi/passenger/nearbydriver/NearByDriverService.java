package com.benbentaxi.passenger.nearbydriver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NearByDriverService extends Service{
	private static final String TAG = NearByDriverService.class.getName();
	private NearByDrvierBinder mNearByDrvierBinder = new NearByDrvierBinder();
	private Thread			   mServiceThread	   = null;
	private long			   mServiceSleepTime   = 1000;
	private boolean			   mIsStop			   = false;
	@Override
	public void onCreate()
	{
		Log.i(TAG,"Test some Service Code............................");
		mServiceThread = new Thread(TAG){
			
			public void run(){
				while(!mIsStop && !Thread.interrupted()){
					try {
						Thread.sleep(mServiceSleepTime);
						Log.i(TAG,"Test some Service Code............................");
					} catch (InterruptedException e) {
						Log.d(TAG,"Interrupt......");
					}
				}
			}
		};
		mServiceThread.start();
	}
	@Override
	public IBinder onBind(Intent intent) {
		return mNearByDrvierBinder;
	}
	public void onDestroy() {
		this.mIsStop = true;
		mServiceThread.interrupt();
		super.onDestroy();
    }
	public class NearByDrvierBinder extends Binder{
		public NearByDriverService getService()
		{
			return NearByDriverService.this;
		}
	}
	
	
	
}
