package com.benbentaxi.passenger.taxirequest.state;

import org.json.JSONObject;


import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;
import com.benbentaxi.passenger.taxirequest.confirm.ConfirmPopupWindow;

public class WaitingConfirmStateHandler  implements StateChangeHandler{

	@Override
	public void handler(TaxiRequest old, TaxiRequestResponse newState) {
		old.init((JSONObject) newState.getJsonResult());
		ConfirmPopupWindow confirmPopupWindow = new ConfirmPopupWindow(old.getActivity());
    	confirmPopupWindow.show();
	}

}
