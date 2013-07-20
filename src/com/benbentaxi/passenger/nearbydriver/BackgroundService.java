package com.benbentaxi.passenger.nearbydriver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class BackgroundService extends Service{
	private static final int	MSG_NEAR_BY_DRIVERS							= 0;
	private static final long	REFRESH_NEARBY_DRIVER_INTERVAL   			= 10000;
	public  static final String	NEARYBY_DRIVER_ACTION						= "nearbydrvier_action";
	private static final String TAG 										= BackgroundService.class.getName();
	private BackgroundServiceBinder mBackgroundServiceBinder 				= null;
    private Looper			   mLooper			   							= null;
    private ServiceHandler	   mHandler										= null;

	@Override
	public void onCreate()
	{
		HandlerThread thread 					= new HandlerThread(TAG,android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		mLooper 			 					= thread.getLooper();
		mBackgroundServiceBinder				= new BackgroundServiceBinder(this);
		mHandler								= new ServiceHandler(mLooper);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return mBackgroundServiceBinder;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mLooper.quit();
    }
	public void startRefreshNearByDriver()
	{
		this.mHandler.dispatchMessage(this.mHandler.obtainMessage(MSG_NEAR_BY_DRIVERS));
	}
	
	private final class ServiceHandler extends Handler
	{
		private Intent mNearbyDriverIntent = new Intent(NEARYBY_DRIVER_ACTION);
		 public ServiceHandler(Looper looper) {
	          super(looper);
	      }
		 public void handleMessage(Message msg) {
				switch (msg.what)
				{
					case	MSG_NEAR_BY_DRIVERS:
						Log.i(TAG, "Test me ............");
						LocalBroadcastManager.getInstance(BackgroundService.this).sendBroadcast(mNearbyDriverIntent);
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_NEAR_BY_DRIVERS), REFRESH_NEARBY_DRIVER_INTERVAL);
						break;
					default:
						break;
				}
			}


	}
	
	
	
}
