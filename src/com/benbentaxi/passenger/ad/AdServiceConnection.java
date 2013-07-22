package com.benbentaxi.passenger.ad;


import com.benbentaxi.passenger.background.BackgroundService;
import com.benbentaxi.passenger.background.BackgroundServiceBinder;
import com.benbentaxi.passenger.background.BackgroundServiceConnection;

import android.content.ComponentName;
import android.os.IBinder;
import android.util.Log;

public class AdServiceConnection extends BackgroundServiceConnection {
	private static final String TAG				=	 AdServiceConnection.class.getName();
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) 
	{
		BackgroundServiceBinder binder 	= (BackgroundServiceBinder) service;
    	mBackgroundService 				= (BackgroundService) binder.getService();
    	mIBound 						= true;
    	mBackgroundService.startTextAd();
    	Log.d(TAG,"start refresh ad info");
	}

}
