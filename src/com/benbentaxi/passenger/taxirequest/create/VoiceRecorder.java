package com.benbentaxi.passenger.taxirequest.create;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.util.Log;


public class VoiceRecorder {
	private static final String TAG = VoiceRecorder.class.getName();
	private String strPath;
	private boolean bStart=false;
	
	private MediaRecorder mediaRecorder =null;	 
	
	public VoiceRecorder(int nAudioSource,int nOutputFormat,int nAudioEncoder,String OutputFile)
	{
		if(mediaRecorder==null)
			mediaRecorder = new MediaRecorder();
		//设置音源为Micphone  
		mediaRecorder.setAudioSource(nAudioSource); 
	    //设置封装格式  
		mediaRecorder.setOutputFormat(nOutputFormat); 		
	    //设置编码格式  
		mediaRecorder.setAudioEncoder(nAudioEncoder); 		
		mediaRecorder.setOutputFile(OutputFile);
		strPath=OutputFile;
		bStart=false;
	}
	
	public void SetAudioSource(int nAudioSource){
		if(mediaRecorder==null)
			mediaRecorder = new MediaRecorder();
		
		mediaRecorder.setAudioSource(nAudioSource);
	}
	
	public void SetOutputFormat(int nOutputFormat){
		if(mediaRecorder==null)
			mediaRecorder = new MediaRecorder();
		
		mediaRecorder.setOutputFormat(nOutputFormat);
	}
	
	public void SetAudioEncoder(int nAudioEncoder){
		if(mediaRecorder==null)
			mediaRecorder = new MediaRecorder();
		
		mediaRecorder.setAudioEncoder(nAudioEncoder);
	}
	
	public void SetOutputFile(String OutputFile){
		if(mediaRecorder==null)
			mediaRecorder = new MediaRecorder();		
		mediaRecorder.setOutputFile(OutputFile);
	}
		
	public void onDestroy() {
		if(bStart)
			mediaRecorder.stop();
		if(mediaRecorder!=null)
			mediaRecorder.release();        
        mediaRecorder=null;
	}
	
	public void StartRecorder() throws IllegalStateException, IOException{		
			
		if(bStart)
			mediaRecorder.stop();
		
		File file = new File(strPath); 	         
			if (file.exists()) 
				file.delete();			
            mediaRecorder.prepare();
            mediaRecorder.start();      
            bStart=true;
	}
	
	public void StopRecorder() throws NullPointerException{		
		if(mediaRecorder!=null)
		{
			if(bStart){
				try {
					mediaRecorder.stop();
				}catch(RuntimeException stopException){
					Log.e(TAG,"no voice is record.......");
				}
			}
			bStart=false;
		}
		else
			throw new NullPointerException("Not Init mediaRecorder");
	}
	
	public void Reset(){
		if(mediaRecorder!=null)
		{
			mediaRecorder.reset();	
		}
	}
	
}
