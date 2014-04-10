package com.mcgill.locationfinder;

import java.util.ArrayList;

import com.mcgill.locationfinder.communication_wifi.CommunicationService;
import com.mcgill.locationfinder.communication_wifi.CommunicationService.ServiceBinder;
import com.mcgill.locationfinder.measurement.MeasureController;
import com.mcgill.locationfinder.measurement.MeasureMethod;
import com.mcgill.locationfinder.measurement.MeasurementException;
import com.mcgill.locationfinder.measurement.MeasurementListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MeasurementActivity extends Activity {

	public static final int HANDLER_RAW_VALUE = 0;
	public static final int HANDLER_DISTANCE = 1;
	public static final int HANDLER_FAILEDCODE = 2;
	
	private String side;
	private String server_address;
	private MeasureController controller;
	private TextView rawTextView;
	private TextView distanceTextView;
	private Button connectButton;
	
	private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
	
    private CommunicationService comServ;
	private boolean bound = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measurement);
		rawTextView = (TextView)findViewById(R.id.raw_textview);
		distanceTextView = (TextView)findViewById(R.id.distance_textview);
		connectButton = (Button)findViewById(R.id.connect_button);
		connectButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					
					controller.connect(MeasurementActivity.this,server_address);
					
				} catch (MeasurementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		Intent intent = this.getIntent();
		side = intent.getStringExtra(StartActivity.SIDE_MESSAGE);
		server_address = intent.getStringExtra(StartActivity.ADDRESS_MESSAGE);
		controller = MeasureController.getInstance();
		controller.clear();
		controller.addMeasurementListener(new MeasurementListener(){

			@Override
			public void distanceMeasuredSucced(double distance) {
				// TODO Auto-generated method stub
				handler.obtainMessage(HANDLER_DISTANCE, distance).sendToTarget();
			}

			@Override
			public void failedToMeasure(int failedCode, String reason) {
				// TODO Auto-generated method stub
				handler.obtainMessage(HANDLER_FAILEDCODE, failedCode).sendToTarget();
			}

			@Override
			public void connected(Object obj) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		if(side.equalsIgnoreCase(StartActivity.Server_side)){
			connectButton.setVisibility(View.GONE);
			connectButton.setEnabled(false);
			try {
				
				controller.startToWait(this);
				//mSensorManager.registerListener(sensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			} catch (MeasurementException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		else{
			connectButton.setVisibility(View.VISIBLE);
			connectButton.setEnabled(true);
//			try {
//				
//				controller.connect(MeasurementActivity.this,server_address);
//			} catch (MeasurementException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			//mSensorManager.registerListener(sensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
				//controller.connect(this,server_address);
		}
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);
		mSensorManager.registerListener(sensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		Intent intent = new Intent(this, CommunicationService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.unregisterReceiver(mReceiver);
		mSensorManager.unregisterListener(sensorListener);
		this.unbindService(mConnection);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
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
	
	private final SensorEventListener sensorListener= new SensorEventListener(){
		
		private final float CHANGE_THRESHOLD = (float)20;
		private final long PERIODICAL_THRESHOLD = 100;
		private final long SHAKE_TIMEOUT = 5000000;
		private final int COUNTER_THRESHOLD = 3;
		
		private float mLastX;
		private float mLastY;
		private float mLastZ;
		private long mLastTime = 0;
		private int counter=0;
		private long mLastEffective=0;
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			try{
				if(mLastTime == 0){
					mLastTime = System.currentTimeMillis();
					mLastEffective = mLastTime;
					mLastX = event.values[0];
					mLastY = event.values[1];
					mLastZ = event.values[2];
				}
				else{
					long mTime = System.currentTimeMillis();
					long timeDiff = mTime - mLastTime;
					
					if(timeDiff > PERIODICAL_THRESHOLD){
						float mX = event.values[0];
						float mY = event.values[1];
						float mZ = event.values[2];
						if(mTime - mLastEffective > SHAKE_TIMEOUT){
							counter = 0;
						}
						float changed = Math.abs(mX-mLastX)+Math.abs(mY-mLastY)+Math.abs(mZ-mLastZ);
						if(changed > CHANGE_THRESHOLD){
							if(++counter > COUNTER_THRESHOLD){
								if(side.equalsIgnoreCase(StartActivity.Server_side))
									controller.measure();
								else
									controller.connect(MeasurementActivity.this,server_address);
								counter=0;
							}
							mLastEffective = mTime;
						}
						mLastX = mX;
						mLastY = mY;
						mLastZ = mZ;
						mLastTime = mTime;
					}
				}
			
			} catch (MeasurementException e) {
				// TODO Auto-generated catch block
				Toast.makeText(MeasurementActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		
	};

	private final Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case HANDLER_RAW_VALUE:
				rawTextView.setText(msg.obj.toString());
				break;
			case HANDLER_DISTANCE:
				double distance = (Double)msg.obj;
				distanceTextView.setText(""+distance);
				break;
			case HANDLER_FAILEDCODE:
				int failedCode = (Integer)msg.obj;
				Toast.makeText(MeasurementActivity.this, ""+failedCode, Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		private int rssi_counter=0;
		private final int rssi_max_counter = 10;
		private String rssi_str="";
		private ArrayList<Integer> rssis = new ArrayList<Integer>();
		
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
	            if(device.getAddress().equals(server_address)){
	            	
	            	MeasureMethod m = controller.getMethod();
	            	if(rssi_counter < rssi_max_counter){
	            		try {
							controller.connect(MeasurementActivity.this, server_address);
							rssi_counter++;
							rssi_str = rssi_str + rssi+" ";
							rssis.add((int)rssi);
							handler.obtainMessage(HANDLER_RAW_VALUE, rssi_str).sendToTarget();
						} catch (MeasurementException e) {
							// TODO Auto-generated catch block
							rssi_counter = 0;
							rssi_str = "";
							rssis.clear();
							e.printStackTrace();
							Toast.makeText(MeasurementActivity.this, "Discovery failed!", Toast.LENGTH_SHORT).show();
						}
	            	}
	            	else{
	            		rssi_counter = 0;
	            		rssi_str = "";
	            		rssis.clear();
	            		if(m!=null){
	            			m.addDistance(m.calculateDis(rssi));
	            		}
	            	}
	            	//Toast.makeText(MeasurementActivity.this, device.getAddress()+"-- "+rssi, Toast.LENGTH_SHORT).show();
	            }
	            
	        }
	    }
	};

}
