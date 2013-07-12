package com.benbentaxi.passenger.location;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.benbentaxi.passenger.taxirequest.create.CreateTaxiRequestActivity;
import com.benbentaxi.passenger.taxirequest.index.TaxiRequestIndexActivity;
import com.benbentaxi.util.GetInfoTask;
import com.benbentaxi.util.IdShow;
public class LocationOverlayDemo extends Activity {
	
	private String TAG = LocationOverlayDemo.class.getName();

	
	static MapView mMapView = null;
	
	private MapController mMapController = null;

	public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;
	
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
    public NotifyLister mNotifyer=null;
	
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
                doPassenger();
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
    
    private String mTokenKey, mTokenVal;
	private static final String mTestHost = "42.121.55.211:8081";
	
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
	
	private String mUserMobile;
	private boolean mIsGetLocation = false; // 判断是否成功获取地理位置
	
	
	
	
	public final static int MSG_HANDLE_MAP_MOVE = 1;
	public final static int MSG_HANDLE_POS_REFRESH = 2;
	public final static int MSG_HANDLE_ITEM_TOUCH = 10000;
	
	
	private DemoApplication mApp = null;
	
	private OnClickListener mCallTaxiListener = new OnClickListener(){
		public void onClick(View v) {
			
			LocationData curloc=mApp.getCurrentPassengerLocation();
			if(curloc==null)
			{
				Toast.makeText(LocationOverlayDemo.this, getString(R.string.no_location).toString(), Toast.LENGTH_SHORT).show();
				return;
			}
			
			testUpdateClick();
			testUpdateButton.setText(LocationOverlayDemo.this.getResources().getString(R.string.recall_taxi));			
			//add by wsj			
			Intent createIntent = new Intent(LocationOverlayDemo.this,CreateTaxiRequestActivity.class);			
			
			startActivity(createIntent);			
			
		}
    };
    
    @SuppressWarnings("static-access")
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
        mTokenKey 		= (mApp.getCurrentSession()!=null)? mApp.getCurrentSession().getTokenKey():"";
        mTokenVal 		= (mApp.getCurrentSession()!=null)? mApp.getCurrentSession().getTokenVal():"";
        mUserMobile		= (mApp.getCurrentPassenger()!=null)? mApp.getCurrentPassenger().getMobile():"";
        initMapView();
        
        mLocClient = new LocationClient( this );
        mLocClient.registerLocationListener( myListener );
        
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
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
		mApp.setCurrentPassengerLocation(locData);
	    myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();
		
