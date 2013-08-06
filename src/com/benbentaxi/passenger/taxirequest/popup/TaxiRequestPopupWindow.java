package com.benbentaxi.passenger.taxirequest.popup;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;

public class TaxiRequestPopupWindow {
	private final String TAG			     		= TaxiRequestPopupWindow.class.getName();
	private PopupOverlay 		mPopupOverlay		=	null;
	private MapView				mMapView			=	null;
	private DemoApplication		mApp				=	null;
	private Handler			    mHandler			=	null;
	private TaxiRequest			mTaxiRequest		=	null;	
	private Bitmap[] 			mBmps 				= 	new Bitmap[3];  

	
	public TaxiRequestPopupWindow(DemoApplication app,MapView mapView,Handler handler)
	{
		mApp 			=		app;
		mMapView		=		mapView;
		mHandler		=		handler;
		init();
	}
	public void showPopup()
	{
		
		if (mPopupOverlay != null){
			BDLocation bdLocation 		= mApp.getCurrentPassengerLocation();
			mTaxiRequest 				= mApp.getCurrentTaxiRequest();
			if (mTaxiRequest == null){
				Log.e(TAG, "Can't find current TaxiRequest !!!");
				return;
			}
						
			GeoPoint ptTAM = new GeoPoint((int)( bdLocation.getLatitude()* 1E6), (int) (bdLocation.getLongitude() * 1E6));
			
			mPopupOverlay.showPopup(mBmps, ptTAM, 64);
			Log.d(TAG,"show the popup window....................................");
		}
	}
	public void hidePop()
	{
		if (mPopupOverlay != null){
			mPopupOverlay.hidePop();
		}
	}
	public PopupOverlay getPopupOverlay()
	{
		return mPopupOverlay;
	}
	private void init()
	{
		mPopupOverlay	=	new PopupOverlay(mMapView,new  PopupClickListener(){

			@Override
			public void onClickedPopup(int arg0) {
				
				Log.i(TAG,"click index is :..........................................."+arg0);
				Log.i(TAG,"click index is :...........................................");
				Log.i(TAG,"click index is :...........................................");
				Log.i(TAG,"click index is :...........................................");
				Log.i(TAG,"click index is :...........................................");
			}
			
		}
		);
	    mMapView.getOverlays().add(mPopupOverlay);
	    try {
			mBmps[0] = BitmapFactory.decodeStream(mApp.getAssets().open("steering.png"));
			mBmps[1] = BitmapFactory.decodeStream(mApp.getAssets().open("steering.png"));
			mBmps[2] = BitmapFactory.decodeStream(mApp.getAssets().open("steering.png"));
		} catch (IOException e) {
			Log.e(TAG,"open bmp for popup fail!");
			e.printStackTrace();
		}  

	}
	
	
	
	
}
