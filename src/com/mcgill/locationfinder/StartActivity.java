package com.mcgill.locationfinder;

import com.mcgill.locationfinder.bluetooth.BluetoothPairedList;
import com.mcgill.locationfinder.communication_wifi.CommunicationService;
import com.mcgill.locationfinder.communication_wifi.CommunicationService.ServiceBinder;
import com.mcgill.locationfinder.measurement.MeasureController;
import com.mcgill.locationfinder.measurement.MeasurementException;
import com.mcgill.locationfinder.measurement.MethodList;
import com.mcgill.locationfinder.measurement.beepbeep.MeasurementMethod_Beepbeep;
import com.mcgill.locationfinder.measurement.bluetooth.MeasurementMethod_Bluetooth;
import com.mcgill.locationfinder.measurement.wifi.MeasurementMethod_WIFI;

import android.os.Bundle;
import android.os.IBinder;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {

	public static final String SIDE_MESSAGE = "com.mcgill.locationfinder.measurement.side";
	public static final String ADDRESS_MESSAGE = "com.mcgill.locationfinder.measurement.address";

	public static final String Server_side = "server";
	public static final String Client_side = "client";

	private Button server_button;
	private Button client_button;
	private MeasureController controller;
	
	private OnSharedPreferenceChangeListener listener;
	
	private CommunicationService comServ;
	private boolean bound = false;
	
	private static final int REQUEST_ENABLE_BT = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		controller=MeasureController.getInstance();
		
		server_button = (Button)findViewById(R.id.server_button);
		client_button = (Button)findViewById(R.id.client_button);
		
		server_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StartActivity.this, CommunicationService.class);
				bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
				if(controller.getMethodKey().equalsIgnoreCase(MethodList.Bluetooth)){
					bluetooth_init_server();
				}
				else if((controller.getMethodKey().equalsIgnoreCase(MethodList.WIFI))){
					
				}
				else if(controller.getMethodKey().equalsIgnoreCase(MethodList.Beepbeep)){
					beepbeep_init_server();
				}
			}
			
		});
		
		client_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(controller.getMethodKey().equalsIgnoreCase(MethodList.Bluetooth)){
					bluetooth_init_client();
				}
				else if((controller.getMethodKey().equalsIgnoreCase(MethodList.WIFI))){
					
				}
				else if(controller.getMethodKey().equalsIgnoreCase(MethodList.Beepbeep)){
					beepbeep_init_client();
				}
			}
			
		});
		

		controller.registerMethod(MethodList.Bluetooth, new MeasurementMethod_Bluetooth());
		controller.registerMethod(MethodList.WIFI, new MeasurementMethod_WIFI());
		controller.registerMethod(MethodList.Beepbeep, new MeasurementMethod_Beepbeep(this.getApplicationContext()));
		
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String method_key = prefs.getString(getResources().getString(R.string.method_list_key), getResources().getString(R.string.default_method));
		try{
			Toast.makeText(StartActivity.this, method_key+" selected", Toast.LENGTH_SHORT).show();
			controller.setMeasurementMethod(method_key);
		}
		catch(MeasurementException e){
			Toast.makeText(StartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			ListPreference listpref=(ListPreference)this.getSharedPreferences(getResources().getString(R.string.method_list_key), MODE_PRIVATE);
			listpref.setValue(getResources().getString(R.string.default_method));
			try{
				controller.setMeasurementMethod(getResources().getString(R.string.default_method));
			}
			catch(MeasurementException ee){
				Toast.makeText(this, "Big bug, exiting!", Toast.LENGTH_LONG);
				finish();
			}
		}
		listener = new OnSharedPreferenceChangeListener(){

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// TODO Auto-generated method stub
				try{
					String method_key = sharedPreferences.getString(key, getResources().getString(R.string.default_method));
					Toast.makeText(StartActivity.this, method_key+" selected", Toast.LENGTH_SHORT).show();
					controller.setMeasurementMethod(method_key);
				}
				catch(Exception e){
					Toast.makeText(StartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					Editor editor=sharedPreferences.edit();
					editor.putString(key, controller.getMethodKey());
					editor.commit();
				}
			}
			
		};
		prefs.registerOnSharedPreferenceChangeListener(listener);
		
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.unbindService(mConnection);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.menu_settings:
			this.startActivity(new Intent(this,SettingsActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
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
	
	private void bluetooth_init_client(){
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
	
	private void bluetooth_init_server(){
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		else{
			Intent intent=new Intent(getBaseContext(),MeasurementActivity.class);
			intent.putExtra(StartActivity.SIDE_MESSAGE, Server_side);
			startActivity(intent);
		}
	}

	private void beepbeep_init_server(){
		Intent intent=new Intent(getBaseContext(),MeasurementActivity.class);
		intent.putExtra(StartActivity.SIDE_MESSAGE, Server_side);
		startActivity(intent);
	}
	
	private void beepbeep_init_client(){
		
		Intent intent=new Intent(getBaseContext(),MeasurementActivity.class);
		intent.putExtra(StartActivity.SIDE_MESSAGE, Client_side);
		startActivity(intent);
		
	}
	
	private ServiceConnection mConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			ServiceBinder binder = (ServiceBinder)service;
			comServ = binder.getService();
			bound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			bound = false;
		}
		
	};
}
