package com.benbentaxi.passenger.taxirequest.confirm;

import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.location.LocationOverlayDemo;
import com.benbentaxi.util.PopupWindowSize;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ConfirmPopupWindow extends PopupWindow{
	
	public final static int MSG_HANDLE_TAXIREQUEST_CONFIRM_TIMEOUT = 200001; 
	
//	private final static String TAG = ConfirmPopupWindow.class.getName();
	private final static String BTN_POS_TEXT= "确认";

	private View mView;
	private TextView mTitle;
	private TextView mContent;
	private Button mBtnPos;
	private Activity mActivity;
	private ProgressBar mProgressBar;
	private long 	    mSecs;
	
	private Handler		mHandler;
	private DemoApplication mApp;


	
	private View.OnClickListener mPosfunc = null, mNegfunc = null;

	
	public ConfirmPopupWindow(Context c)
	{
		super(c);
	}
	public ConfirmPopupWindow(Activity activity,Handler handler ,long secs)
	{
		super(activity.getLayoutInflater().inflate(R.layout.confirm_dialog, null),PopupWindowSize.getPopupWindoWidth(activity),
				PopupWindowSize.getPopupWindowHeight(activity),true);
		mActivity 			 		= activity;
		mApp 						= (DemoApplication)activity.getApplicationContext();
		mView 						= this.getContentView();
		mTitle 						= (TextView)mView.findViewById(R.id.tvConfirmTitle);
    	mContent 					= (TextView)mView.findViewById(R.id.tvConfirmContent);
    	mBtnPos 					= (Button)mView.findViewById(R.id.btnConfirmOk);
		String d 					=(mApp.getCurrentTaxiRequest() != null) ? mApp.getCurrentTaxiRequest().getDistance().toString() : "0";
    	mProgressBar 				= (ProgressBar)mView.findViewById(R.id.confirmProgress);
    	mSecs						= secs;
    	mHandler					= handler;
    	
    	this.setOutsideTouchable(false);
    	mProgressBar.setProgress(0);
    	mProgressBar.setIndeterminate(false);

    	mTitle.setText("有司机响应，距离您约");
    	mContent.setText(d+"公里");
    	mBtnPos.setText(BTN_POS_TEXT);
    	
    	

    	mPosfunc = mNegfunc = new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if ( ConfirmPopupWindow.this.isShowing() ) {
					ConfirmPopupWindow.this.dismiss();
				}
			}
		};
	}
	
	public void show()
	{
		mBtnPos.setOnClickListener(mPosfunc);
		mBtnPos.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						ConfirmTask confirmRequest = new ConfirmTask((DemoApplication) ConfirmPopupWindow.this.mActivity.getApplication(),mHandler,true);
//						confirmRequest.go();
						ConfirmPopupWindow.this.doClean();
						mHandler.sendMessage(mHandler.obtainMessage(LocationOverlayDemo.MSG_HANDLE_TAXIREQUEST_SHOW,mApp.getCurrentTaxiRequest()));
					}
	        	}
		);
		showAtLocation(mView, Gravity.CENTER, 0, 0);
	}
	public void doClean() {
		if ( this.isShowing() ) {
			this.dismiss();
		}
	}


	
	
	
}
