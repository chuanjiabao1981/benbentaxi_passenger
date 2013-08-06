package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;


import android.os.Handler;

import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.location.LocationOverlayDemo;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;

public class WaitingConfirmStateHandler  implements StateChangeHandler{
	private final static String TAG = WaitingConfirmStateHandler.class.getName();
	@Override
	public void handler(DemoApplication app,Handler handler,TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		handler.sendMessage(handler.obtainMessage(LocationOverlayDemo.MSG_HANDLE_TAXIREQUEST_DRIVER_RESPONSE,old));
		
	}
}
