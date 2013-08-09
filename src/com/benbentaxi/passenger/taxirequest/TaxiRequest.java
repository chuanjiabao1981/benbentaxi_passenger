package com.benbentaxi.passenger.taxirequest;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;


import android.os.Handler;
import android.util.Log;

import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
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
	private String mSource;
	private String mCreatedAt;
	private Date mCreatedDate;
	private float  mDriverLat = -1;
	private float  mDriverLng = -1;
	private float  mPassengerLat = -1;
	private float  mPassengerLng = -1;
	private float  mDistance	 = 0.0f;
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
	
	public void refresh(DemoApplication app,Handler h,TaxiRequestResponse newState)
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

		handler.handler(app,h,this,newState);
		Log.d(TAG,"From [" + oldTaxiRequestState.toString() +"] to ["+newTaxiRequestState.toString() +"] done!");
		
	}
	public String getField(String key)
	{
		if (TaxiRequestApiConstant.DISTANCE.equals(key)){
			return getDistance();
		}
		return JsonHelper.getString(this.mTaxiRequestJson, key);
	}
	public String getSource()
	{
		return this.mSource;
	}
	public String getState()
	{
		return this.mTaxiRequestState.toString();
	}
	public String getHumanBreifTextState(){
		return mTaxiRequestState.getHumanBreifText();
	}
	public String getDriverMobile()
	{
		return this.mDriverMobile;
	}
	public String getPassengerMobile()
	{
		return this.mPassengerMobile;
	}
	public String getDistance()
	{
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(mDistance) ;
	}
	public String getCreatedAt()
	{
		return this.mCreatedAt;
	}
	public String getCreatedAt(String sDateFormat)
	{
		SimpleDateFormat formatter1= new SimpleDateFormat(sDateFormat);		
		return formatter1.format(mCreatedDate);		
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
		mSource					= JsonHelper.getString(obj, TaxiRequestApiConstant.SOURCE);
		if (mDriverLat > 0 && mDriverLng > 0 && mPassengerLat>0 && mPassengerLng>0){
			GeoPoint g1 = new GeoPoint((int)(mDriverLat*1e6),(int)(mDriverLng *1e6));
			GeoPoint g2 = new GeoPoint((int)(mPassengerLat*1e6),(int)(mPassengerLng*1e6));
			mDistance = (float) (DistanceUtil.getDistance(g1, g2)/1000);
			
		}

		mCreatedAt						=JsonHelper.getString(obj, TaxiRequestApiConstant.CREATED_AT);
		
		mCreatedAt = mCreatedAt.replaceAll("\\+0([0-9]){1}\\:00", "+0$100");
		String ISO8601String = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
		SimpleDateFormat formatter = new SimpleDateFormat(ISO8601String,Locale.CHINESE);
		
		Date strtodate;
		try {
			SimpleDateFormat formatter1= new SimpleDateFormat("yyyy-MM-dd");
			strtodate = formatter.parse(mCreatedAt);
			mCreatedDate=strtodate;
			mCreatedAt = formatter1.format(strtodate);
		} catch (ParseException e) {			
			e.printStackTrace();
			Log.e(TAG,"CreateAt is Wrong....."+mCreatedAt);
		}
		
	}	
	public String getHumanStateText(){
		return this.mTaxiRequestState.getHumanText();
	}

	public boolean canCancel()
	{
		if (this.mTaxiRequestState == TaxiRequestState.Waiting_Driver_Response || this.mTaxiRequestState == TaxiRequestState.Waiting_Passenger_Confirm){
			return true;
		}
		return false;
	}

	public boolean canDialDriver()
	{
		if (this.mTaxiRequestState == TaxiRequestState.Success ){
			return true;
		}
		return false;
	}
	
	
	
	
}
