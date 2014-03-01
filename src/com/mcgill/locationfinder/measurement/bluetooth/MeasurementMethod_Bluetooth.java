package com.mcgill.locationfinder.measurement.bluetooth;

import android.bluetooth.BluetoothAdapter;

import com.mcgill.locationfinder.measurement.MeasureMethod;
import com.mcgill.locationfinder.measurement.MeasurementException;

public class MeasurementMethod_Bluetooth extends MeasureMethod {
	
	private BluetoothAdapter mBluetoothAdapter;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
	}

	@Override
	public double measure() throws MeasurementException {
		// TODO Auto-generated method stub
		return 0;
	}

}
