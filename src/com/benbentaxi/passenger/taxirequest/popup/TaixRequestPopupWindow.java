package com.benbentaxi.passenger.taxirequest.popup;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.benbentaxi.passenger.location.DemoApplication;

public class TaixRequestPopupWindow {
	private final String TAG			     		= TaixRequestPopupWindow.class.getName();
	private PopupOverlay 		mPopupOverlay		=	null;
	private MapView				mMapView			=	null;
	private DemoApplication		mApp				=	null;
	private Handler			    mHandler			=	null;
	
	
	public TaixRequestPopupWindow(Activity a,MapView mapView,Handler handler)
	{
		mApp 			=		(DemoApplication) a.getApplication();
		mMapView		=		mapView;
		mHandler		=		handler;
	}
	
	
	private void init()
	{
		mPopupOverlay	=	new PopupOverlay(mMapView,new  PopupClickListener(){

			@Override
			public void onClickedPopup(int arg0) {
				
				Log.d(TAG,"click index is :"+arg0);
				
			}
			
		}
		);
	}
}
