package com.benbentaxi.passenger.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.nearbydriver.NearByDriverTask;
import com.benbentaxi.passenger.nearbydriver.NearByDriverTrackResponse;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestRefreshTask;
import com.benbentaxi.passenger.taxirequest.confirm.ConfirmPopupWindow;
import com.benbentaxi.passenger.taxirequest.create.CreateTaxiRequestActivity;
import com.benbentaxi.passenger.taxirequest.detail.TaxiRequestDetail;
import com.benbentaxi.util.IdShow;
public class LocationOverlayDemo extends Activity {
	
	private String TAG = LocationOverlayDemo.class.getName();


	public final static int MSG_HANDLE_MAP_MOVE = 1;
	public final static int MSG_HANDLE_POS_REFRESH = 2;
	public final static int MSG_HANDLE_NEARBY_DRIVER = 3;
	public final static int MSG_HANDLE_REFRESH_CURRENT_TAXIREQUEST = 4;
	public final static int MSG_HANDLE_ITEM_TOUCH = 10000;
	
	
	static MapView mMapView = null;
	
	private MapController mMapController = null;

	public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;
	
	PassengerLocation mPassengerLocation = null;
	
	Button testUpdateButton = null;
	
	EditText indexText = null;
	MyLocationOverlay myLocationOverlay = null;
	int index =0;
	LocationData locData = null;
	
	
	
