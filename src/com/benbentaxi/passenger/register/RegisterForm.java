package com.benbentaxi.passenger.register;


import com.benbentaxi.common.ApiConstant;
import com.benbentaxi.common.api.ViewForm;
import com.benbentaxi.passenger.R;

import android.app.Activity;
import android.view.View;

public class RegisterForm extends ViewForm{
	//private final String TAG			     = RegisterForm.class.getName();

	public RegisterForm(Activity activity) {
		super(activity);
	}
	protected void init()
	{
		addControl(ApiConstant.BASE,R.id.mobile);
		addControl(RegisterApiConstant.MOBILE,R.id.mobile);
		addControl(RegisterApiConstant.PASSWORD,R.id.password);
		addControl(RegisterApiConstant.PAWWWORD_CONFIRM,R.id.password_confirm);
		addControl(RegisterApiConstant.NAME,R.id.name);
	}
	@Override
	protected View getProgressStatusView() {
		return this.findViewById(R.id.register_progress);
	}
	@Override
	protected View getFormView() {
		return this.findViewById(R.id.register_form);
	}
}
