package com.benbentaxi.passenger.location;



import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.benbentaxi.Passenger;
import com.benbentaxi.Session;
import com.benbentaxi.passenger.nearbydriver.NearByDriverTrackResponse;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.util.DataPreference;


public class DemoApplication extends Application {
	private static final String LOGIN_MOBILE					= "login_mobile";
	private static final String LOGIN_PASS						= "login_pass";
    private static DemoApplication mInstance 			= null;
    private TaxiRequest mCurrentShowTaxiRequest 	= null;
    private TaxiRequest mCurrentTaxiRequest 		= null;
    private Session 	   mCurrentSession			= null;
    private Passenger   mCurrentPassenger			= null;
    private BDLocation mCurrentPassengerLocation 		= null;
    private NearByDriverTrackResponse mCurrentNearbyDrivers = null;
    private DataPreference 					mDataPreference 	  = null;
    private Handler		mHandler					= null;
    public boolean m_bKeyRight = true;
    
    BMapManager mBMapManager = null;

    public static final String strKey = "1BE33CC3A1DEBDC8FF3A8A3F23A5E208C27E5C83";
	
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		mDataPreference = new DataPreference(this.getApplicationContext()); 
		initEngineManager(this);
	}
	
	@Override
	//建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
	    if (mBMapManager != null) {
            mBMapManager.destroy();
            mBMapManager = null;
        }
		super.onTerminate();
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
            Toast.makeText(DemoApplication.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	
	public static DemoApplication getInstance() {
		return mInstance;
	}
	
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(DemoApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(DemoApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
                Toast.makeText(DemoApplication.getInstance().getApplicationContext(), 
                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                DemoApplication.getInstance().m_bKeyRight = false;
            }
        }
    }
    
    public void setCurrentPassenger(Passenger passenger)
    {
    	mCurrentPassenger = passenger;
    }
    public Passenger getCurrentPassenger()
    {
    	return mCurrentPassenger;
    }
    
    public Session getCurrentSession()
    {
    	return mCurrentSession;
    }
    
    public void setCurrentSession(Session session)
    {
    	mCurrentSession = session;
    }
    public  void setCurrentTaxiRequest(TaxiRequest o)
    {
		mCurrentTaxiRequest = o;
		if (o != null){
			this.mHandler.sendMessage(mHandler.obtainMessage(LocationOverlayDemo.MSG_HANDLE_REFRESH_CURRENT_TAXIREQUEST));
		}
	}
    public  TaxiRequest getCurrentTaxiRequest()
    {
    	return mCurrentTaxiRequest;
    }
    
    public  void setCurrentShowTaxiRequest(TaxiRequest t)
    {
    	mCurrentShowTaxiRequest = t;
    }
    public  TaxiRequest getCurrentShowTaxiRequest()
    {
    	return mCurrentShowTaxiRequest;
    }
    
    public  BDLocation getCurrentPassengerLocation()
    {
    	return mCurrentPassengerLocation;
    }
    public  void setCurrentPassengerLocation(BDLocation locationData)
    {
    	mCurrentPassengerLocation = locationData;
    }
    public  NearByDriverTrackResponse getCurrentNearByDriverTrack()
    {
    	return mCurrentNearbyDrivers;
    }
    public  void setCurrentNearByDrivers(NearByDriverTrackResponse nearByDriverTrackResponse)
    {
    	mCurrentNearbyDrivers = nearByDriverTrackResponse;
    }
    
    public void setLoginMobile(String mobile)
    {
    	mDataPreference.SaveData(LOGIN_MOBILE, mobile);
    }
    public String getLoginMobile()
    {
    	return mDataPreference.LoadString(LOGIN_MOBILE);
    }
    public void setLoginPass(String pass)
    {
    	mDataPreference.SaveData(LOGIN_PASS, pass);
    }
    public String getLoginPass()
    {
    	return mDataPreference.LoadString(LOGIN_PASS);
    }
    public void setHandler(Handler h)
    {
    	this.mHandler = h;
    }
    
}