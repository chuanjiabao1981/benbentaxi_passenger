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
	@SuppressWarnings("deprecation")
	@Override
	public void handler(Activity activity,Handler handler,TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		// this
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(ns);

		int icon = android.R.drawable.ic_dialog_info;        
		CharSequence tickerText = "有司机响应!"; // ticker-text
		long when = System.currentTimeMillis();       
		CharSequence contentTitle = "有司机响应";  
		CharSequence contentText = "司机:"+old.getDriverMobile()+"响应请求，请确认!";      
		Intent notificationIntent = new Intent(activity.getApplicationContext(), LocationOverlayDemo.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(activity.getApplicationContext(), contentTitle, contentText, contentIntent);
		notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS;
		Log.d(TAG,"notification default settings:"+notification.defaults);
		final int HELLO_ID = 1;
		mNotificationManager.notify(HELLO_ID, notification);        Log.d(TAG,"notify user");
		handler.sendMessage(handler.obtainMessage(LocationOverlayDemo.MSG_HANDLE_TAXIREQUEST_DRIVER_RESPONSE));
		
//		ConfirmPopupWindow confirmPopupWindow = new ConfirmPopupWindow(activity,handler,30);
//    	confirmPopupWindow.show();
	}
}
