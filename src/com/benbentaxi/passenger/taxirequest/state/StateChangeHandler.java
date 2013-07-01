package com.benbentaxi.passenger.taxirequest.state;

import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestResponse;

public interface StateChangeHandler {
	public void handler(TaxiRequest old,TaxiRequestResponse newState);
}
