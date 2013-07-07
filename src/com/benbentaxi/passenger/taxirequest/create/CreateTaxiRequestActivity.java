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
	private String strXY="";
	
	// for audiorecorder
	private AudioRecord mAudioRecord; //  乘客声音
	private AudioTrack mAudioTrack; // 播放乘客声音
	private  int mAudioBufSize = 0;
	private  byte[] mAudioBuffer;
	//private int mDestDot=0; //copy byte[] 的目的地开始位置
	private long mRecTime; // 判断录音时间是否过短
	private boolean isRecord = false;// 设置正在录制的状态 
	private ArrayList<byte[]> arry=new ArrayList<byte[]>();
	
	//for mediarecorder
	private String mAbsolutePath="";  //getApplicationContext().getFilesDir().getAbsolutePath();
	private String mTmpfile="_voice_record_tmp.3gp";
	//private MediaRecorder mediaRecorder ;	
	private VoiceRecorder mediaRecorder;
	private MediaPlayer   mPlayer = null; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_taxi_request_create);			
		strXY =  getIntent().getStringExtra("location");  	
		mAbsolutePath=Environment.getExternalStorageDirectory()+"/benbentaxi_passenger/tmp/voice/";
		//mAbsolutePath="/sdcard/1111";//Environment.getExternalStorageDirectory()+"/MyTestRecord";
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
		stopRecord();
		if (mAudioRecord != null)
			mAudioRecord.release();//释放资源 		
        mAudioRecord = null; 
        
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
	
	public String getAudioString()
	{
		byte[] st=new byte[mAudioBufSize*arry.size()];
		for(int i=0;i<arry.size();i++)
		{			
			System.arraycopy(arry.get(i), 0, st,i*mAudioBufSize, mAudioBufSize);
		}
		return android.util.Base64.encodeToString(st, Base64.DEFAULT);
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
	
	public String getXY()
	{
		return strXY;
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

	private void initAudio1() {
    	
    	mAudioBufSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
	
    	Log.i("mAudioBufSize : ",String.valueOf(mAudioBufSize));

	mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, 
			AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, mAudioBufSize);
	
	mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, 
			AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, mAudioBufSize, AudioTrack.MODE_STREAM);    	
	Log.i("initAudio : ","end");
	
}

private void doRecordAudio() {
	mAudioRecord.startRecording();
	isRecord = true; 
	
	Log.i("doRecordAudio : ","start");
	new Thread(new AudioRecordThread()).start(); 
}

private void stopRecord() { 

	if (mAudioRecord != null) { 
		//System.out.println("stopRecord"); 
        
        if(isRecord)
        {
        	isRecord = false;//停止 
        	mAudioRecord.stop();         	
        }       
        Log.i("stopRecord : ","stop");
    } 

} 

/*@Override
public void onCompletion(MediaPlayer mp) {
    // TODO Auto-generated method stub
    mp.release();
    Log.i("stop mediaplay : ","资源已释放");
    
}*/


