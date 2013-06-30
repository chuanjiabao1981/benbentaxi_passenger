package com.benbentaxi.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;

import android.os.AsyncTask;

public abstract class JsonHttpTask extends AsyncTask<String, Integer, Boolean> {

	public final static int REQUEST_SEND = 10;
	public final static int REQUEST_DONE = 100;
	
	protected String _errmsg = null;
	protected List<NameValuePair> sess_params;
	protected List<Header> _headers;
	protected HttpResponse _httpResp;
	protected CookieStore cs;
	
	
	public JsonHttpTask()
	{
		cs = new BasicCookieStore();
		sess_params = new ArrayList<NameValuePair>();
		_headers = new ArrayList<Header>();
		initHeaders("Content-Type", "application/json");

	}
	protected void initCookies(String key, String val, String domain) {
		BasicClientCookie bc1 = new BasicClientCookie(key, val);
		bc1.setVersion(0);
        bc1.setDomain(domain);
        bc1.setPath("/");
        
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			final Date ed = df.parse("2050-04-23");
	        bc1.setExpiryDate(ed);
	        cs.addCookie(bc1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void initHeaders(String Key, String Val) {
		_headers.add(new BasicHeader(Key, Val));
	}
	public String getErrorMsg()
	{
		return this._errmsg;
	}
	
	public int getHttpCode() {
		if ( _httpResp != null ) {
			return _httpResp.getStatusLine().getStatusCode();
		} else {
			return -1;
		}
	}
}
