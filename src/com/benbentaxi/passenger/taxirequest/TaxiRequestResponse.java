package com.benbentaxi.passenger.taxirequest;

import android.util.Log;

import com.benbentaxi.api.Response;

public class TaxiRequestResponse extends Response{
	private String TAG = TaxiRequestResponse.class.getName();

	public TaxiRequestResponse(String r) {
		super(r);
	}

	@Override
	public void parser() {
		
	}

	@Override
	protected void dealError(String key, String val) {
		Log.e(TAG,key+":"+val);
	}


	
}
