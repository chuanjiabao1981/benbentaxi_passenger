package com.benbentaxi.passenger.taxirequest.create;


import com.baidu.mapapi.map.LocationData;
import com.benbentaxi.api.ApiConstant;
import com.benbentaxi.api.ViewForm;
import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.DemoApplication;

import android.app.Activity;
import android.util.Log;
import android.view.View;

public class CeateTaxiRequestForm extends ViewForm{
	//private final String TAG			     = RegisterForm.class.getName();
	private String strLatAndLng="";
	private String strAudio="";
	private String strMobile="";
	
	public CeateTaxiRequestForm(Activity activity) {			
		super(activity);		
		DemoApplication app = (DemoApplication)activity.getApplication();
		
		LocationData curloc=app.getCurrentPassengerLocation();
		String strLat=Double.toString(curloc.longitude);
		String strLng=Double.toString(curloc.latitude);
		strLatAndLng= strLat+"|"+strLng;
		
		strMobile= (app.getCurrentPassenger()!=null)? app.getCurrentPassenger().getMobile():"";
		
		strAudio=((CreateTaxiRequestActivity)activity).getAudioFile2String();
	}
	protected void init()
	{
		addControl(ApiConstant.BASE,R.id.destination);
		addControl(CreateTaxiRequestApiConstant.DESTINATION,R.id.destination);
		addControl(CreateTaxiRequestApiConstant.SOURCE,R.id.source);		
		addControl(CreateTaxiRequestApiConstant.PASSENGER_MOBILE,R.id.destination);
		addControl(CreateTaxiRequestApiConstant.VOICE,R.id.destination);
	}
	@Override
	protected int getProgressStatusView() {
		return R.id.deal_taxirequest;
	}
	@Override
	protected int getFormView() {
		return R.id.create_form;
	}
	
	
	public String getLatAndLng()
	{
		return strLatAndLng;
	}
	
	public void setAudio(String strAudioString)
	{
		strAudio=strAudioString;
	}
	
	public String getAudio()
	{
		return strAudio;
	}
	
	public String getMobile()
	{
		return strMobile;		
	}
	 
}
