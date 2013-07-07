package com.benbentaxi.passenger.taxirequest.create;


import com.benbentaxi.api.ApiConstant;
import com.benbentaxi.api.ViewForm;
import com.benbentaxi.passenger.R;

import android.app.Activity;
import android.view.View;

public class CeateTaxiRequestForm extends ViewForm{
	//private final String TAG			     = RegisterForm.class.getName();
	private String strXY="";
	private String strAudio="";
	
	public CeateTaxiRequestForm(Activity activity) {
		super(activity);		
	}
	protected void init()
	{
		addControl(ApiConstant.BASE,R.id.want_to);
		addControl(CreateTaxiRequestApiConstant.WANT_TO,R.id.want_to);
		addControl(CreateTaxiRequestApiConstant.START_FROM,R.id.start_from);		
		
	}
	@Override
	protected int getProgressStatusView() {
		return R.id.deal_progress;
	}
	@Override
	protected int getFormView() {
		return R.id.create_form;
	}
	
	public void setXY(String strxy)
	{
		strXY=strxy;
	}
	
	public String getXY()
	{
		return strXY;
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
		return "13570888761";//this.getControlVal(CreateTaxiRequestApiConstant.DRIVER_MOBILE);
	}
	 
}
