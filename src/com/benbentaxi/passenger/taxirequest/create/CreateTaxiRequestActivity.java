package com.benbentaxi.passenger.taxirequest.create;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.baidu.location.BDLocation;
import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.DemoApplication;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.Color;



public class CreateTaxiRequestActivity  extends Activity  {
	private final static String TAG = CreateTaxiRequestActivity.class.getName();
	private String mTmpfile ; 
	private long mRecTime;
	private String mAbsolutePath=""; 
	private VoiceRecorder mediaRecorder;
	private boolean		  mRecorderSuccess = false;
	private MediaPlayer   mPlayer = null; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_taxi_request_create);	
		mAbsolutePath		= Environment.getExternalStorageDirectory()+"/benbentaxi_passenger/tmp/voice/";
		mTmpfile			= mAbsolutePath+"/_tmp_passenger_voice";
		TextView t = (TextView) this.findViewById(R.id.taxi_request_source);
		DemoApplication app 			= (DemoApplication) this.getApplication();
		BDLocation 		bdLocation		= app.getCurrentPassengerLocation();
		t.setText("位置:"+  ((bdLocation == null) ? "未知" : bdLocation.getAddrStr()));
		File dir = new File(mAbsolutePath);
		if (!dir.exists()) {
			 if(dir.mkdirs())
			     Log.i("mkdir : ",mAbsolutePath+" ok");
			else
			     Log.i("mkdir : ","NG");
		}
		
		buttonBind();		
	}	
	
	@Override
	protected void onDestroy() {
		
		releaseAudio();
		if(mPlayer != null){
        	mPlayer.stop();
        	mPlayer.release();
        	mPlayer=null;
        }
		
		super.onDestroy();
		
	}
	
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK && 
				event.getAction() == KeyEvent.ACTION_DOWN) {			
				finish();			
	        return true;   
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}
	
	
	
	public String getAudioFile2String()
	{
		
		try{ 
			
			FileInputStream fin = new FileInputStream(mTmpfile);
			int length = fin.available(); 
	
		    byte [] buffer = new byte[length]; 
	
		    fin.read(buffer);     	  
	
		    fin.close();  
		    return android.util.Base64.encodeToString(buffer, Base64.DEFAULT);
	    
		}
		catch(FileNotFoundException e){
			Log.e(TAG,"文件未找到"+mTmpfile);
		}
		catch(Exception e){ 
			e.printStackTrace(); 
		}
		
		return "";
		
	}
	
	private void initAudio() {
		File file = new File(mTmpfile); 
		if (file.exists()) 
			file.delete();  
		mediaRecorder = new VoiceRecorder(MediaRecorder.AudioSource.MIC,MediaRecorder.OutputFormat.THREE_GPP,
		MediaRecorder.AudioEncoder.AMR_NB,mTmpfile);

	}
	
	private void releaseAudio()
	{
		if (mediaRecorder != null){
			mediaRecorder.onDestroy();
			mediaRecorder = null;
		}
	}

	

	
	private void buttonBind()
	{
		Button button = (Button)findViewById(R.id.create_request);
		button.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						
						if (!CreateTaxiRequestActivity.this.mRecorderSuccess){
							Toast.makeText(CreateTaxiRequestActivity.this, "请录音后发送!", Toast.LENGTH_LONG).show();
						}else{
							CeateTaxiRequestForm ceateTaxiRequestForm = new CeateTaxiRequestForm(CreateTaxiRequestActivity.this);						
						
							CeateTaxiRequestTask ceateTaxiRequestTask = new CeateTaxiRequestTask(ceateTaxiRequestForm);
							ceateTaxiRequestTask.go();
						}
						
					}
		});
		button 		  = (Button)findViewById(R.id.go_back);
		button.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						
						finish();						
						
					         
					}
		});
		final ImageView  imgBtn = (ImageView)findViewById(R.id.imgBtnRec);
		imgBtn.setClickable(true);
		imgBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {					
				
				if (v.getId() == R.id.imgBtnRec) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						mRecorderSuccess = false;
						releaseAudio();
						initAudio();
						imgBtn.setBackgroundColor(Color.RED);
						mRecTime = System.currentTimeMillis();
						try {							
				            mediaRecorder.StartRecorder();	
				        }
				        catch (IllegalStateException e) {
				            e.printStackTrace();
				        }
				        catch (IOException e) {
				            e.printStackTrace();
				        }
					}
					if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
						
						imgBtn.setBackgroundColor(Color.TRANSPARENT);
						
						
						try {
							if (mediaRecorder != null)
								mediaRecorder.StopRecorder();
							else
								Log.e(TAG,"mediaRecorder is already null");
						}
				        catch (IllegalStateException e) {
				            e.printStackTrace();
				        }
				        
					        
						if ( (System.currentTimeMillis()-mRecTime) < 1000 ) {
							// 时间太短
							File file = new File(mTmpfile); 
					         if (file.exists()) 
					        	 file.delete();
							Toast.makeText(CreateTaxiRequestActivity.this, "录音时间太短，请重新录制", Toast.LENGTH_SHORT).show();
						}else{
							mRecorderSuccess = true;
						}
						
					}
				}
				return false;
			}
					
		});
	}
	

}


