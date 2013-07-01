package com.benbentaxi.passenger.taxirequest;

public interface StateChangeHandler {
	public void handler(TaxiRequest oldState,TaxiRequestResponse newState);
}
