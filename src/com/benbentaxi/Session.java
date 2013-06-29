package com.benbentaxi;

public class Session {
	private String mTokenKey;
	private String mTokenValue;
	
	public Session(String tokenKey,String tokenValue)
	{
		this.mTokenKey 		= tokenKey;
		this.mTokenValue	= tokenValue;
	}
	
	public String getTokenKey()
	{
		return this.mTokenKey;
	}
	
	public String getTokenValue()
	{
		return this.mTokenValue;
	}
}
