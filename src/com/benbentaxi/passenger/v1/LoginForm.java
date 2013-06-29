package com.benbentaxi.passenger.v1;

import android.app.Activity;

import com.benbentaxi.api.ApiConstant;
import com.benbentaxi.api.ViewForm;
import com.benbentaxi.passenger.R;

public class LoginForm extends ViewForm{

	public LoginForm(Activity activity) {
		super(activity);
	}

	@Override
	protected void init() {
		addControl(ApiConstant.BASE,R.id.login_mobile);
		addControl(LoginApiConstant.MOBILE,R.id.login_mobile);
		addControl(LoginApiConstant.PASSWORD,R.id.login_password);
	}

	@Override
	protected int getProgressStatusView() {
		return R.id.login_progress;
	}

	@Override
	protected int getFormView() {
		return R.id.login_form;
	}
	
	public String getMobile()
	{
		return getControlVal(LoginApiConstant.MOBILE);
	}

}
