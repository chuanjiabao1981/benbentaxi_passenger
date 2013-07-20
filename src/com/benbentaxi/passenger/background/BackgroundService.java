package com.benbentaxi.passenger.background;

import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.nearbydriver.NearByDriverTask;
import com.benbentaxi.passenger.nearbydriver.NearByDriverTrackResponse;

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
    private NearByDriverTrackResponse mNearByDriverTrackResponse			= null;
    private HandlerThread mThread 											= null;


	@Override
	public void onCreate()
	{
		mThread 								= new HandlerThread(TAG,android.os.Process.THREAD_PRIORITY_BACKGROUND);
		mThread.start();
		mLooper 			 					= mThread.getLooper();
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
		mThread.quit();
		this.mHandler.removeMessages(MSG_NEAR_BY_DRIVERS);
    }
	public void startRefreshNearByDriver()
	{
		
		this.mHandler.sendMessage(this.mHandler.obtainMessage(MSG_NEAR_BY_DRIVERS));
	}
	public NearByDriverTrackResponse getNearByDriverTrackResponse()
	{
		return mNearByDriverTrackResponse;
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

						NearByDriverTask nearByDriverTask 							= new NearByDriverTask((DemoApplication) BackgroundService.this.getApplication());
					 	mNearByDriverTrackResponse 	  								= nearByDriverTask.send();
					 	if (mNearByDriverTrackResponse != null){
							LocalBroadcastManager.getInstance(BackgroundService.this).sendBroadcast(mNearbyDriverIntent);
					 	}else{
					 		Log.e(TAG,"获取附近司机为null....");
					 	}
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_NEAR_BY_DRIVERS), REFRESH_NEARBY_DRIVER_INTERVAL);
						break;
					default:
						break;
				}
			}


	}
	
	
	
}
