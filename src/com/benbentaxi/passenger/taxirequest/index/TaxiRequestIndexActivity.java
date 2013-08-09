package com.benbentaxi.passenger.taxirequest.index;

import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.detail.TaxiRequestDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup;
import android.widget.BaseAdapter;  
import android.widget.ListView;
import android.widget.TextView; 
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;

public class TaxiRequestIndexActivity extends Activity  {
	
	private final String TAG			     = TaxiRequestDetail.class.getName();
	
	private MyAdapter adapter;  
    private ListView listview;  
    private DemoApplication mApp;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 获得手机分辨率  		
		DisplayMetrics dm = new DisplayMetrics();   
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		int adjustFontSize=adjustFontSize(dm.widthPixels,dm.heightPixels);
		
		setContentView(R.layout.activity_taxirequstindex);
		
		mApp=(DemoApplication)this.getApplication();
		
		
//		TextView vw=(TextView) findViewById(R.id.textView2);    
//		vw.setTextSize(adjustFontSize+5);
//		vw=(TextView) findViewById(R.id.textView3);    
//		vw.setTextSize(adjustFontSize+5);
		
		listview = (ListView)findViewById(R.id.lv);  
		
		listview.setOnItemClickListener(new OnItemClickListener(){			
			@Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
					TaxiRequestIndexResponse taxiRequestIndexResponse=mApp.getCurrentTaxiRequestIndex();
		    		TaxiRequest tx=(TaxiRequest) taxiRequestIndexResponse.getTaxiRequest(arg2);
                	mApp.setCurrentShowTaxiRequest(tx);
                	Intent taxiRequestDetailIntent = new Intent(TaxiRequestIndexActivity.this,TaxiRequestDetail.class);
                    startActivity(taxiRequestDetailIntent);                
            }     
        });		
		
		
		showList(adjustFontSize);
    }
    
    protected void showList(int nAdjustFontSize){    	
    	boolean hasData=true;
    	TaxiRequestIndexResponse taxiRequestIndexResponse=mApp.getCurrentTaxiRequestIndex();
    	listview = (ListView)findViewById(R.id.lv);  
		
    	if(taxiRequestIndexResponse==null)
    		return;
    	
    	 
		adapter = new MyAdapter(this,hasData,nAdjustFontSize); 
		
		listview.setAdapter(adapter);  
		
    }
    
    //获取字体大小  
    private  int adjustFontSize(int screenWidth, int screenHeight) {  
            screenWidth=screenWidth>screenHeight?screenWidth:screenHeight;  
            /** 
             * 1. 在视图的 onsizechanged里获取视图宽度，一般情况下默认宽度是320，所以计算一个缩放比率 
                rate = (float) w/320   w是实际宽度 
               2.然后在设置字体尺寸时 paint.setTextSize((int)(8*rate));   8是在分辨率宽为320 下需要设置的字体大小 
                实际字体大小 = 默认字体大小 x  rate 
             */  
            int rate = (int)(5*(float) screenWidth/320); //我自己测试这个倍数比较适合，当然你可以测试后再修改  
            return rate<15?15:rate; //字体太小也不好看的  
    }  
    
    protected class MyAdapter extends BaseAdapter {        
    	private LayoutInflater mInflater;        
    	private boolean mhasData;  
    	int adjustFontSize;
    	//public  Map<Integer, Boolean> isSelected;     
    	@SuppressWarnings({ "static-access", "unchecked" })
    	public MyAdapter(Context context,boolean hasData,int nAdjustFontSize)
    	{           
    		adjustFontSize = nAdjustFontSize;  
    		mInflater = LayoutInflater.from(context); 
    		mhasData=hasData;
    	}    
    	
    	@Override        
    	public int getCount() {      		
    		TaxiRequestIndexResponse taxiRequestIndexResponse=mApp.getCurrentTaxiRequestIndex();
    		return taxiRequestIndexResponse.getSize();
    		}            
    	@Override       
    	public Object getItem(int position) { 
    		TaxiRequestIndexResponse taxiRequestIndexResponse=mApp.getCurrentTaxiRequestIndex();
    		return  taxiRequestIndexResponse.getTaxiRequest(position);   
    		}            
    	@Override        
    	public long getItemId(int position) {
    		return position;        
    		}            
    	@Override        
    	public View getView(int position, View convertView, ViewGroup parent) {
    		ViewHolder holder = null;           
    		//convertView为null的时候初始化convertView。           
    		if (convertView == null) { 
    			holder = new ViewHolder();  
    			convertView = mInflater.inflate(R.layout.taxi_requestindex_item, null); 
    			holder.date 	= (TextView) convertView.findViewById(R.id.date);
    			holder.month	= (TextView) convertView.findViewById(R.id.taxi_request_index_month);
    			holder.source = (TextView) convertView.findViewById(R.id.source);    
    			holder.state = (TextView) convertView.findViewById(R.id.state); 
    			convertView.setTag(holder);            
    		} else {   
    			holder = (ViewHolder) convertView.getTag(); 
    		}         		
    		
    		TaxiRequestIndexResponse taxiRequestIndexResponse=mApp.getCurrentTaxiRequestIndex();
    		TaxiRequest tx=(TaxiRequest) taxiRequestIndexResponse.getTaxiRequest(position);
    			
    		holder.date.setText(tx.getCreatedAt("dd日"));
    		holder.month.setText(tx.getCreatedAt("MM月"));
    		holder.source.setText("打车位置:"+tx.getSource());
    		holder.state.setText("交易状态:"+tx.getHumanBreifTextState());
    		/*
    		holder.date.setTextSize(adjustFontSize+5);    
    		holder.source.setTextSize(adjustFontSize+3);    
    		holder.state.setTextSize(adjustFontSize+3);    
    		*/
    		if(tx.isTaxiRequestSuccess())
    			holder.state.setTextColor(Color.GREEN);
    		else
    			holder.state.setTextColor(Color.RED);
    		holder.state.setTypeface(Typeface.MONOSPACE,Typeface.ITALIC);
    		//holder.cBox.setChecked(isSelected.get(position));            
    		return convertView;        
    	}            
    	
    	public final class ViewHolder { 
    		
    		public TextView date;
    		public TextView month;
    		public TextView source;  
    		public TextView state;   		   
    	}       	
    }
    
}



