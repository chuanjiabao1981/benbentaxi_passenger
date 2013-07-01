package com.benbentaxi.passenger.demo;


import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.benbentaxi.Passenger;
import com.benbentaxi.Session;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;


public class DemoApplication extends Application {
	
    private static DemoApplication mInstance 			= null;
    private static TaxiRequest CurrentShowTaxiRequest 	= null;
    private static TaxiRequest CurrentTaxiRequest 		= null;
    private static Session 	   CurrentSession			= null;
    private static Passenger   CurrentPassenger			= null;
    public boolean m_bKeyRight = true;
    BMapManager mBMapManager = null;

    public static final String strKey = "1BE33CC3A1DEBDC8FF3A8A3F23A5E208C27E5C83";
	
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
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
    
    public static void setCurrentPassenger(Passenger passenger)
    {
    	CurrentPassenger = passenger;
    }
    public static Passenger getCurrentPassenger()
    {
    	return CurrentPassenger;
    }
    
    public static Session getCurrentSession()
    {
    	return CurrentSession;
    }
    
    public static void setCurrentSession(Session session)
    {
    	CurrentSession = session;
    }
    public static void setCurrentTaxiRequest(TaxiRequest o)
    {
		CurrentTaxiRequest = o;
	}
    public static TaxiRequest getCurrentTaxiRequest()
    {
    	return CurrentTaxiRequest;
    }
    
    public static void setCurrentShowTaxiRequest(TaxiRequest t)
    {
    	CurrentShowTaxiRequest = t;
    }
    public static TaxiRequest getCurrentShowTaxiRequest()
    {
    	return CurrentShowTaxiRequest;
    }
}