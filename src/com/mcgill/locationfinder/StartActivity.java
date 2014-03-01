package com.mcgill.locationfinder;

import com.mcgill.locationfinder.bluetooth.BluetoothPairedList;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends Activity {

	private Button bluetooth_button;
	private Button wifi_button;
	private Button beepbeep_button;
	
	private static final int REQUEST_ENABLE_BT = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		this.setContentView(R.layout.activity_start);
		bluetooth_button = (Button)findViewById(R.id.bluetooth_button);
		wifi_button = (Button)findViewById(R.id.wifi_button);
		beepbeep_button = (Button)findViewById(R.id.beepbeep_button);
		bluetooth_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (mBluetoothAdapter == null) {
				    // Device does not support Bluetooth
				}
				
				if (!mBluetoothAdapter.isEnabled()) {
				    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}
				else{
					startActivity(new Intent(getBaseContext(),BluetoothPairedList.class));
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
		case REQUEST_ENABLE_BT:
			if(resultCode == Activity.RESULT_OK){
				this.startActivity(new Intent(getBaseContext(),BluetoothPairedList.class));
			}
		}
		
	}
	
	

}
