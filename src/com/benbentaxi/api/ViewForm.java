package com.benbentaxi.api;

import java.util.HashMap;
import java.util.Map;

import com.benbentaxi.util.DataPreference;



import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public abstract class ViewForm {
	private Map< String, ViewControl > mControls = null;	// 在html规范中中每个input都叫做control，沿用此名称
	private Activity mActivity 				 = null;
	private final String TAG			     = ViewForm.class.getName();
	
	private View mProgressStatusView 			 = null;
	private View mFormView 						 = null;
	
	public ViewForm(Activity activity)
	{
		mActivity = activity;
		mControls   = new HashMap< String, ViewControl >();
		init();
		mProgressStatusView = this.mActivity.findViewById(getProgressStatusView());
		mFormView 			= this.mActivity.findViewById(getFormView());
	}
	
	public String getControlVal(String name)
	{
		if (mControls.containsKey(name)){
			return mControls.get(name).getValue();
		}else{
			Log.e(TAG, "不存在此control " + name);
			return "";
		}
	}
	
	public void setControlFieldError(String name,String errmsg)
	{
		if (mControls.containsKey(name)){
			mControls.get(name).setError(errmsg);
		}else{
			Toast.makeText(this.mActivity,errmsg,Toast.LENGTH_SHORT).show();
//			Log.e(TAG, "不存在此control " + name);
			return;
		}
	}
	@Deprecated
	public DataPreference getDataPreference()
	{
		return new DataPreference(this.mActivity.getApplicationContext());
	}
	public Activity getActivity()
	{
		return this.mActivity;
	}
	
	//@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		/*
		 * 2.3.3版本rom不支持
		 *
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = this.mActivity.getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mProgressStatusView.setVisibility(View.VISIBLE);
			mProgressStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mFormView.setVisibility(View.VISIBLE);
			mFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {

							mFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
		*/
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		//}
	}
	
	protected abstract void init();
	protected abstract int getProgressStatusView();
	protected abstract int getFormView();
	protected int getControlViewId(String name)
	{
		return mControls.get(name).getResourceId();
	}
	
	protected View findViewById(int id)
	{
		return this.mActivity.findViewById(id);
	}

	protected void addControl(String name,int id)
	{
		mControls.put(name, new TextInputControl(this.mActivity,id));
		mControls.get(name).clearError();
	}
	protected void addSpinnerControl(String name,int id)
	{
		mControls.put(name, new SpinnerInputControl(this.mActivity,id));
		mControls.get(name).clearError();
	}
}
