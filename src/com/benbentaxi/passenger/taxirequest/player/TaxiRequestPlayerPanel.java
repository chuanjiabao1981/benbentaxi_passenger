package com.benbentaxi.passenger.taxirequest.player;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.confirm.ConfirmTask;

public class TaxiRequestPlayerPanel {
	private final String TAG			     		= TaxiRequestPlayerPanel.class.getName();
	private PopupOverlay 		mPopupOverlay		=	null;
	private MapView				mMapView			=	null;
	private DemoApplication		mApp				=	null;
	private Handler			    mHandler			=	null;
	private TaxiRequest			mTaxiRequest		=	null;	
	private Bitmap[] 			mBmps 				= 	new Bitmap[3];  
	private TaxiRequestAudio	mTaxiRequestAudio	=	null;

	
	public TaxiRequestPlayerPanel(DemoApplication app,MapView mapView,Handler handler)
	{
		mApp 				=		app;
		mMapView			=		mapView;
		mHandler			=		handler;
		mTaxiRequestAudio	=		new TaxiRequestAudio();
		init();
	}
	public void show()
	{
		if (mPopupOverlay != null){
			BDLocation bdLocation 		= mApp.getCurrentPassengerLocation();
			mTaxiRequest 				= mApp.getCurrentTaxiRequest();
			if (mTaxiRequest == null){
				Log.w(TAG, "Can't find current TaxiRequest !!!");
				return;
			}
			if (bdLocation == null ){
				Log.w(TAG, "Can't find current Location !!!");
				return;
			}
			
			GeoPoint ptTAM = new GeoPoint((int)( bdLocation.getLatitude()* 1E6), (int) (bdLocation.getLongitude() * 1E6));
			Log.d(TAG,"show the popup window before ...................................."+mMapView.getOverlays().size());
			mPopupOverlay.showPopup(mBmps, ptTAM, 64);
			Log.d(TAG,"show the popup window after ...................................."+mMapView.getOverlays().size());

		}
	}
	public void hide()
	{
		hidePop();
		stop();
	}
	public void stop()
	{
		if (mTaxiRequestAudio != null){
			mTaxiRequestAudio.release();
		}

	}
	private void hidePop()
	{
		if (mPopupOverlay != null){
			Log.d(TAG,"map overlay size before hide popupOverlay:"+mMapView.getOverlays().size());
			mPopupOverlay.hidePop();
			/*
			 * hide之后必须再次把它加进去
			 * 否则，mPopupOverlay的PopupClickListener不能用，原因不明
			 * 如果升级baidu map api 一定要测试重复打车是否可以在地图上展示。
			 */
		    mMapView.getOverlays().add(mPopupOverlay);
			Log.d(TAG,"map overlay size after hide popupOverlay:"+mMapView.getOverlays().size());
		}
	}

	private void init()
	{
		mPopupOverlay = getPopupOverlay();
	    try {
			mBmps[0] = BitmapFactory.decodeStream(mApp.getAssets().open("panel_play.png"));
			mBmps[1] = BitmapFactory.decodeStream(mApp.getAssets().open("panel_current_location.png"));
			mBmps[2] = BitmapFactory.decodeStream(mApp.getAssets().open("panel_cancel.png"));
		} catch (IOException e) {
			Log.e(TAG,"open bmp for popup fail!");
			e.printStackTrace();
		}  

	}
	
	private PopupOverlay getPopupOverlay()
	{
		mPopupOverlay	=	new PopupOverlay(mMapView,new  PopupClickListener(){

			@Override
			public void onClickedPopup(int arg0) {
				if (arg0 == 0){
					mTaxiRequestAudio.play();
				}
				if (arg0 == 2){
					ConfirmTask  confirmTask = new ConfirmTask(mApp,mHandler,false);
					confirmTask.go();
				}
				Log.i(TAG,"click index is :..........................................."+arg0);
			}
			
		}
		);
		Log.d(TAG,"map overlay size before add popupOverlay:"+mMapView.getOverlays().size());
	    mMapView.getOverlays().add(mPopupOverlay);
		Log.d(TAG,"map overlay size after add popupOverlay:"+mMapView.getOverlays().size());
		return mPopupOverlay;
	}
	
	
	
	
}
