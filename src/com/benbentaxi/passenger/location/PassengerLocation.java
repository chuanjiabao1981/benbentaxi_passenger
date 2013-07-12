package com.benbentaxi.passenger.location;

import android.app.Activity;
import android.os.Handler;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class PassengerLocation 
{
	
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
            if (location == null)
                return ;
            mHandler.sendMessage(mHandler.obtainMessage(LocationOverlayDemo.MSG_HANDLE_POS_REFRESH, location));
            mApp.setCurrentPassengerLocation(location);
    		mLocClient.stop();
        }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
}
