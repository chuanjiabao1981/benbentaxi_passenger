package com.benbentaxi.api;


import org.apache.http.Header;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


public abstract class PostTask extends JsonHttpTask {
	
	
	protected String post_param;
	protected HttpContext hcon;
	public byte[] result;

	
	protected PostTask() {
		result = new byte[1];
	}
	
		
	protected abstract String getPostParams();
	protected abstract String getApiUrl();
	public    abstract void   go();
	
	
	
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
	
	
	public byte[] toByte() {
		return result;
	}
	
	public String getResult()
	{
		return new String(result);
	}
}
