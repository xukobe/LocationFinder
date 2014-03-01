package com.mcgill.locationfinder.bluetooth;

import java.util.Set;

import com.mcgill.locationfinder.R;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BluetoothPairedList extends ListActivity{

	private ArrayAdapter<BluetoothDevice> mArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}
		
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		
		mArrayAdapter = new ArrayAdapter<BluetoothDevice>(this, R.layout.bluetooth_device_view);
		
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        mArrayAdapter.add(device);
		    }
		}
		
		this.setListAdapter(mArrayAdapter);
		
		ListView listview = this.getListView();
		
		listview.setTextFilterEnabled(true);
		
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(BluetoothPairedList.this, getListAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();
			}
			
		});
		
		
		
	}

	
	
}
