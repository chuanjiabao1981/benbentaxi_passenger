package com.benbentaxi.common.api;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.benbentaxi.common.ApiConstant;
import com.benbentaxi.common.SysErrorMessage;


public abstract class FormResponse {
	private final String TAG			     = FormResponse.class.getName();

	private String mRStr;
	private Object mRjson = null;
	private String mSysErrorMessage = null;
	private ViewForm mViewForm = null;
	private RESULT_JSON_TYPE mRJsonType = null;
	enum RESULT_JSON_TYPE {JSONObject,JSONArray};

	public  abstract void parser();
	public FormResponse(ViewForm viewForm,String rStr)
	{
		this.mRStr = rStr;
		this.mViewForm = viewForm;
		init();
	}
	public boolean hasError()
	{
		if (hasSysError()){
			dealError();
			return true;
		}
		if (hasAppError()){
			dealError();
			return true;
		}
		return false;
	}
	protected Object getJsonResult()
	{
		return mRjson;
	}
	private ViewForm getViewForm()
	{
		return mViewForm;
	}
	private RESULT_JSON_TYPE getResponseJsonType()
	{
		return mRJsonType;
	}
	private void init()
	{
		JSONTokener jsParser = new JSONTokener(mRStr);
		try {
			if (mRStr.startsWith("[")){
				mRJsonType = RESULT_JSON_TYPE.JSONArray;
			}else if (mRStr.startsWith("{")){
				mRJsonType = RESULT_JSON_TYPE.JSONObject;
			}
			mRjson = jsParser.nextValue();
			
		} catch (JSONException e) {
			setSysErrorMessage(SysErrorMessage.ERROR_API_DATA_ERROR);
		}catch (Exception e){
			setSysErrorMessage(SysErrorMessage.ERROR_NET_WORK);
		}
	}
	private void setSysErrorMessage(String m)
	{
		this.mSysErrorMessage = m;
	}
	private String getSysErrorMesssage()
	{
		return this.mSysErrorMessage;
	}
	private boolean hasSysError()
	{
		if (mSysErrorMessage != null){
			return true;
		}
		return false;
	}
	private boolean hasAppError()
	{
		if (getResponseJsonType() == RESULT_JSON_TYPE.JSONObject && ((JSONObject)getJsonResult()).has(ApiConstant.ERROR))
			return true;
		return false;
	}
	private void dealError()
	{
		if (hasSysError()){
			getViewForm().setControlFieldError(ApiConstant.BASE, getSysErrorMesssage());
			return;
		}
		if (hasAppError()){
			JSONObject err;
			try {
				err = ((JSONObject)getJsonResult()).getJSONObject(ApiConstant.ERROR);
				@SuppressWarnings("rawtypes")
				Iterator i = err.keys();
				while(i.hasNext()){
					String k = (String)i.next();
					String v = err.getJSONArray(k).getString(0);
					if (getViewForm() != null){
						getViewForm().setControlFieldError(k, v);
					}
				}
			} catch (JSONException e) {
				Log.e(TAG, "解析应用层错误数据出错(JSON)!");
			} catch (Exception e) {
				Log.e(TAG, "解析应用层错误数据出错!");
			}
		}
	}
	

}
