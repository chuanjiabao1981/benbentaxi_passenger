package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;


import android.app.Activity;

import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;
import com.benbentaxi.passenger.taxirequest.confirm.ConfirmPopupWindow;

public class WaitingConfirmStateHandler  implements StateChangeHandler{
//	private final static String TAG = WaitingConfirmStateHandler.class.getName();
	@Override
	public void handler(Activity activity,TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		ConfirmPopupWindow confirmPopupWindow = new ConfirmPopupWindow(activity);
    	confirmPopupWindow.show();
	}

}
