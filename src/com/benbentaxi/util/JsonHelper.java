package com.benbentaxi.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {
	public static String getString(JSONObject o,String k)
	{
		if (o == null)
			return "";

		try {
			return o.getString(k);
		} catch (JSONException e) {
			return "";
		}
	}
	
	public static float getFloat(JSONObject o,String k)
	{
		if (o == null)
			return -1f;
		
		try {
			return (float) o.getDouble(k);
		} catch (JSONException e) {
			return -1f;
		}
	}
	
	public static long getLong(JSONObject o,String k)
	{
		if (o == null)
			return -1l;
		try {
			return o.getLong(k);
		} catch (JSONException e) {
			return -1;
		}
	}
}
