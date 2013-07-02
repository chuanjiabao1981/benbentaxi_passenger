package com.benbentaxi.passenger.login;


import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.register.RegisterActivity;
import com.benbentaxi.util.DataPreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	
	private long exitTime;
	private DataPreference mData;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		bindButton();

		//mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		
		
		
		exitTime = 0;
		
		mData = new DataPreference(this.getApplicationContext());
	}

	//@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//mEmailView.setText(mData.LoadString("user"));
		//mPasswordView.setText(mData.LoadString("pass"));
	}
	
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		if(keyCode == KeyEvent.KEYCODE_BACK && 
				event.getAction() == KeyEvent.ACTION_DOWN && exitTime == 0) {
			exitTime = System.currentTimeMillis();
	        finish();
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	private void bindButton()
	{
		Button signInBtn = (Button)findViewById(R.id.sign_in_button);
		signInBtn.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						LoginForm loginForm = new LoginForm(LoginActivity.this);
						LoginTask loginTask = new LoginTask(loginForm);
						loginTask.go();
					}
				});
		
		Button createBtn = (Button)findViewById(R.id.create_button);
		createBtn.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
						startActivity(registerIntent);
					}
				});
	}
}
