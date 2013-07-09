package com.benbentaxi.api;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public abstract class GetTask extends JsonHttpTask{

	protected HttpContext hcon;
	public byte[] result;
	
	public GetTask()
	{
		result = new byte[1];
	}
	protected abstract String getApiUrl();
	public    abstract void   go();
	@Override
	protected Boolean doInBackground(String... params) {
		
		String useragent = "taxi get api";

		hcon = new BasicHttpContext();
		hcon.setAttribute(ClientContext.COOKIE_STORE, cs);
		
		HttpParams httpparam = new BasicHttpParams();
		HttpProtocolParams.setUserAgent(httpparam, useragent);
		
		HttpGet httpRequest = new HttpGet(getApiUrl());
		try {
			_httpResp = new DefaultHttpClient(httpparam).execute(httpRequest, hcon);
			for ( Header hh : _headers ) {
				httpRequest.setHeader(hh);
			}
			publishProgress(REQUEST_SEND);
			result = EntityUtils.toByteArray(_httpResp.getEntity());
			publishProgress(REQUEST_DONE);
		} catch (ClientProtocolException e) {
			_errmsg = "网络错误，请检查网络是否正常"; //"stage 3: "+e.toString();
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			_errmsg = "系统错误"; //"stage 3: "+e.toString();
			e.printStackTrace();
			return false;
		}
		if (_httpResp.getStatusLine().getStatusCode() != 200){
			_errmsg = "服务器出现异常";
			Log.d(TAG,"服务器的反回码:"+_httpResp.getStatusLine().getStatusCode());
			return false;
		}
		return true;

	}
	
	public String getResult()
	{
		return new String(result);
	}

}
