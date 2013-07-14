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
					TaxiRequestIndexResponse taxiRequestIndexResponse=mApp.getCurrentTaxiRequestIndex();
		    		TaxiRequest tx=(TaxiRequest) taxiRequestIndexResponse.getTaxiRequest(arg2);
                	mApp.setCurrentShowTaxiRequest(tx);
                	Intent taxiRequestDetailIntent = new Intent(TaxiRequestIndexActivity.this,TaxiRequestDetail.class);
                    startActivity(taxiRequestDetailIntent);                
            }     
        });		
		
		
		showList();
    }
    
    protected void showList(){    	
    	boolean hasData=true;
    	TaxiRequestIndexResponse taxiRequestIndexResponse=mApp.getCurrentTaxiRequestIndex();
    	listview = (ListView)findViewById(R.id.lv);  
		
    	if(taxiRequestIndexResponse==null)
    		return;
		adapter = new MyAdapter(this,hasData);  
		
		listview.setAdapter(adapter);  
		
		
    }

    
    protected class MyAdapter extends BaseAdapter {        
    	private LayoutInflater mInflater;        
    	private boolean mhasData;        
    	//public  Map<Integer, Boolean> isSelected;     
    	@SuppressWarnings({ "static-access", "unchecked" })
    	public MyAdapter(Context context,boolean hasData) 
    	{           
    		mInflater = LayoutInflater.from(context); 
    		mhasData=hasData;
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
    			holder.date = (TextView) convertView.findViewById(R.id.date);    
    			holder.state = (TextView) convertView.findViewById(R.id.state);    
    			holder.index = (TextView) convertView.findViewById(R.id.index);  
    			//holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);     
    			convertView.setTag(holder);            
    		} else {   
    			holder = (ViewHolder) convertView.getTag(); 
    		}     
    		
    		
    		TaxiRequestIndexResponse taxiRequestIndexResponse=mApp.getCurrentTaxiRequestIndex();
    		TaxiRequest tx=(TaxiRequest) taxiRequestIndexResponse.getTaxiRequest(position);
    			
    		holder.date.setText(tx.getCreatedAt());
    		holder.state.setText(tx.getHumanBreifTextState());
    		holder.index.setText(String.valueOf(position+1));
    		//holder.cBox.setChecked(isSelected.get(position));            
    		return convertView;        
    	}            
    	
    	public final class ViewHolder { 
    		public TextView index;      
    		public TextView date;      
    		public TextView state;   
    		//public CheckBox cBox;        
    	}       	
    }
    
}



