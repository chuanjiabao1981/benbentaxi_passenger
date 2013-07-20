package com.benbentaxi.passenger.location;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class PassengerLocation 
{
	private static final String TAG = PassengerLocation.class.getName(); 
	private LocationClient mLocClient;
	private Handler				mHandler;
	private DemoApplication     mApp;
	public PassengerLocation(Activity a,Handler h)
	{
		mLocClient 	= new LocationClient(a.getApplicationContext());
        mHandler 	= h;
        mApp		= (DemoApplication) a.getApplication();
        mLocClient.registerLocationListener( new MyLocationListenner() );
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setAddrType("all");
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
	}
	public void start()
	{
         mLocClient.start();
	}
	
	public void stop()
	{
		mLocClient.stop();
	}
	
	public int requestLocation()
	{
		return mLocClient.requestLocation();
	}
	
	/**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        //private int mCountFactor = 0; // 计数器，控制执行频率
        
    	@Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || 
            		location.getLocType() == 62 || 
            		location.getLocType() == 63 ||
            		location.getLocType() == 67 ||
            		(location.getLocType() >= 162 && location.getLocType() <=167))
            {
            	if (location != null){
            		Log.d(TAG,"定位不成功:"+location.getLocType()+"|"+location.getLongitude()+"|"+location.getLatitude());
            	}
                return ;
            }
            mHandler.sendMessage(mHandler.obtainMessage(LocationOverlayDemo.MSG_HANDLE_POS_REFRESH, location));
            mApp.setCurrentPassengerLocation(location);
    		Log.d(TAG,"定位成功:"+location.getLocType()+"|"+location.getLongitude()+"|"+location.getLatitude());

    		mLocClient.stop();
        }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
}
