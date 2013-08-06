package com.benbentaxi.passenger.location;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.actionbar.ActionBarActivity;
import com.benbentaxi.passenger.background.BackgroundService;
import com.benbentaxi.passenger.nearbydriver.NearbyDrvierReceiver;
import com.benbentaxi.passenger.nearbydriver.NearybyDrvierServiceConnection;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.TaxiRequestRefreshTask;
import com.benbentaxi.passenger.taxirequest.confirm.ConfirmPopupWindow;
import com.benbentaxi.passenger.taxirequest.create.CreateTaxiRequestActivity;
import com.benbentaxi.passenger.taxirequest.detail.TaxiRequestDetail;
import com.benbentaxi.passenger.taxirequest.index.TaxiRequestIndexTask;
import com.benbentaxi.passenger.taxirequest.popup.TaxiRequestPopupWindow;
import com.benbentaxi.util.IdShow;
public class LocationOverlayDemo extends ActionBarActivity {
	
	private String TAG = LocationOverlayDemo.class.getName();


	public final static int MSG_HANDLE_ITEM_TOUCH 							= 10000;
	public final static int MSG_HANDLE_MAP_MOVE 							= 1;
	public final static int MSG_HANDLE_POS_REFRESH 							= 2;
	public final static int MSG_HANDLE_REFRESH_CURRENT_TAXIREQUEST 			= 4;
	public final static int MSG_HANDLE_TAXIREQUEST_DRIVER_RESPONSE 			= 5;
	public final static int MSG_HANDLE_TAXIREQUEST_POPUP					= 6;
	public final static int MSG_HANDLE_TAXIREQUEST_HIDE						= 7;
	public final static int MSG_HANDLE_TAXI_EQUEST_PASSENGER_CONFIRM		= 8;
	
	
	private NearbyDrvierReceiver mNearbyDrvierReceiver						= null;
	
	static MapView mMapView 												= null;
	private MapController mMapController 									= null;

	FrameLayout mMapViewContainer = null;
	PassengerLocation mPassengerLocation = null;
	Button testUpdateButton = null;
	MyLocationOverlay myLocationOverlay = null;
	int index =0;
	LocationData locData = null;
	
	
	
