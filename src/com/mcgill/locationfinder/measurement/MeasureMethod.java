package com.mcgill.locationfinder.measurement;

import java.util.ArrayList;

import android.content.Context;

public abstract class MeasureMethod implements Runnable {
	public static final int DEFAULT_TIMES = 5;
	
	protected ArrayList<Double> distanceList = new ArrayList<Double>();
	protected ArrayList<MeasurementListener> listeners = new ArrayList<MeasurementListener>();
	protected String name = null;
	protected String server_address =null;
	protected int measure_times = DEFAULT_TIMES;
	protected Context context =null;

	public abstract void initialize();
	
	public abstract void waitForConnection();
	
	public abstract void connect();
	
	public abstract void measure() throws MeasurementException;
	
	public abstract double calculateDis(Object o);
	
	public void setServerAddress(String address){
		server_address=address;
	}
	public void setContext(Context context){
		this.context=context;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			synchronized(distanceList){
				measure();
				distanceList.clear();
			}
		}
		catch(MeasurementException e){
			for(MeasurementListener l:listeners){
				l.failedToMeasure(e.getFailCode(), e.getMessage());
			}
		}
	}
	
	//can be only accessed in the package
	void setMeasurementListener(ArrayList<MeasurementListener> listeners){
		this.listeners=listeners;
	}

	void addMeasurementListener(MeasurementListener listener){
		this.listeners.add(listener);
	}
	
	public void setMeasure_times(int measure_times) {
		this.measure_times = measure_times;
	}
	
	public void addDistance(double distance){
		distanceList.add(distance);
		if(getDistanceSize()>=3){
			(new Thread(this)).start();
		}
	}
	
	public int getDistanceSize(){
		return distanceList.size();
	}
	
	public void clear(){
		distanceList.clear();
		listeners.clear();
	}
}
