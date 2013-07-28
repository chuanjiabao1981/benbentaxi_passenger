package com.benbentaxi.passenger.register;

import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.login.LoginActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	//private String TAG = RegisterActivity.class.getName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_register);
		Spinner spinner 					= (Spinner) findViewById(R.id.tenant_item);
		ArrayAdapter<CharSequence> adapter 	= ArrayAdapter.createFromResource(this,
		        R.array.tenants_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		Toast.makeText(this, spinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
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
