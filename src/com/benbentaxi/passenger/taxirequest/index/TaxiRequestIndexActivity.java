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
					
					//Log.i("tx.mobile:",tx.getPassengerMobile());
                	mApp.setCurrentShowTaxiRequest(tx);
                	Intent taxiRequestDetailIntent = new Intent(TaxiRequestIndexActivity.this,TaxiRequestDetail.class);
                    startActivity(taxiRequestDetailIntent);                
            }     
        });		
		
		TaxiRequestIndexTask tsk=new TaxiRequestIndexTask(this);
		tsk.go();
    }
    
    public void showList(ArrayList<TaxiRequest> list){
    	listview = (ListView)findViewById(R.id.lv);  
		
		adapter = new MyAdapter(this,list);  
		
		listview.setAdapter(adapter);  
		
		
    }

    
    protected class MyAdapter extends BaseAdapter {        
    	private LayoutInflater mInflater;        
    	private ArrayList<Map<String, Object>> mData;        
    	//public  Map<Integer, Boolean> isSelected;     
    	@SuppressWarnings({ "static-access", "unchecked" })
    	public MyAdapter(Context context,ArrayList<TaxiRequest> list) 
    	{           
    		mInflater = LayoutInflater.from(context);  
    		
    		//mData=(ArrayList<TaxiRequest>) list.clone();
    		//init();        
    		
    		mData=new ArrayList<Map<String, Object>>();  
    		for (int i = 0; i < list.size(); i++) 
    		{               
    			TaxiRequest tx=list.get(i);
    			
    			Map<String, Object> map = new HashMap<String, Object>();   
    			map.put("date",tx.getPassengerMobile());           
    			map.put("desc",tx.getState());           
    			map.put("title",  tx.getPassengerMobile());    
    			map.put("obj", tx);
    			mData.add(map);         
    			}
    		
    		
    	}           
    	//初始化       
    	/*private void init() { 
    		mData=new ArrayList<Map<String, Object>>();  
    		for (int i = 0; i < 5; i++) 
    		{               
    			Map<String, Object> map = new HashMap<String, Object>();   
    			map.put("desc","content....");           
    			map.put("title",  (i + 1) + "#");    
    			mData.add(map);         
    			}           
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
    		return null;       
    		}            
    	@Override        
    	public long getItemId(int position) {
    		return 0;        
    		}            
    	@Override        
    	public View getView(int position, View convertView, ViewGroup parent) {
    		ViewHolder holder = null;           
    		//convertView为null的时候初始化convertView。           
    		if (convertView == null) { 
    			holder = new ViewHolder();  
    			convertView = mInflater.inflate(R.layout.taxi_requestindex_item, null); 
    			holder.date = (TextView) convertView.findViewById(R.id.date);    
    			holder.desc = (TextView) convertView.findViewById(R.id.desc);    
    			holder.title = (TextView) convertView.findViewById(R.id.title);  
    			holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);     
    			convertView.setTag(holder);            
    		} else {   
    			holder = (ViewHolder) convertView.getTag(); 
    		}     
    		TaxiRequest tx=(TaxiRequest) mData.get(position).get("obj");
    		holder.date.setText((String)mData.get(position).get("date"));
    		holder.desc.setText((String)mData.get(position).get("desc"));
    		holder.title.setText((String)mData.get(position).get("title"));
    		//holder.cBox.setChecked(isSelected.get(position));            
    		return convertView;        
    	}            
    	
    	public final class ViewHolder { 
    		public TextView date;      
    		public TextView desc;      
    		public TextView title;          
    		public CheckBox cBox;        
    	}       	
    }
    
}



