package com.benbentaxi.passenger.nearbydriver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class NearbyDrvierReceiver extends BroadcastReceiver {
	private Activity mActivity;
	
	public NearbyDrvierReceiver(Activity a)
	{
		mActivity  = a;
	}
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Toast.makeText(mActivity, "xxxxxxxxx", Toast.LENGTH_LONG).show();
	}

}
