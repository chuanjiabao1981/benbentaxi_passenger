package com.benbentaxi.passenger.taxirequest.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.benbentaxi.passenger.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TaxiRequestDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taxi_request_detail);
		
		setContentView(R.layout.activity_taxi_request_detail);
        ListView lv= (ListView)findViewById(R.id.detail_info_list);

        lv.setAdapter(new SimpleAdapter(this, getData(), R.layout.taxi_request_detail_item,   
                new String[]{"taxi_request_detail_item_icon", "taxi_request_detail_item_title", "tqxi_request_detail_item_content"},   
                new int[]{R.id.taxi_request_detail_item_icon, R.id.taxi_request_detail_item_title, R.id.tqxi_request_detail_item_content}));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_taxi_request_detail, menu);
		return true;
	}
	private List<Map<String, Object>> getData() {  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();        
          
        //for(int i = 0; i < 1; i++) {  
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("taxi_request_detail_item_icon", R.drawable.distance);
            map.put("taxi_request_detail_item_title", "aaaaa");
            map.put("tqxi_request_detail_item_content", "content1");
            list.add(map);  

            map = new HashMap<String, Object>();
            map.put("taxi_request_detail_item_icon", R.drawable.user);
            map.put("taxi_request_detail_item_title", "bbbb");
            map.put("tqxi_request_detail_item_content", "content2");
            list.add(map);  
            
            
            map = new HashMap<String, Object>();
            map.put("taxi_request_detail_item_icon", R.drawable.location2);
            map.put("taxi_request_detail_item_title", "bbbb");
            map.put("tqxi_request_detail_item_content", "content2");
            list.add(map);
            map = new HashMap<String, Object>();
            map.put("taxi_request_detail_item_icon", R.drawable.location);
            map.put("taxi_request_detail_item_title", "bbbb");
            map.put("tqxi_request_detail_item_content", "content2");
            list.add(map);
        //}  
          
        return list;  
    }  

}
