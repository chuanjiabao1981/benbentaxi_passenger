package com.benbentaxi.passenger.background;

import android.content.ComponentName;
import android.os.IBinder;
import android.util.Log;

public class NearybyDrvierServiceConnection extends BackgroundServiceConnection{
	private final static String TAG								=   NearybyDrvierServiceConnection.class.getName();
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		BackgroundServiceBinder binder 	= (BackgroundServiceBinder) service;
    	mBackgroundService 				= (BackgroundService) binder.getService();
    	mIBound 						= true;
    	mBackgroundService.startRefreshNearByDriver();
    	Log.d(TAG,"start refresh nearyby drivers");
	}
}