private void doPlayAudio() {
	mAudioTrack.play();
	
	byte[] st=new byte[mAudioBufSize*arry.size()];
	for(int i=0;i<arry.size();i++)
	{
		//mAudioTrack.write(mAudioBuffer, 0, mAudioBufSize);
		//mAudioTrack.write(arry.get(i), 0, mAudioBufSize);	
		System.arraycopy(arry.get(i), 0, st,i*mAudioBufSize, mAudioBufSize);
	}
	mAudioTrack.write(st, 0, st.length);	
	Log.i("Play:",String.valueOf(st.length));
	mAudioTrack.stop();
}
	
	private void buttonBind()
	{
		Button button = (Button)findViewById(R.id.create_request);
		button.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						CeateTaxiRequestForm ceateTaxiRequestForm = new CeateTaxiRequestForm(CreateTaxiRequestActivity.this);
						ceateTaxiRequestForm.setXY(strXY);
						//ceateTaxiRequestForm.setAudio(getAudioString());
						ceateTaxiRequestForm.setAudio(getAudioFile2String());
						CeateTaxiRequestTask ceateTaxiRequestTask = new CeateTaxiRequestTask(ceateTaxiRequestForm);
						ceateTaxiRequestTask.go();
						//finish();
					}
		});
		button 		  = (Button)findViewById(R.id.go_back);
		button.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						//Intent loginIntent = new Intent(CreateTaxiRequestActivity.this,LocationOverlayDemo.class);
						//startActivity(loginIntent);
						//finish();
						
						//doPlayAudio() ;
						
						try {
							mediaRecorder.StopRecorder();
							Log.i("mediaRecorder : ","stop1");
						}
				        catch (IllegalStateException e) {
				            e.printStackTrace();
				        }
						
						Log.i("play", mTmpfile+" "); 
						
						mPlayer = new MediaPlayer(); 
						
						 try { 
							 //设置要播放的文件  
							            mPlayer.setDataSource(mTmpfile); 
							            mPlayer.prepare(); 
							            //播放之  
							            mPlayer.start(); 
							            
							        } catch (IOException e) { 
							            Log.i("prepare", mTmpfile+" prepare() failed"); 
							        } 
						 //mPlayer.stop();	
						// mPlayer.release(); 
					       // mPlayer = null; 
						 Log.i("mPlayer", mTmpfile+" end() "); 
					        /*File file = new File(mTmpfile); 
					         if (file.exists()) 
					        	 file.delete();  */
					         
					}
		});
		final ImageView  imgBtn = (ImageView)findViewById(R.id.imgBtnRec);
		imgBtn.setClickable(true);
		imgBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {					
				//Application app = CreateTaxiRequestActivity.this.getApplication();
				//TextView tv = (TextView)findViewById(R.id.want_to);				
				
				if (v.getId() == R.id.imgBtnRec) {
										
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						// TODO 录音或播放		
						//imgBtn.setBackgroundColor(Color.RED);
						
						//imgBtn.setBackgroundColor(android.graphics.Color.parseColor("#ab1268"));
						imgBtn.setBackgroundColor(Color.RED);
						//imgBtn.setBackgroundColor(Color.rgb(0, 0, 255)); 
						
						mRecTime = System.currentTimeMillis();
						//doRecordAudio();
						
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
						//stopRecord();	
						
						//imgBtn.setBackgroundColor(Color.BLACK);
						//imgBtn.setBackgroundColor(Color.rgb(255, 0, 0)); 
						
						Log.i("cl","111");
						imgBtn.setBackgroundColor(Color.TRANSPARENT);
						Log.i("cl","222");
						
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
							//tv.setText(app.getResources().getString(R.string.rec_short));
							
						} 
						
					}
				}
				return false;
			}
					
		});
	}
	
	class AudioRecordThread implements Runnable { 
	    @Override 
	    public void run() { 
	    	int readsize = 0;      
	    	Log.i("in thread","begin");
	    	mAudioBuffer  = new byte[mAudioBufSize];	   
	    	byte[] tmp;
	    	while (isRecord == true) { 
	            readsize = mAudioRecord.read(mAudioBuffer, 0, mAudioBufSize);
	            tmp=mAudioBuffer.clone();
	            arry.add(tmp);
	            //mAudioBuffer
	            //System.arraycopy(audiodata, 0, mAudioBuffer,mDestDot, readsize);
	            //mDestDot=readsize;
	            
	            /*for (int i = 0; i < readsize; i++) {
	            	  
	            		   mAudioBuffer[mDestDot]=audiodata[i];
	            		   mDestDot++;
	            	  }*/
	            
	            Log.i("readsize",String.valueOf(readsize));
	        } 
	    } 
	} 
	
	class AudioRecordThread1 implements Runnable { 
	    @Override 
	    public void run() { 
	    	int readsize = 0;      
	    	Log.i("in thread","begin");
	    	mAudioBuffer  = new byte[mAudioBufSize];	   
	    	byte[] tmp;
	    	while (isRecord == true) { 
	            readsize = mAudioRecord.read(mAudioBuffer, 0, mAudioBufSize);
	            tmp=mAudioBuffer.clone();
	            arry.add(tmp);
	            //mAudioBuffer
	            //System.arraycopy(audiodata, 0, mAudioBuffer,mDestDot, readsize);
	            //mDestDot=readsize;
	            
	            /*for (int i = 0; i < readsize; i++) {
	            	  
	            		   mAudioBuffer[mDestDot]=audiodata[i];
	            		   mDestDot++;
	            	  }*/
	            
	            Log.i("readsize",String.valueOf(readsize));
	        } 
	    } 
	} 

}


