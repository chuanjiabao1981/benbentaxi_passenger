package com.benbentaxi.passenger.taxirequest.create;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.baidu.mapapi.map.LocationData;
import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.LocationOverlayDemo;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;



public class CreateTaxiRequestActivity  extends Activity  {
	private String TAG = CreateTaxiRequestActivity.class.getName();
	private LocationData locData = null;
			
	
	//for mediarecorder
	private long mRecTime;
	private String mAbsolutePath=""; 
	private String mTmpfile="_voice_record_tmp.3gp";
	//private MediaRecorder mediaRecorder ;	
	private VoiceRecorder mediaRecorder;
	private MediaPlayer   mPlayer = null; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_taxi_request_create);	
		mAbsolutePath=Environment.getExternalStorageDirectory()+"/benbentaxi_passenger/tmp/voice/";
		
		File dir = new File(mAbsolutePath);
		if (!dir.exists()) {
			 if(dir.mkdirs())
			     Log.i("mkdir : ",mAbsolutePath+" ok");
			else
			     Log.i("mkdir : ","NG");
		}
		Log.i("path : ",mAbsolutePath);			
		initAudio();		
		buttonBind();		
	}	
	
	@Override
	protected void onDestroy() {
		
        mediaRecorder.onDestroy();
        
		super.onDestroy();
		
		if(mPlayer != null){
        	mPlayer.stop();
        	mPlayer.release();
        	mPlayer=null;
        }
		
		Log.i("CraeteActivity : ","Destroyed");
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

	    //res = EncodingUtils.getString(buffer, "UTF-8"); 

	    fin.close();  
	    return android.util.Base64.encodeToString(buffer, Base64.DEFAULT);
	    
		}catch(Exception e){ 

	           e.printStackTrace(); 
		} 

		return "-1";
		
	}
	
private void initAudio() {
    	
	mTmpfile=mAbsolutePath+"/"+mTmpfile;
    File file = new File(mTmpfile); 
  
    if (file.exists()) 
        file.delete();  
    
   mediaRecorder = new VoiceRecorder(MediaRecorder.AudioSource.MIC,MediaRecorder.OutputFormat.THREE_GPP,
    	MediaRecorder.AudioEncoder.AMR_NB,mTmpfile);	
    
	Log.i("initAudio : ","end");
	
}

	

	
	private void buttonBind()
	{
		Button button = (Button)findViewById(R.id.create_request);
		button.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						CeateTaxiRequestForm ceateTaxiRequestForm = new CeateTaxiRequestForm(CreateTaxiRequestActivity.this);						
						
						CeateTaxiRequestTask ceateTaxiRequestTask = new CeateTaxiRequestTask(ceateTaxiRequestForm);
						ceateTaxiRequestTask.go();
						
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
						// TODO 录音或播放		
					
						imgBtn.setBackgroundColor(Color.RED);
					
						
						mRecTime = System.currentTimeMillis();
					
						
						try {							
				            mediaRecorder.StartRecorder();	
				            Log.i("StartRecorder","OK");
				        }
				        catch (IllegalStateException e) {
				            e.printStackTrace();
				        }
				        catch (IOException e) {
				            e.printStackTrace();
				        }
					}
					
					if (event.getAction() == MotionEvent.ACTION_UP) {
						
						imgBtn.setBackgroundColor(Color.TRANSPARENT);
						
						
						try {
							mediaRecorder.StopRecorder();
							Log.i("mediaRecorder : ","stop");
						}
				        catch (IllegalStateException e) {
				            e.printStackTrace();
				        }
				        
						//mediaRecorder.release();     
					        
						if ( (System.currentTimeMillis()-mRecTime) < 1000 ) {
							// 时间太短
							File file = new File(mTmpfile); 
					         if (file.exists()) 
					        	 file.delete();
							Toast.makeText(CreateTaxiRequestActivity.this, "录音时间太短，请重新录制", Toast.LENGTH_SHORT).show();
							
							
						} 
						
					}
				}
				return false;
			}
					
		});
	}
	

}


