package com.benbentaxi.passenger.taxirequest;

import org.json.JSONObject;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.state.DefaultStateChangeHandler;
import com.benbentaxi.passenger.taxirequest.state.FinalStateHandler;
import com.benbentaxi.passenger.taxirequest.state.SimpleStateMachine;
import com.benbentaxi.passenger.taxirequest.state.StateChangeHandler;
import com.benbentaxi.passenger.taxirequest.state.SuccessStateHandler;
import com.benbentaxi.passenger.taxirequest.state.TaxiRequestState;
import com.benbentaxi.passenger.taxirequest.state.TimeOutStateHandler;
import com.benbentaxi.passenger.taxirequest.state.WaitingConfirmStateHandler;
import com.benbentaxi.util.JsonHelper;

public class TaxiRequest {
	private static SimpleStateMachine mSimpleStateMachine;
	private String TAG = TaxiRequest.class.getName();
	private long   mId;
	private String mPassengerMobile;
	private String mDriverMobile;
	private float  mDriverLat = -1;
	private float  mDriverLng = -1;
	private float  mPassengerLat = -1;
	private float  mPassengerLng = -1;
	private JSONObject mTaxiRequestJson = null;
	private TaxiRequestState mTaxiRequestState = TaxiRequestState.Waiting_Driver_Response;
	
	private static FinalStateHandler FINAL_STATE_HANDLER = new FinalStateHandler();
	
	static {
		mSimpleStateMachine = new SimpleStateMachine(new DefaultStateChangeHandler());
		mSimpleStateMachine.addHandler(TaxiRequestState.Canceled_By_Passenger, FINAL_STATE_HANDLER);
		mSimpleStateMachine.addHandler(TaxiRequestState.Success, new SuccessStateHandler());
		mSimpleStateMachine.addHandler(TaxiRequestState.Waiting_Passenger_Confirm, new WaitingConfirmStateHandler());
		mSimpleStateMachine.addHandler(TaxiRequestState.TimeOut, new TimeOutStateHandler());
	}
	
	
	
	public TaxiRequest(JSONObject obj)
	{	this.mTaxiRequestJson = obj;
		init(obj);
	}
	
	public void refresh(Activity activity,TaxiRequestResponse newState)
	{
		if (newState == null){
			Log.e(TAG,"newState is null");
			return;
		}
		if (newState.getJsonResult() == null){
			Log.e(TAG,"newState has no resualt");
			return;
		}
		long newId = JsonHelper.getLong((JSONObject) newState.getJsonResult(), TaxiRequestApiConstant.ID);
		if (newId != this.mId){
			Log.e(TAG,"Old Id["+this.mId+"] New Id ["+newId+"]");
			return;
		}
		TaxiRequestState oldTaxiRequestState = this.mTaxiRequestState;
		TaxiRequestState newTaxiRequestState = TaxiRequestApiConstant.getState(JsonHelper.getString((JSONObject) newState.getJsonResult(), TaxiRequestApiConstant.STATE));
		StateChangeHandler handler 			 = TaxiRequest.mSimpleStateMachine.findHandler(this.mTaxiRequestState, newTaxiRequestState);
		if (handler == null){
			Log.e(TAG,"from " + this.mTaxiRequestState.toString() +" to "+newTaxiRequestState.toString() +" not find any handler!");
			return;
		}
		//TODO::Lock
		//TODO::时间戳判断

		handler.handler(activity,this,newState);
		Log.i(TAG,"From [" + oldTaxiRequestState.toString() +"] to ["+newTaxiRequestState.toString() +"] done!");

		
		//Log.d(TAG,"Refresh State To:"+this.getState().toString());
		//Log.d(TAG,"Refresh Json is:"+this.mTaxiRequestJson.toString());

	}
	/*
	public DemoApplication getApp()
	{
		return (DemoApplication) this.mActivity.getApplication();
	}
	public Activity getActivity()
	{
		return this.mActivity;
	}*/
	public String getField(String key)
	{
		if (TaxiRequestApiConstant.DISTANCE.equals(key)){
			return "0.2";
			/*
			return   String.valueOf(
							Math.sqrt(
										((mDriverLat - mPassengerLat)*(mDriverLat - mPassengerLat) + (mDriverLng-mPassengerLng) *  (mDriverLng-mPassengerLng))
									  )/1000.0
									);*/
		}
		if (TaxiRequestApiConstant.PLATE.equals(key)){
			return "晋C13452";
		}
//		Log.d(TAG,this.mTaxiRequestJson.toString());
		return JsonHelper.getString(this.mTaxiRequestJson, key);
	}
	public String getState()
	{
		return this.mTaxiRequestState.toString();
	}
	public String getDriverMobile()
	{
		return this.mDriverMobile;
	}
	public Float getDistance()
	{
		return 0.2f ;
	}
	public long getId()
	{
		return this.mId;
	}
	
	public boolean isWaitingPassengerConfirm()
	{
		if (this.mTaxiRequestState  == TaxiRequestState.Waiting_Passenger_Confirm){
			return true;
		}
		return false;
	}
	public boolean isTaxiRequestSuccess()
	{
		if(this.mTaxiRequestState == TaxiRequestState.Success){
			return true;
		}
		return false;
	}
	public void init(JSONObject obj)
	{
		this.mTaxiRequestJson 	= obj;
		mTaxiRequestState 		= TaxiRequestApiConstant.getState(JsonHelper.getString(obj, TaxiRequestApiConstant.STATE));
		mId 				 	= JsonHelper.getLong(obj, TaxiRequestApiConstant.ID);
		mPassengerMobile		= JsonHelper.getString(obj, TaxiRequestApiConstant.PASSENGER_MOBILE);
		mDriverMobile			= JsonHelper.getString(obj, TaxiRequestApiConstant.DRIVER_MOBILE);
		mDriverLat 				= JsonHelper.getFloat(obj, TaxiRequestApiConstant.DRIVER_LAT);
		mDriverLng				= JsonHelper.getFloat(obj, TaxiRequestApiConstant.DRIVER_LNG);
		mPassengerLat			= JsonHelper.getFloat(obj, TaxiRequestApiConstant.PASSENGER_LAT);
		mPassengerLng			= JsonHelper.getFloat(obj, TaxiRequestApiConstant.PASSENGER_LNG);
	}
	public String getHumanStateText(){
		return this.mTaxiRequestState.getHumanText();
	}
	
	
	
	
}
