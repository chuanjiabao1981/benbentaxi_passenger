package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;

public class TimeOutStateHandler implements StateChangeHandler {
	private static final String TAG = TimeOutStateHandler.class.getName();
	@Override
	public void handler(Activity activity, Handler handler,TaxiRequest old,
			TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		Log.d(TAG,((JSONObject) newState.getJsonResult()).toString());
//		Toast.makeText(activity.getApplicationContext(), "请求"+old.getId()+","+old.getHumanStateText(), Toast.LENGTH_LONG).show();
		((DemoApplication)activity.getApplication()).setCurrentTaxiRequest(null);
//		TimeoutPopupWindow timeoutPopupWindow = new TimeoutPopupWindow(activity,old);
//		timeoutPopupWindow.show();
	}

}
