package com.benbentaxi.passenger.taxirequest;

import java.util.HashMap;
import java.util.Map;

public class SimpleStateMachine {
	public Map<String,StateChangeHandler> mStateHandlerSets = null;
	public StateChangeHandler mDefaultHandler				= null;
	public SimpleStateMachine(StateChangeHandler defaultHandler)
	{
		mStateHandlerSets 			= new HashMap<String,StateChangeHandler>();
		mDefaultHandler 		= defaultHandler;
	}
	//设置从from状态，转化到to状态，要调用的handler
	public void addHandler(TaxiRequestState from,TaxiRequestState to,StateChangeHandler handler)
	{
		mStateHandlerSets.put( from.toString() + to.toString(),handler);
	}
	//设置从（非）s状态，装哈u到s状态，要调用的handler
	public void addHandler(TaxiRequest s,StateChangeHandler handler)
	{
		mStateHandlerSets.put(s.toString(), handler);
	}
	
	public StateChangeHandler findHandler(TaxiRequestState from,TaxiRequestState to)
	{
		StateChangeHandler stateChangeHandler = null;
		if (from != to){
			stateChangeHandler = mStateHandlerSets.get(to.toString());
			if (stateChangeHandler != null){
				return stateChangeHandler;
			}
		}
		stateChangeHandler = mStateHandlerSets.get(from.toString()+to.toString());
		if (stateChangeHandler != null){
			return stateChangeHandler;
		}

		return mDefaultHandler;
	}
	
}
