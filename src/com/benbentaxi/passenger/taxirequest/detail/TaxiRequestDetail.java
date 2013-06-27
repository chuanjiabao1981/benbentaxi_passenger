package com.benbentaxi.passenger.taxirequest.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.benbentaxi.passenger.R;
import com.benbentaxi.passenger.demo.DemoApplication;
import com.benbentaxi.passenger.taxirequest.TaxiRequestApiConstant;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TaxiRequestDetail extends Activity {

	private static Object DetailTable[][]=
		{
			{R.drawable.user,"司机姓名",TaxiRequestApiConstant.ID},
			{R.drawable.plate,"司机车牌",TaxiRequestApiConstant.PLATE},
			{R.drawable.telephone,"司机电话",TaxiRequestApiConstant.DRIVER_MOBILE},
			{R.drawable.location,"距离约为(公里)",TaxiRequestApiConstant.DISTANCE}
		};
	
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
	@SuppressWarnings("static-access")
	private List<Map<String, Object>> getData() {  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();        
        DemoApplication app = (DemoApplication)getApplicationContext();
        if (app.getCurrentShowTaxiRequest() == null){
        	return list;
        }
        for(int i =0;i<DetailTable.length;i++ ){
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("taxi_request_detail_item_icon",DetailTable[i][0]);
            map.put("taxi_request_detail_item_title",DetailTable[i][1]);
            map.put("tqxi_request_detail_item_content", app.getCurrentShowTaxiRequest().getField((String) (DetailTable[i][2])));
            list.add(map);  
        } 
        return list;  
    }  

}
