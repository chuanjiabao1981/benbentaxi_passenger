package com.benbentaxi.passenger.register;


import com.benbentaxi.api.ApiConstant;
import com.benbentaxi.api.ViewForm;
import com.benbentaxi.passenger.R;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;

public class RegisterForm extends ViewForm{
	private final String TAG			     = RegisterForm.class.getName();
	private Button mGetVerifyCodeButton 	 = null;
	public RegisterForm(Activity activity) {
		super(activity);
	}
	public RegisterForm(Activity activity,Button button)
	{
		super(activity);
		mGetVerifyCodeButton = button;
	}
	protected void init()
	{
		addControl(ApiConstant.BASE,R.id.register_mobile);
		addControl(RegisterApiConstant.MOBILE,R.id.register_mobile);
		addControl(RegisterApiConstant.PASSWORD,R.id.register_password);
		addControl(RegisterApiConstant.PAWWWORD_CONFIRM,R.id.register_password_confirm);
		addControl(RegisterApiConstant.NAME,R.id.register_name);
		addControl(RegisterApiConstant.VERIFY_CODE,R.id.verify_code);
		addSpinnerControl(RegisterApiConstant.TENAT, R.id.tenant_item);
	}
	@Override
	protected int getProgressStatusView() {
		return R.id.register_progress;
	}
	@Override
	protected int getFormView() {
		return R.id.register_form;
	}
	
	public String getMobile()
	{
		return this.getControlVal(RegisterApiConstant.MOBILE);
	}
	public String getPass()
	{
		return this.getControlVal(RegisterApiConstant.PASSWORD);
	}
	public void disableGetVerifyCode(){
		if (mGetVerifyCodeButton != null){ 
			Log.d(TAG,"set button disable");
			mGetVerifyCodeButton.setText("已发送");
			mGetVerifyCodeButton.setClickable(false);
		}
	}
	public void enableGetVerifyCode(){
		if (mGetVerifyCodeButton != null){ 
			Log.d(TAG,"set button enable");
			mGetVerifyCodeButton.setClickable(true);
		}

	}
	 
}
