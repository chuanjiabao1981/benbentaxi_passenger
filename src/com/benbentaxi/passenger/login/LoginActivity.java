package com.benbentaxi.passenger.login;

import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.register.RegisterActivity;
import com.benbentaxi.util.CustomExceptionHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends FragmentActivity {
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	
	private long exitTime;
	
	private EditText mMobileView;
	private EditText mPasswordView;
	private DemoApplication mApp; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		bindButton();
		mMobileView 			= (EditText) findViewById(R.id.login_mobile);
		mPasswordView 		= (EditText) findViewById(R.id.login_password);
		mApp				= (DemoApplication) this.getApplication();
		//mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		exitTime = 0;
		
	}

	//@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mMobileView.setText(this.mApp.getLoginMobile());
		mPasswordView.setText(this.mApp.getLoginPass());
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
