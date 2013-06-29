package com.benbentaxi.passenger.register;

import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.login.LoginActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends Activity {
	//private String TAG = RegisterActivity.class.getName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		buttonBind();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}
	
	private void buttonBind()
	{
		Button button = (Button)findViewById(R.id.register_button);
		button.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						RegisterForm registerForm = new RegisterForm(RegisterActivity.this);
						RegisterTask registerTask = new RegisterTask(registerForm);
						registerTask.go();
					}
		});
		button 		  = (Button)findViewById(R.id.goback_button);
		button.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
						startActivity(loginIntent);
					}
		});
	}

}