	Handler MsgHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //Toast.makeText(LocationOverlayDemo.this, "msg:" +msg.what, Toast.LENGTH_SHORT).show();
        	switch (msg.what) {
        	case MSG_HANDLE_MAP_MOVE:
        		break;
        	case MSG_HANDLE_TAXIREQUEST_POPUP:
        		if  (mTaxiRequestPopupWindow != null)
        			mTaxiRequestPopupWindow.showPopup();
        		break;
        	case MSG_HANDLE_TAXIREQUEST_HIDE:
        		if (mTaxiRequestPopupWindow != null)
        			mTaxiRequestPopupWindow.release(false);
        		break;
        	case MSG_HANDLE_POS_REFRESH:
        		doPassengerLocationRefresh((BDLocation) msg.obj);
        		break;
        	case MSG_HANDLE_REFRESH_CURRENT_TAXIREQUEST:
        		doRefreshCurrentTaxiRequest();
        		break;
        	case MSG_HANDLE_TAXIREQUEST_DRIVER_RESPONSE:
        		doDriverResponse((TaxiRequest) msg.obj);
        		break;
        	case MSG_HANDLE_TAXI_EQUEST_PASSENGER_CONFIRM:
        		doShowTaxiRequestDetialInfo((TaxiRequest) msg.obj);
        		break;
        	case ConfirmPopupWindow.MSG_HANDLE_TAXIREQUEST_CONFIRM_TIMEOUT:
        		if (msg.obj != null){
        			((ConfirmPopupWindow)msg.obj).doClean();
        			Toast.makeText(LocationOverlayDemo.this, "请求确认超时，请重新打车!", Toast.LENGTH_LONG).show();
        			LocationOverlayDemo.this.mApp.setCurrentTaxiRequest(null);
        		}
        		break;
        	case MSG_HANDLE_ITEM_TOUCH:
				showDriverInfo((OverlayItem) msg.obj);
				break;
        	default:
        		Log.d(TAG,"Mesg="+msg.what);
        		break;
        	}
        };
    };
    private boolean mIsOnTop 													= false;
	DriverOverlay ov 															= null;
	private Drawable mDrvMarker													= null;
	private ConfirmPopupWindow mPassengerConfirmPopupWindow						= null;
    private DemoApplication mApp 						                    	= null;
    private Timer mRefreshTaxiRequestTimer				                    	= null;
    private long  mRefreshTaxiRequestPerod				                    	= 5000;
    private NearybyDrvierServiceConnection mNearByDriverServiceConnection		= null;
    private TaxiRequestPopupWindow		   mTaxiRequestPopupWindow				= null;
	
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
        setContentView(R.layout.activity_locationoverlay);
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
        }
        mMapView 							= (MapView)findViewById(R.id.bmapView);
        mMapController 						= mMapView.getController();
        mApp								= app;
        this.mPassengerConfirmPopupWindow	= new ConfirmPopupWindow(this,MsgHandler,30);
        mApp.setHandler(MsgHandler);
        initMapView();
        this.mPassengerLocation 			= new PassengerLocation(this,MsgHandler);
        this.mPassengerLocation.start();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        
        mMapView.setBuiltInZoomControls(true);
        
		
		mDrvMarker = this.getResources().getDrawable(R.drawable.steering);
	    ov 						= new DriverOverlay(mDrvMarker, this,mMapView, MsgHandler); 

	    mMapView.getOverlays().add(ov);
	    
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
	    myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();
		
		

		testUpdateButton = (Button)findViewById(R.id.btn_callTaxi);
	    testUpdateButton.setOnClickListener(mCallTaxiListener);
	    mTaxiRequestPopupWindow	= new TaxiRequestPopupWindow((DemoApplication) this.getApplication(),mMapView,MsgHandler);

	    
	    
	    
	    
	    Log.d(TAG,"create............... ");

    }
    public BackgroundService getBackgroundService()
    {
    	if (this.mNearByDriverServiceConnection != null){
    		return this.mNearByDriverServiceConnection.getService();
    	}else{
    		return null;
    	}
    }
    @Override
    protected void onPause() {

    	mIsOnTop = false;
    	if (mTaxiRequestPopupWindow != null){
    		mTaxiRequestPopupWindow.release(true);
    	}
        mMapView.onPause();
        unregisterReceiver();
        unboundService();
        super.onPause();
	    Log.d(TAG,"Pause ................. ");
    }
    
    @Override
    protected void onResume() {
    	mIsOnTop = true;
        mMapView.onResume();
        boundService();
        registerReceiver();
        /*
        if (mTaxiRequestPopupWindow != null)
        	mTaxiRequestPopupWindow.showPopup();*/
        super.onResume();
	    Log.d(TAG,"Resume ................. ");
    }
    
    
    @Override
    protected void onDestroy() {
        if (mPassengerLocation != null)
        	mPassengerLocation.stop();
        if (mPassengerConfirmPopupWindow != null && mPassengerConfirmPopupWindow.isShowing()){
        	mPassengerConfirmPopupWindow.dismiss();
        }
    	if (mTaxiRequestPopupWindow != null){
    		mTaxiRequestPopupWindow.release(false);
    	}
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
    private void doDriverResponse(TaxiRequest taxiRequest){
    	if (this.mPassengerConfirmPopupWindow.isShowing() == false){
    		this.mPassengerConfirmPopupWindow.show();
    	}
    	if (taxiRequest != null){
    		String ns = Context.NOTIFICATION_SERVICE;
    		NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(ns);
    		int icon = android.R.drawable.ic_dialog_info;        
    		CharSequence tickerText = "有司机响应!"; 
    		CharSequence contentTitle = "有司机响应";  
    		CharSequence contentText = "司机:"+taxiRequest.getDriverMobile()+"响应请求，请确认!";
    		
    		Intent notificationIntent = new Intent(this.getApplicationContext(), LocationOverlayDemo.class);
    		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    		PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    		Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
    		notification.setLatestEventInfo(this.getApplicationContext(), contentTitle, contentText, contentIntent);
    		notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS;
    		mNotificationManager.notify((int)taxiRequest.getId(), notification);
    	}
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
    private void doShowTaxiRequestDetialInfo(TaxiRequest taxiRequest)
    {
		mApp.setCurrentShowTaxiRequest(taxiRequest);
		mApp.setCurrentTaxiRequest(null);
		Intent taxiRequestDetailIntent = new Intent(this,TaxiRequestDetail.class);
		startActivity(taxiRequestDetailIntent);
    }
    private void boundService()
    {
        if (mNearByDriverServiceConnection == null){
        	mNearByDriverServiceConnection = new NearybyDrvierServiceConnection();
        }
        Intent intent = new Intent(this, BackgroundService.class);
        boolean s = bindService(intent, mNearByDriverServiceConnection, Context.BIND_AUTO_CREATE);
		Log.i(TAG,"Bind Service "+s);

    }
    private void unboundService()
    {
    	if (this.mNearByDriverServiceConnection != null && this.mNearByDriverServiceConnection.isBound()){
    		Log.d(TAG,"begin to unbound................");
            unbindService(mNearByDriverServiceConnection);
            mNearByDriverServiceConnection.close();
    	}
    }
    private void registerReceiver()
    {
    	if (mNearbyDrvierReceiver == null){
    		mNearbyDrvierReceiver = new NearbyDrvierReceiver(this,this.mMapView,this.ov);
    	}
    	LocalBroadcastManager.getInstance(this).registerReceiver(mNearbyDrvierReceiver,new IntentFilter(BackgroundService.NEARYBY_DRIVER_ACTION));
    }
    
    private void unregisterReceiver()
    {
    	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mNearbyDrvierReceiver);
    }
    
    private void showDriverInfo(OverlayItem item)
    {
    	if (item == null){
    		Log.e(TAG,"tap item info is null");
    		return;
    	}
    	IdShow confirm = new IdShow("司机信息",item.getTitle(),this);
    	confirm.SetNegativeOnclick(null, null);
    	confirm.SetPositiveOnclick("关闭", null);
    	confirm.getIdDialog().show();
    }
    

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
		        moveTaskToBack(true);
		        return true;
		    }
	    return super.onKeyDown(keyCode, event);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.i("item:",String.valueOf(item.getItemId()));
	    switch (item.getItemId()) {
		    case R.id.menu_history:
		    	TaxiRequestIndexTask tsk=new TaxiRequestIndexTask(this,mApp);
				tsk.go();
		    return true;		    
	    }
	    return super.onOptionsItemSelected(item);
    }

}


