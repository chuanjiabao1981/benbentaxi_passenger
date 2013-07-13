package com.benbentaxi.passenger.taxirequest.index;

import java.util.ArrayList;    
import java.util.HashMap;    
import java.util.List;   
import java.util.Map; 

import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.location.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequest;
import com.benbentaxi.passenger.taxirequest.detail.TaxiRequestDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup;
import android.widget.BaseAdapter;  
import android.widget.CheckBox;   
import android.widget.ImageView;    
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
		
		
		setContentView(R.layout.activity_taxirequstindex);
		
		mApp=(DemoApplication)this.getApplication();
		
		listview = (ListView)findViewById(R.id.lv);  
		
		listview.setOnItemClickListener(new OnItemClickListener(){			
			@Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                
					Log.i("arg2:",String.valueOf(arg2));					
					TaxiRequest tx=(TaxiRequest)TaxiRequestIndexActivity.this.adapter.mData.get(arg2).get("obj");
					
					
                	mApp.setCurrentShowTaxiRequest(tx);
                	Intent taxiRequestDetailIntent = new Intent(TaxiRequestIndexActivity.this,TaxiRequestDetail.class);
                    startActivity(taxiRequestDetailIntent);                
            }     
        });		
		
				
		showList();
    }
    
    public void showList(){    	
    	
    	TaxiRequestIndexResponse taxiRequestIndexResponse=mApp.getCurrentTaxiRequestIndex();
    	listview = (ListView)findViewById(R.id.lv);  
		
		adapter = new MyAdapter(this,taxiRequestIndexResponse);  
		
		listview.setAdapter(adapter);  
		
		
    }

    
    protected class MyAdapter extends BaseAdapter {        
    	private LayoutInflater mInflater;        
    	private ArrayList<Map<String, Object>> mData;        
    	//public  Map<Integer, Boolean> isSelected;     
    	@SuppressWarnings({ "static-access", "unchecked" })
    	public MyAdapter(Context context,TaxiRequestIndexResponse taxiRequestIndexResponse) 
    	{           
    		mInflater = LayoutInflater.from(context);  
    		
    		mData=new ArrayList<Map<String, Object>>();  
    		for (int i = 0; i < taxiRequestIndexResponse.getSize(); i++) 
    		{               
    			TaxiRequest tx=taxiRequestIndexResponse.getTaxiRequest(i);
    			
    			Map<String, Object> map = new HashMap<String, Object>();   
    			map.put("date",tx.getPassengerMobile());           
    			map.put("state",tx.getState());           
    			map.put("driver_mobile",  tx.getPassengerMobile());    
    			map.put("obj", tx);
    			mData.add(map);         
    			}
    		
    		
    	}           
    	//初始化       
    	/*private void init() { 
    		
    		//这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。   
    		//isSelected = new HashMap<Integer, Boolean>();     
    		for (int i = 0; i < mData.size(); i++) { 
    			isSelected.put(i, false);           
    			}     
    		}   */ 
    	@Override        
    	public int getCount() {  
    		return mData.size();        
    		}            
    	@Override       
    	public Object getItem(int position) { 
    		return  mData.get(position);       
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
    			holder.date = (TextView) convertView.findViewById(R.id.date);    
    			holder.state = (TextView) convertView.findViewById(R.id.state);    
    			holder.driver_mobile = (TextView) convertView.findViewById(R.id.driver_mobile);  
    			//holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);     
    			convertView.setTag(holder);            
    		} else {   
    			holder = (ViewHolder) convertView.getTag(); 
    		}     
    		TaxiRequest tx=(TaxiRequest) mData.get(position).get("obj");
    		holder.date.setText((String)mData.get(position).get("date"));
    		holder.state.setText((String)mData.get(position).get("state"));
    		holder.driver_mobile.setText((String)mData.get(position).get("driver_mobile"));
    		//holder.cBox.setChecked(isSelected.get(position));            
    		return convertView;        
    	}            
    	
    	public final class ViewHolder { 
    		public TextView date;      
    		public TextView state;      
    		public TextView driver_mobile;          
    		//public CheckBox cBox;        
    	}       	
    }
    
}



