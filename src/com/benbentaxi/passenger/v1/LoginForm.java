package com.benbentaxi.passenger.v1;

import android.app.Activity;
import android.view.View;

import com.benbentaxi.common.ApiConstant;
import com.benbentaxi.common.api.ViewForm;
import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.register.RegisterApiConstant;

public class LoginForm extends ViewForm{

	public LoginForm(Activity activity) {
		super(activity);
	}

	@Override
	protected void init() {
		addControl(ApiConstant.BASE,R.id.login_mobile);
		addControl(LoginApiConstant.PASSWORD,R.id.login_password);
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

}
