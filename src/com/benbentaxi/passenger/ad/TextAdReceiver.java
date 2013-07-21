package com.benbentaxi.passenger.ad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TextAdReceiver extends BroadcastReceiver{
	private static final String TAG							= TextAdReceiver.class.getName();
	private TextAdFragment 		mTextAdFragment				= null;
	
	public TextAdReceiver(TextAdFragment textAdFragment)
	{
		mTextAdFragment				=		textAdFragment;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		if (mTextAdFragment  != null && this.mTextAdFragment.getBackgroundService() != null){
			Log.d(TAG,"recive ad info......");
		}
	}

}
