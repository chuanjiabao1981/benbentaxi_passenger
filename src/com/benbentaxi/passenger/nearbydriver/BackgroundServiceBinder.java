package com.benbentaxi.passenger.nearbydriver;

import android.app.Service;
import android.os.Binder;

public class BackgroundServiceBinder extends Binder{
	private BackgroundService mBackgroundService;
	public BackgroundServiceBinder(BackgroundService bs)
	{
		super();
		mBackgroundService = bs;
	}
	public Service getService()
	{
		return mBackgroundService;
	}
}
