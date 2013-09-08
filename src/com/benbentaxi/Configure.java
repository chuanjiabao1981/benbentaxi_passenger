package com.benbentaxi;

public class Configure {

	public String getService()
	{
		return getHost()+":80";
	}
	public String getHost()
	{
		return "yangquan.benbentaxi.com";
	}
	public String getEquipmentId()
	{
		return "xxxxxxxx";
	}
	public String getOsVersion()
	{
		return "android "+android.os.Build.VERSION.RELEASE;
	}
	
	public String getClientVersion()
	{
		//这个和分支一致
		return "passenger-"+"0.1";
	}
}
