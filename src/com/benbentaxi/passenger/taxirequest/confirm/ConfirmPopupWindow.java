package com.benbentaxi.passenger.taxirequest.confirm;

import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.DemoApplication;
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
	private final static String BTN_NEG_TEXT= "重新打车";

	private View mView;
	private TextView mTitle;
	private TextView mContent;
	private Button mBtnPos, mBtnNeg;
	private Activity mActivity;
	private ProgressBar mProgressBar;
	private long 	    mSecs;
	
	private Handler		mHandler;
	
	private CountDownTimer mCountDownTimer;
	private TextView	   mCountDonwTimerText;


	
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
		DemoApplication mApp 		= (DemoApplication)activity.getApplicationContext();
		mView 						= this.getContentView();
		mTitle 						= (TextView)mView.findViewById(R.id.tvConfirmTitle);
    	mContent 					= (TextView)mView.findViewById(R.id.tvConfirmContent);
    	mBtnPos 					= (Button)mView.findViewById(R.id.btnConfirmOk);
    	mBtnNeg 					= (Button)mView.findViewById(R.id.btnConfirmCancel);
		String d 					=(mApp.getCurrentTaxiRequest() != null) ? mApp.getCurrentTaxiRequest().getDistance().toString() : "0";
		mCountDonwTimerText			= (TextView) mView.findViewById(R.id.confirmCountDonwTimerText);
    	mProgressBar 				= (ProgressBar)mView.findViewById(R.id.confirmProgress);
    	mSecs						= secs;
    	mHandler					= handler;
    	mCountDownTimer				= new CountDownTimer(mSecs*1000,1000){

			@Override
			public void onFinish() {
				if ( mHandler != null ) {
					mHandler.sendMessage(mHandler.obtainMessage(MSG_HANDLE_TAXIREQUEST_CONFIRM_TIMEOUT,ConfirmPopupWindow.this));
				}
			}
			@Override
			public void onTick(long millisUntilFinished) {
				mCountDonwTimerText.setText("确认时间还有:"+millisUntilFinished/1000+"秒！");
			}
    	};
    	
    	this.setOutsideTouchable(false);
    	mProgressBar.setProgress(0);
    	mProgressBar.setIndeterminate(false);

    	mTitle.setText("有司机响应，距离您约");
    	mContent.setText(d+"公里");
    	mBtnPos.setText(BTN_POS_TEXT);
    	mBtnNeg.setText(BTN_NEG_TEXT);
    	
    	

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
		mBtnNeg.setOnClickListener(mNegfunc);
		mBtnPos.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ConfirmTask confirmRequest = new ConfirmTask(ConfirmPopupWindow.this.mActivity,mHandler,true);
						confirmRequest.go();
						ConfirmPopupWindow.this.doClean();

					}
	        	}
		);
		mBtnNeg.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ConfirmTask confirmRequest = new ConfirmTask(ConfirmPopupWindow.this.mActivity,mHandler,false);
						confirmRequest.go();
						ConfirmPopupWindow.this.doClean();

					}
	        	}
		);
		showAtLocation(mView, Gravity.CENTER, 0, 0);
		mCountDownTimer.start();
	}
	public void doClean() {
		if (mCountDownTimer!=null){
			mCountDownTimer.cancel();
		}
		if ( this.isShowing() ) {
			this.dismiss();
		}
	}


	
	
	
}
