package com.benbentaxi.passenger.nearbydriver;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.LocationOverlayDemo;
import com.benbentaxi.passenger.location.DriverOverlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;


public class NearbyDrvierReceiver extends BroadcastReceiver {
	private LocationOverlayDemo mActivity;
	private MapView				mMapView;
	private DriverOverlay			mOverlay;
	public List<OverlayItem> 	mGeoList; 
	public List<Drawable> 	    res; 

	
	public NearbyDrvierReceiver(LocationOverlayDemo a,MapView mapView,DriverOverlay overlay)
	{
		mActivity  						= a;
		mMapView						= mapView;
		mOverlay						= overlay;
		mGeoList						= new ArrayList<OverlayItem>();
		res							    = new ArrayList<Drawable>();
		res.add(mActivity.getResources().getDrawable(R.drawable.steering));
	}
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (mActivity.getBackgroundService() != null)
			if (mActivity.getBackgroundService().getNearByDriverTrackResponse() != null){
				NearByDriverTrackResponse nearByDriverTrackResponse = mActivity.getBackgroundService().getNearByDriverTrackResponse();
				ShowCurrentNearByDrivers(nearByDriverTrackResponse);
		}
		
	}
	private void ShowCurrentNearByDrivers(NearByDriverTrackResponse nearByDriverTrackResponse) 
	{		
		//清除所有添加的Overlay
		if (mOverlay != null)
			mOverlay.removeAll();
        mGeoList.clear();
        for( int i=0; i< nearByDriverTrackResponse.getSize(); ++i ) {
        	int lat = 0, lng = 0;
        	
        	OverlayItem item = null;
	        lat = (int)(nearByDriverTrackResponse.getLat(i)*1e6);
	        lng = (int)(nearByDriverTrackResponse.getLng(i)*1e6);
        	item= new OverlayItem(new GeoPoint(lat, lng),
		        		"司机"+nearByDriverTrackResponse.getId(i),"创建时间: "+nearByDriverTrackResponse.getCreatedAt(i));		
	        
        	if ( item != null ) {
			   	item.setMarker(res.get(i%res.size()));
			   	mGeoList.add(item);
        	}
        }
    	if ( mOverlay.size() < mGeoList.size()){
    		mOverlay.addItem(mGeoList);
    	}
	    mMapView.refresh();
		Toast.makeText(mActivity, "附近有"+nearByDriverTrackResponse.getSize()+"辆出租车",Toast.LENGTH_SHORT).show();
	}

}