		testUpdateButton = (Button)findViewById(R.id.btn_callTaxi);
	    testUpdateButton.setOnClickListener(mCallTaxiListener);
	    
	    
	    Log.d(TAG, mTokenKey+": "+mTokenVal);
    }
    
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    
    
    @Override
    protected void onDestroy() {
        if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager != null) {
            app.mBMapManager.destroy();
            app.mBMapManager = null;
        }
        super.onDestroy();
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
        mLocClient.requestLocation();
    }
    private void initMapView() {
        mMapView.setLongClickable(true);
        //mMapController.setMapClickEnable(true);
        //mMapView.setSatellite(false);
    }
    
   
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.i("item:",String.valueOf(item.getItemId()));
	    switch (item.getItemId()) {
		    case R.id.menu_history:
		    	Intent createIntent = new Intent(LocationOverlayDemo.this,TaxiRequestIndexActivity.class);				
				startActivity(createIntent);			
		    return true;		    
	    }
	    return super.onOptionsItemSelected(item);
    }
    
    @SuppressWarnings("static-access")
	private void doPassenger() {
    	// 获取周边Taxi
        TaxiRequest taxiRequest = mApp.getCurrentTaxiRequest();
        NearByDriverTask nearyByDriverTask = new NearByDriverTask(this.mApp);
        nearyByDriverTask.go();
        if (taxiRequest != null) {
        	TaxiRequestRefreshTask refreshTask = new TaxiRequestRefreshTask(this.mApp);
        	refreshTask.go();
        }
        //展示周边Taxi
        ShowCurrentNearByDrivers();
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
    

	
	/**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        //private int mCountFactor = 0; // 计数器，控制执行频率
        
    	@Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            myLocationOverlay.setData(locData);
            mMapView.refresh();
            mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)), 
            		MsgHandler.obtainMessage(MSG_HANDLE_MAP_MOVE));
            
            mIsGetLocation = true;
            MsgHandler.dispatchMessage(MsgHandler.obtainMessage(MSG_HANDLE_POS_REFRESH));
        }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
    
    public class NotifyLister extends BDNotifyListener{
        public void onNotify(BDLocation mlocation, float distance) {
        }
    }
    

    
	private class GetTaxiTask extends GetInfoTask {
		private static final int TYPE_REQ_TAXI = 1;
				
		
		private String _useragent = "ning@benbentaxi";
		private JSONObject _json_data;
		private int _type = -1;
		
				
		public void requireTaxi(double lng, double lat) {
			_type = TYPE_REQ_TAXI;
			String url = "http://"+mTestHost+"/api/v1/taxi_requests";
			
			_json_data = new JSONObject();
			try {
				//{\"taxi_request\":{\"passenger_mobile\":\"15910676326\",\"passenger_lng\":\"8\",\"passenger_lat\":\"8\",\"waiting_time_range\":30}}" 
				JSONObject sess = new JSONObject();
				sess.put("passenger_mobile", mUserMobile);
				sess.put("passenger_lng", lng);
				sess.put("passenger_lat", lat);
				sess.put("waiting_time_range", 10);
				sess.put("passenger_voice", "aSB3aWxsIGJlIHRoZXJl");
				sess.put("passenger_voice_format", "m4a");
				_json_data.put("taxi_request", sess);
			} catch (JSONException e) {
				//_info.append("form json error: "+e.toString());
			}

			doPOST(url);
		}
		
				

		
		
		private void doPOST(String url) {
			// 一定要初始化cookie和content-type!!!!!
			super.initCookies(mTokenKey, mTokenVal, "42.121.55.211");
			super.initHeaders("Content-Type", "application/json");
			
			execute(url, _useragent, GetInfoTask.TYPE_POST);
		}
		

		@Override
		protected void initPostValues() {
			if ( _json_data != null ) {
				post_param = _json_data.toString();
			}
		}
		
		@Override
		protected void onPostExecPost(Boolean succ) {
			String data = this.toString();
			if ( succ ) {
				JSONTokener jsParser = new JSONTokener(data);
				try {
					
					switch ( _type ) {
					case TYPE_REQ_TAXI:
						doCreateRequest(jsParser);
						break;

					default:
						break;
					}
					
				} catch (JSONException e) {
					//e.printStackTrace();
					try {
						JSONObject ret = (JSONObject) jsParser.nextValue();
						JSONObject err = ret.getJSONObject("errors");
						//_info.append("errmsg \""+err.getJSONArray("base").getString(0)+"\"");
						_errmsg = err.getJSONArray("base").getString(0);
						succ = false;
					} catch (Exception ee) {
						//_info.append("json error: "+ee.toString()+"\n");
						//_info.append("to json: "+_json_data.toString());
						_errmsg = "数据通信异常，请检查云服务器配置，或联系服务商";
						succ = false;
					}
				} catch (Exception e) {
					_errmsg = "网络错误，请检查云服务器配置，并确认网络正常后再试";
					succ = false;
				}
				
			} else {
				//_info.append("errmsg: \n"+_errmsg);
			}
			
			if( succ == false ) {
				Toast.makeText(LocationOverlayDemo.this.getApplicationContext(), "错误返回: "+_errmsg+"\n"+data, Toast.LENGTH_SHORT).show();
			}
		}
		
		@SuppressWarnings("static-access")
		private void doCreateRequest(JSONTokener jsParser) throws JSONException {
			JSONObject ret = (JSONObject)jsParser.nextValue();
			TaxiRequest taxiRequest = new TaxiRequest(LocationOverlayDemo.this,ret);
			mApp.setCurrentTaxiRequest(taxiRequest);
		}
		
	}
	
	@SuppressWarnings("static-access")
	private void ShowCurrentNearByDrivers() 
	{
		NearByDriverTrackResponse nearByDriverTrackResponse = this.mApp.getCurrentNearByDriverTrack();
		if (nearByDriverTrackResponse  == null){
			return;
		}
			
		//清除所有添加的Overlay
        ov.removeAll();
        mGeoList.clear();
        
		//添加一个item
    	//当要添加的item较多时，可以使用addItem(List<OverlayItem> items) 接口
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
    		//ov.addItem(mGeoList.get(ov.size() ));
    		ov.addItem(mGeoList);
    	}
	    mMapView.refresh();
	    
	    if (mApp.getCurrentTaxiRequest() == null ) {
	    	Toast.makeText(LocationOverlayDemo.this.getApplicationContext(), "附近有"+nearByDriverTrackResponse.getSize()+"辆出租车",
					Toast.LENGTH_SHORT).show();
	    }
	    
	}
	
	private class DelayTask extends AsyncTask<Integer, Integer, Boolean> {
		public final static int TYPE_CLOSE_POPUP = 0;
		
		private int _type;
		
		public DelayTask( int type ) {
			_type = type;
		}
		
		@Override
		protected Boolean doInBackground(Integer... params) {
			// 获取延迟时间, ms
			int delay = params[0];
			SystemClock.sleep(delay);
			return true;
		}

		
	}
	
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {		
	    return super.onKeyDown(keyCode, event);
	}
}