	Handler MsgHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //Toast.makeText(LocationOverlayDemo.this, "msg:" +msg.what, Toast.LENGTH_SHORT).show();
        	switch (msg.what) {
        	case MSG_HANDLE_MAP_MOVE:
        		break;
        	case MSG_HANDLE_POS_REFRESH:
        		doPassengerLocationRefresh((BDLocation) msg.obj);
        		break;
        	case MSG_HANDLE_REFRESH_CURRENT_TAXIREQUEST:
        		doRefreshCurrentTaxiRequest();
        		break;
        	case MSG_HANDLE_NEARBY_DRIVER:
        		doNearByDriver();
        		break;
        	case ConfirmPopupWindow.MSG_HANDLE_TAXIREQUEST_CONFIRM_TIMEOUT:
        		if (msg.obj != null){
        			((ConfirmPopupWindow)msg.obj).doClean();
        			Toast.makeText(LocationOverlayDemo.this, "请求确认超时，请重新打车!", Toast.LENGTH_LONG).show();
        		}
        		break;
        	default:
        		if ( msg.what >= MSG_HANDLE_ITEM_TOUCH ) {
        			int idx = msg.what-MSG_HANDLE_ITEM_TOUCH;
            		try {
						// 乘客态，显示司机信息
							JSONObject obj = mReqInfo.getJSONObject(idx);
							int drvid = obj.getInt("driver_id");
							showDriverInfo(drvid, obj);
					} catch (JSONException e) {
//						resetStatus();
						// 下标异常
		        		Toast.makeText(LocationOverlayDemo.this.getApplicationContext(), "请求状态异常: "+idx+"/"+mReqInfo.length(),
								Toast.LENGTH_SHORT).show();
					}
        		}
        		break;
        	}
        };
    };
    
    private boolean mIsOnTop = false;
    
	
	OverlayTest ov = null;
	// 存放overlayitem 
	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	// 保存司机/乘客请求的详细信息
	public JSONArray mReqInfo;
	// 被确认的司机/乘客请求信息
	public JSONObject mConfirmObj;
	// 存放overlay图片
	public List<Drawable>  res = new ArrayList<Drawable>();
	private Drawable mDrvMarker;
	
	
	private DemoApplication mApp 				= null;
	
	private Timer mRefreshTaxiRequestTimer		= null;
	private long  mRefreshTaxiRequestPerod		= 5000;
	private Timer mNearyByDriverTimer			= null;
	private long  mNearyByDrvierPeriod			= 10000;
	
	private OnClickListener mCallTaxiListener = new OnClickListener(){
		public void onClick(View v) {
			BDLocation	 curloc					= mApp.getCurrentPassengerLocation();
			TaxiRequest	 taxiRequest			= mApp.getCurrentTaxiRequest();
			if(curloc==null)
			{
				Toast.makeText(LocationOverlayDemo.this, getString(R.string.no_location).toString(), Toast.LENGTH_SHORT).show();
				return;
			}
			if (taxiRequest != null){
				mApp.setCurrentShowTaxiRequest(taxiRequest);
				Intent taxiRequestDetailIntent = new Intent(LocationOverlayDemo.this,TaxiRequestDetail.class);
				LocationOverlayDemo.this.startActivity(taxiRequestDetailIntent);
				Toast.makeText(LocationOverlayDemo.this, "打车请求已经发出", Toast.LENGTH_LONG).show();
				return;
			}
			testUpdateClick();
			Intent createIntent = new Intent(LocationOverlayDemo.this,CreateTaxiRequestActivity.class);			
			startActivity(createIntent);			
		}
    };
    
    
    class RefreshInfo extends TimerTask
    {
    	private int mMessage;
    	public RefreshInfo(int msg)
    	{
    		this.mMessage = msg;
    	}
		@Override
		public void run() {
			if (MsgHandler != null){
				MsgHandler.sendMessage(MsgHandler.obtainMessage(mMessage));
				Log.d(TAG,"Send Message " +mMessage);
			}
		}
    	
    }
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
        }
        setContentView(R.layout.activity_locationoverlay);
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapController = mMapView.getController();
        
        mApp			= app;
        mApp.setHandler(MsgHandler);
        
        initMapView();
        this.mPassengerLocation = new PassengerLocation(this,MsgHandler);
        this.mPassengerLocation.start();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        
        mMapView.setBuiltInZoomControls(true);
        mMapListener = new MKMapViewListener() {
			
			@Override
			public void onMapMoveFinish() {
				// Auto-generated method stub
			}
			
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null){
					title = mapPoiInfo.strText;
					Toast.makeText(LocationOverlayDemo.this,title,Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				//  Auto-generated method stub
				
			}

			@Override
			public void onMapAnimationFinish() {
				//  Auto-generated method stub
				
			}
		};
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager, mMapListener);
		
		mDrvMarker = this.getResources().getDrawable(R.drawable.steering);
		res.add(getResources().getDrawable(R.drawable.steering));
	    ov = new OverlayTest(mDrvMarker, this,mMapView, MsgHandler); 
	    
	    mMapView.getOverlays().add(ov);
	    
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
	    myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();
		
		testUpdateButton = (Button)findViewById(R.id.btn_callTaxi);
	    testUpdateButton.setOnClickListener(mCallTaxiListener);
	    
	    Log.d(TAG,"create............... ");

    }
    
    @Override
    protected void onPause() {
    	mIsOnTop = false;
        mMapView.onPause();
        if (mNearyByDriverTimer != null){
        	Log.d(TAG,"Cancel the NearbyDrvierTimer.");
        	mNearyByDriverTimer.cancel();
        	mNearyByDriverTimer = null;
        }
        super.onPause();
	    Log.d(TAG,"Pause ................. ");
    }
    
    @Override
    protected void onResume() {
    	mIsOnTop = true;
        mMapView.onResume();
        
        
        if (mNearyByDriverTimer == null){
        	mNearyByDriverTimer = new Timer("NearyByDriverTimer",true);
            mNearyByDriverTimer.schedule(new RefreshInfo(MSG_HANDLE_NEARBY_DRIVER), 0 ,mNearyByDrvierPeriod);

        }
        super.onResume();
	    Log.d(TAG,"Resume ................. ");
    }
    
    
    @Override
    protected void onDestroy() {
        if (mPassengerLocation != null)
        	mPassengerLocation.stop();
        mMapView.destroy();
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager != null) {
            app.mBMapManager.destroy();
            app.mBMapManager = null;
        }
        super.onDestroy();
	    Log.d(TAG,"Destory..............");
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
    public void testUpdateClick(){
       int s = this.mPassengerLocation.requestLocation();
       Log.d(TAG,"request my location ,res="+s);
    }
    private void initMapView() {
        mMapView.setLongClickable(true);
    }
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
    private void doNearByDriver()
    {
       NearByDriverTask nearyByDriverTask = new NearByDriverTask(this.mApp);
       nearyByDriverTask.go();
       ShowCurrentNearByDrivers();
    }
	private void doRefreshCurrentTaxiRequest() {

        TaxiRequest taxiRequest = mApp.getCurrentTaxiRequest();
        if (taxiRequest != null) {
    		if (mRefreshTaxiRequestTimer == null){
            	mRefreshTaxiRequestTimer = new Timer("RefreshTaxiRequestTimer",true);
                mRefreshTaxiRequestTimer.schedule(new RefreshInfo(MSG_HANDLE_REFRESH_CURRENT_TAXIREQUEST),mRefreshTaxiRequestPerod , mRefreshTaxiRequestPerod);
            }
        	if (mIsOnTop){
        		Toast.makeText(this, "请求"+taxiRequest.getId()+","+taxiRequest.getHumanStateText(), Toast.LENGTH_LONG).show();
        	}
        	TaxiRequestRefreshTask refreshTask = new TaxiRequestRefreshTask(this,MsgHandler);
        	refreshTask.go();
        }else{
        	if (mRefreshTaxiRequestTimer != null){
        		mRefreshTaxiRequestTimer.cancel();
        		mRefreshTaxiRequestTimer = null;
        	}
        }
    }
	
	private void doPassengerLocationRefresh(BDLocation location)
	{
		if (location == null)
			return;
		locData.latitude = location.getLatitude();
		locData.longitude = location.getLongitude();
		locData.accuracy = location.getRadius();
		locData.direction = location.getDerect();
		Log.d(TAG,location.getAddrStr()+"|"+location.getCity());
		myLocationOverlay.setData(locData);
		mMapView.refresh();

		mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)), 
         		MsgHandler.obtainMessage(MSG_HANDLE_MAP_MOVE));
	}
    
    
    
    private void showDriverInfo(int idx, JSONObject obj) throws JSONException {
		int drvid = obj.getInt("driver_id");
		double drv_lat = obj.getDouble("lat");
		double drv_lng = obj.getDouble("lng");
		
		IdShow confirm = new IdShow("司机信息", "ID: "+drvid+"\n经度: "+drv_lng+"\n纬度: "+drv_lat, this);

    	confirm.SetNegativeOnclick(null, null);
    	confirm.SetPositiveOnclick("关闭", null);
    	confirm.getIdDialog().show();
    }
    
	
	private void ShowCurrentNearByDrivers() 
	{
		NearByDriverTrackResponse nearByDriverTrackResponse = this.mApp.getCurrentNearByDriverTrack();
		if (nearByDriverTrackResponse  == null){
			return;
		}
			
		//清除所有添加的Overlay
        ov.removeAll();
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
    	if ( ov.size() < mGeoList.size()){
    		ov.addItem(mGeoList);
    	}
	    mMapView.refresh();
	    Toast.makeText(LocationOverlayDemo.this.getApplicationContext(), "附近有"+nearByDriverTrackResponse.getSize()+"辆出租车",
					Toast.LENGTH_SHORT).show();
	    
	}
	
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {		
	    return super.onKeyDown(keyCode, event);
	}
}


