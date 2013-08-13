package com.benbentaxi.passenger.taxirequest.index;



import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.benbentaxi.Configure;

import com.benbentaxi.Session;
import com.benbentaxi.api.GetTask;
import com.benbentaxi.passenger.location.DemoApplication;


public class TaxiRequestIndexTask extends GetTask{
	private final String API1 						="/api/v1/taxi_requests";
	private final String TAG			     		= TaxiRequestIndexTask.class.getName();

	private Configure       mConfigure				= null;	
	private Session mSession 						= null;
	private DemoApplication mApp 					= null;
	private Context mContext						= null;
	private Handler mHandler						= null;

	private TaxiRequestIndexTask(Context context,DemoApplication app)
	{	
		mConfigure		 = new Configure();
		mContext=context;
		this.mApp = app;
		this.mSession 	  = mApp.getCurrentSession();
		
		if (this.mSession != null){
			setCookie(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());//TODO::测试这个加端口
			Log.d(TAG,mSession.getTokenKey()+":"+mSession.getTokenVal());
		}else{
			Log.e(TAG,"Session 获取出错!");
		}
	}
	
	public TaxiRequestIndexTask(Context context,DemoApplication app,Handler handler)
	{
		this(context,app);
		mHandler = handler;
	}
	
		
	public void go()
	{
		mHandler.sendMessage(mHandler.obtainMessage(TaxiRequestIndexActivity.MSG_HANDLE_INDEX_TASK_START));
		execute();
	}
	protected String getApiUrl()
	{
		return "http://"+mConfigure.getService()+API1;
	}
	
	
	protected void onPostExecute(Boolean succ) 
	{
		TaxiRequestIndexResponse taxiRequestIndexResponse = new TaxiRequestIndexResponse(this.getResult());
		if (!succ){
			taxiRequestIndexResponse.setSysErrorMessage(this.getErrorMsg());
			mHandler.sendMessage(mHandler.obtainMessage(TaxiRequestIndexActivity.MSG_HANDLE_INDEX_TASK_ERROR,this.getErrorMsg()));
		}
		if (!taxiRequestIndexResponse.hasError()){	
			mHandler.sendMessage(mHandler.obtainMessage(TaxiRequestIndexActivity.MSG_HANDLE_INDEX_TASK_SUCCESS,taxiRequestIndexResponse));
		}else{
			mHandler.sendMessage(mHandler.obtainMessage(TaxiRequestIndexActivity.MSG_HANDLE_INDEX_TASK_ERROR,"获取历史打车列表失败，访问服务器出错！"));
		}
	}
	
    
}
