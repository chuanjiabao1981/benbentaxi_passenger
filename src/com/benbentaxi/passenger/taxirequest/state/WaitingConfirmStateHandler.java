package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.benbentaxi.passenger.location.LocationOverlayDemo;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;

public class WaitingConfirmStateHandler  implements StateChangeHandler{
	private final static String TAG = WaitingConfirmStateHandler.class.getName();
	@Override
	public void handler(Activity activity,Handler handler,TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		// this
		handler.sendMessage(handler.obtainMessage(LocationOverlayDemo.MSG_HANDLE_TAXIREQUEST_DRIVER_RESPONSE,old));
		
	}
}
