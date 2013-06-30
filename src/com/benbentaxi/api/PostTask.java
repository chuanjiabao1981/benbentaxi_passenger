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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public abstract class PostTask extends AsyncTask<String, Integer, Boolean> {
	public final static String TYPE_POST = "post";
	
	public final static int REQUEST_SEND = 10;
	public final static int REQUEST_DONE = 100;
	
	protected String _errmsg = null;
	protected List<NameValuePair> sess_params;
	protected String post_param;
	protected HttpContext hcon;
	public byte[] result;
	protected String _type;
	protected CookieStore cs;
	private List<Header> _headers;
	protected HttpResponse _httpResp;
	
	protected PostTask() {
		cs = new BasicCookieStore();
		sess_params = new ArrayList<NameValuePair>();
		post_param = new String();
		_headers = new ArrayList<Header>();
		result = new byte[1];
		initHeaders("Content-Type", "application/json");
	}
	
		
	protected abstract String getPostParams();
	protected abstract String getApiUrl();
	public    abstract void   go();
	
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
	
	@Override
	protected Boolean doInBackground(String... params) {
		// attempt authentication against a network service.
		String urlstr = getApiUrl();
		String useragent = "taxi post api";

		try {
			hcon = new BasicHttpContext();
			hcon.setAttribute(ClientContext.COOKIE_STORE, cs);
			
			HttpParams httpparam = new BasicHttpParams();
			HttpProtocolParams.setUserAgent(httpparam, useragent);
			post_param = getPostParams();
			HttpPost httpRequest = new HttpPost(urlstr);
			for ( Header hh : _headers ) {
				httpRequest.setHeader(hh);
			}
			if ( sess_params.size() > 0 ) {
					httpRequest.setEntity(new UrlEncodedFormEntity(sess_params,"UTF-8"));
			} else if ( post_param.length() > 0 ) {
					httpRequest.setEntity(new StringEntity(post_param,"UTF-8"));
		    }

			_httpResp = new DefaultHttpClient(httpparam).execute(httpRequest, hcon);

			publishProgress(REQUEST_SEND);
			result = EntityUtils.toByteArray(_httpResp.getEntity());
			publishProgress(REQUEST_DONE);

		} catch ( Exception e ) {
			e.printStackTrace();
			_errmsg = "网络错误，请检查网络是否正常"; //"stage 3: "+e.toString();
			return false;
		}

		return true;
	}


	@Override
	protected void onCancelled() {
		//showProgress(false);
	}
	
	
	public String getErrorMsg()
	{
		return this._errmsg;
	}
	public byte[] toByte() {
		return result;
	}
	
	public String getResult()
	{
		return new String(result);
	}
	public int getHttpCode() {
		if ( _httpResp != null ) {
			return _httpResp.getStatusLine().getStatusCode();
		} else {
			return -1;
		}
	}
}
