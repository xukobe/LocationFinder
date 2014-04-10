package com.mcgill.locationfinder.measurement.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import com.mcgill.locationfinder.measurement.MeasureMethod;
import com.mcgill.locationfinder.measurement.MeasurementException;
import com.mcgill.locationfinder.measurement.MeasurementListener;

public class MeasurementMethod_Bluetooth extends MeasureMethod {
	
	private static String NAME = "LocationFinder_Bluetooth";
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		name = NAME;
	}

	@Override
	public void measure() throws MeasurementException {
		// TODO Auto-generated method stub
		double sum=0;
		for(Double d: distanceList){
			sum+=d;
		}
		for(MeasurementListener l: listeners){
    		l.distanceMeasuredSucced(sum);
    	}
	}
	
	@Override
	public double calculateDis(Object o) {
		// TODO Auto-generated method stub
		short rssi = (Short)o;
		
		return (double)((Short)o);
	}

	@Override
	public void waitForConnection() {
		// TODO Auto-generated method stub
		Intent discoverableIntent = new
		Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
		if(context!= null)
			context.startActivity(discoverableIntent);
		else{
			for(MeasurementListener l: listeners){
        		l.failedToMeasure(MeasurementException.NO_CONTEXT, "no context");
        	}
		}
	}
	
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			for(MeasurementListener l: listeners){
        		l.failedToMeasure(MeasurementException.BLUETOOTH_NOT_AVAILABLE, "Device does not support Bluetooth");
        	}
		}
		while(!mBluetoothAdapter.cancelDiscovery());
		if(!mBluetoothAdapter.isDiscovering()){
			mBluetoothAdapter.startDiscovery();
		}
	}

}
