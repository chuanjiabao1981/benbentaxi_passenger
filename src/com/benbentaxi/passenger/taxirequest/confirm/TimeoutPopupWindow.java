package com.benbentaxi.passenger.taxirequest.confirm;

import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.util.PopupWindowSize;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TimeoutPopupWindow extends PopupWindow{
	private TaxiRequest mTaixRequest;
	private View mView;
	private TextView mTitle;
	private Button mBtnPos;


	public TimeoutPopupWindow(Context c)
	{
		super(c);
	}
	public 	TimeoutPopupWindow(Activity activity,TaxiRequest taxiRequest)
	{
		super(activity.getLayoutInflater().inflate(R.layout.timeout_dialog, null),
			  PopupWindowSize.getPopupWindoWidth(activity),
			  PopupWindowSize.getPopupWindowHeight(activity));
		mView 							= this.getContentView();
		mTaixRequest 					= taxiRequest;
		mTitle 							= (TextView)mView.findViewById(R.id.timeout_title);
		mBtnPos 						= (Button) mView.findViewById(R.id.timeout_ok);
		mTitle.setText(mTaixRequest.getId() + " 确认已经超时，请您重新打车！");
		bindButton();
	}
	public void show()
	{
		showAtLocation(mView, Gravity.CENTER, 0, 0);
	}
	private void bindButton()
	{
		mBtnPos.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if ( TimeoutPopupWindow.this.isShowing() ) {
					TimeoutPopupWindow.this.dismiss();
				}
			}
		}
		);

	}
	
	
	
}
