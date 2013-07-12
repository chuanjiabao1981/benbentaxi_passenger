package com.benbentaxi.passenger.taxirequest.index;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import com.benbentaxi.Configure;

import com.benbentaxi.Session;
import com.benbentaxi.api.GetTask;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.location.LocationOverlayDemo;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;
import com.benbentaxi.session.SessionResponse;


public class TaxiRequestIndexTask extends GetTask{
	private final String API1 			="/api/v1/taxi_requests";	
	private Configure       mConfigure;	
	private Session mSession = null;
	private TaxiRequestIndexActivity mTaxiRequestIndexActivity;
	private DemoApplication mApp = null;
	private final String TAG			     = TaxiRequestIndexTask.class.getName();

	public TaxiRequestIndexTask(TaxiRequestIndexActivity activity)
	{	
		mConfigure		 = new Configure();
		mTaxiRequestIndexActivity=activity;
		this.mApp = (DemoApplication) mTaxiRequestIndexActivity.getApplication();
		this.mSession 	  = mApp.getCurrentSession();
		
		if (this.mSession != null){
			initCookies(mSession.getTokenKey(), mSession.getTokenVal(),mConfigure.getHost());//TODO::测试这个加端口
			Log.d(TAG,mSession.getTokenKey()+":"+mSession.getTokenVal());
		}else{
			Log.e(TAG,"Session 获取出错!");
		}
		
	}
	
		
	public void go()
	{
		//this.mTaxiRequestIndexForm.showProgress(true);
		execute();
	}
	protected String getApiUrl()
	{
		return "http://"+mConfigure.getService()+API1;
	}
	
	
	@SuppressWarnings("static-access")
	protected void onPostExecute(Boolean succ) 
	{
		
		TaxiRequestIndexResponse taxiRequestIndexResponse = new TaxiRequestIndexResponse(this.getResult());
		
		//Log.i("res : ",this.getResult());
		
		//this.mTaxiRequestIndexForm.showProgress(false);
		if (!succ){
			taxiRequestIndexResponse.setSysErrorMessage(this.getErrorMsg());
		}		
		
		if (!taxiRequestIndexResponse.hasError()){	
			//Log.i("rows:",String.valueOf(taxiRequestIndexResponse.getSize()));
			ArrayList<TaxiRequest> list=new ArrayList<TaxiRequest>(); 
			for(int i=0;i<taxiRequestIndexResponse.getSize();i++)
			{
				TaxiRequest tx=taxiRequestIndexResponse.getTaxiRequest(i);
				list.add(tx);
			}
			mTaxiRequestIndexActivity.showList(list);
			//this.mTaxiRequestIndexForm.getActivity().finish();
		}
	}
	
    
}
