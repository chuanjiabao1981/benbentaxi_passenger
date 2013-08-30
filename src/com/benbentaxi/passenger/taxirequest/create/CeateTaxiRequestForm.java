package com.benbentaxi.passenger.taxirequest.create;


import com.baidu.location.BDLocation;
import com.benbentaxi.api.ViewForm;
import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.DemoApplication;

import android.app.Activity;

public class CeateTaxiRequestForm extends ViewForm{
	//private final String TAG			     = RegisterForm.class.getName();
	private String mAudio					 ="";
	private String mMobile					 ="";
	private BDLocation mBDLocation 			 = null;
	DemoApplication mApp					 = null; 
	public CeateTaxiRequestForm(Activity activity) {			
		super(activity);		
		mApp					= (DemoApplication)activity.getApplication();
		mBDLocation				= mApp.getCurrentPassengerLocation();
		mMobile					= (mApp.getCurrentPassenger()!=null)? mApp.getCurrentPassenger().getMobile():"";
		mAudio					= ((CreateTaxiRequestActivity)activity).getAudioFile2String();
	}
	protected void init()
	{
		
	}
	@Override
	protected int getProgressStatusView() {
		return R.id.deal_taxirequest;
	}
	@Override
	protected int getFormView() {
		return R.id.create_form;
	}
	
	public String getLat()
	{
		if (mBDLocation == null){
			return "0";
		}else{
			return String.valueOf(mBDLocation.getLatitude());
		}
	}
	public String getLng()
	{
		if (mBDLocation == null){
			return "0";
		}else{
			return String.valueOf(mBDLocation.getLongitude());
		}
	}
	public String getSource()
	{
		if (mBDLocation == null){
			return "";
		}else if (mBDLocation.getAddrStr() != null){
			return mBDLocation.getAddrStr();
		}else{
			return "";
		}
	}
	public String getCity()
	{
		if (mBDLocation == null){
			return "";
		}else if (mBDLocation.getCity() != null){
			return mBDLocation.getCity();
		}else{
			return "";
		}
	}
	public void setAudio(String strAudioString)
	{
		mAudio=strAudioString;
	}
	
	public String getAudio()
	{
		return mAudio;
	}
	
	public String getMobile()
	{
		return mMobile;		
	}
	 
}
