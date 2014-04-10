package com.mcgill.locationfinder.communication_wifi;

import java.util.Set;

import com.mcgill.locationfinder.MeasurementActivity;
import com.mcgill.locationfinder.R;
import com.mcgill.locationfinder.StartActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WIFIP2PList extends ListActivity{

	private ArrayAdapter<WifiP2pDevice> mArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
		
		ListView listview = this.getListView();
		
		listview.setTextFilterEnabled(true);
		
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getBaseContext(),MeasurementActivity.class);
				intent.putExtra(StartActivity.SIDE_MESSAGE, StartActivity.Client_side);
				intent.putExtra(StartActivity.ADDRESS_MESSAGE, ((BluetoothDevice)getListAdapter().getItem(position)).getAddress());
				startActivity(intent);
				Toast.makeText(WIFIP2PList.this, getListAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();
			}
			
		});
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	

	
	
}
