package com.benbentaxi.passenger.taxirequest;

public enum TaxiRequestState {
	Waiting_Driver_Response(0,"等待司机响应"),
	Waiting_Passenger_Confirm(1,"等待乘客确认"),
	Success(2,"打车成功"),
	TimeOut(3,"请求超时"),
	Canceled_By_Passenger(4,"取消打车请求"),	
	UNKONW(5,"未知状态");
	
	private int mIndex;
	private String mHumanText;
	private TaxiRequestState(int index,String text)
	{
		this.mIndex = index;
		this.mHumanText = text;
	}
	public int getIndex()
	{
		return this.mIndex;
	}
	public String getHumanText()
	{
		return this.mHumanText;
	}
}
